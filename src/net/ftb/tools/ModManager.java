package net.ftb.tools;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import net.ftb.data.ModPack;
import net.ftb.data.Settings;
import net.ftb.gui.LaunchFrame;
import net.ftb.gui.dialogs.ModpackUpdateDialog;
import net.ftb.log.Logger;
import net.ftb.util.FileUtils;

public class ModManager extends JDialog {
	private static final long serialVersionUID = 6897832855341265019L;
	public static boolean update = true, saveConfiguration = true;
	private JPanel contentPane;
	private double downloadedPerc;
	private final JProgressBar progressBar;
	private final JLabel label;
	private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

	private class ModManagerWorker extends SwingWorker<Boolean, Void> {
		@Override
		protected Boolean doInBackground() throws IOException, NoSuchAlgorithmException {
			if(!upToDate()) {
				Logger.logInfo("Not up to date!");
				String installPath = Settings.getSettings().getInstallPath();
				ModPack pack = ModPack.getPack(LaunchFrame.getSelectedModIndex());
				File modPackZip = new File(installPath + File.separator + "temp" + File.separator + pack.getDir() + File.separator + pack.getModFileName());
				if(modPackZip.exists()) {
					modPackZip.delete();
				}
								
				try {
					new File(installPath + "/temp/" + pack.getDir() + "/").mkdir();
					downloadModPack(pack);
				} catch (MalformedURLException e) { 
				} catch (NoSuchAlgorithmException e) { 
				} catch (IOException e) { }
			}
			return false;
		}

		public void downloadUrl(String filename, String urlString) throws IOException, NoSuchAlgorithmException {
			BufferedInputStream in = null;
			FileOutputStream fout = null;
			try {
				in = new BufferedInputStream(new URL(urlString).openStream());
				fout = new FileOutputStream(filename);
				byte data[] = new byte[1024];
				int count;
				double amount = 0;
				
				URL url = new URL(LaunchFrame.getFullLink(ModPack.getPack(LaunchFrame.getSelectedModIndex()).getUrl()));
				int modPackSize = url.openConnection().getContentLength();
				
				progressBar.setMaximum(10000);
				int steps = 0;
				while ((count = in.read(data, 0, 1024)) != -1) {
					fout.write(data, 0, count);
					downloadedPerc += (count * 1.0 / modPackSize) * 100;
					amount += count;
					steps++;
					if (steps > 100) {
						steps = 0;
						progressBar.setValue((int)downloadedPerc * 100);
						label.setText(decimalFormat.format(amount / 1048576) + "MB / " + decimalFormat.format(modPackSize / 1048576) + "MB");
					}
				}
			} finally {
				in.close();
				fout.flush();
				fout.close();
			}
		}

		protected void downloadModPack(ModPack pack) throws IOException, NoSuchAlgorithmException {
			System.out.println("Downloading");
			String basePath = Settings.getSettings().getInstallPath() + "/temp/" + pack.getDir() + "/";
			
			new File(basePath).mkdirs();
			new File(basePath + pack.getModFileName()).createNewFile();
			downloadUrl(basePath + pack.getModFileName(), LaunchFrame.getFullLink(pack.getUrl()));
			FileUtils.extractZipTo(basePath + pack.getModFileName(), basePath);
			cleanOldInstallation(pack);
			installMods(pack.getModFileName(), pack.getDir());
		}

		private void cleanOldInstallation(ModPack pack) {
			Logger.logInfo("cleaning old installation...");
			File baseFolder = new File(Settings.getSettings().getInstallPath() + File.separator + pack.getDir());
			
			if (baseFolder.exists()) {
				ArrayList<Pattern> uninstallList = new ArrayList<Pattern>();
				if (uninstallList.size() > 0) {
					Logger.logInfo("Using to uninstall: " + pack.getDir() + "/uninstall.lst");
					File cleanFile = new File(Settings.getSettings().getInstallPath() + "/temp/" + pack.getDir() + "/uninstall.lst");
					
					for (String line : FileUtils.readLines(cleanFile)) {
						uninstallList.add(Pattern.compile(line));
					}
					
					File modsFolder = new File(baseFolder, File.separator + "minecraft" + File.separator + "mods");
					if (modsFolder.exists()) {
						cleanModFolder(modsFolder, uninstallList);
					}
					File coreModsFile = new File(baseFolder, File.separator + "minecraft" + File.separator + "coremods");
					
					if (coreModsFile.exists()) {
						cleanModFolder(coreModsFile, uninstallList);
					}
					
					File instModsFile = new File(baseFolder, File.separator + "instMods");
					
					if (instModsFile.exists()) {
						cleanModFolder(instModsFile, uninstallList);
					}
					
					File configFile = new File(baseFolder, File.separator + "minecraft" + File.separator + "config");
					if (configFile.exists()) {
						cleanModFolder(instModsFile, uninstallList);
					}
				}
				else {
					try {
						FileUtils.delete(new File(baseFolder, File.separator + "minecraft" + File.separator + "mods"));
						FileUtils.delete(new File(baseFolder, File.separator + "minecraft" + File.separator + "coremods"));
						FileUtils.delete(new File(baseFolder, File.separator + "instMods"));
						
						if (!saveConfiguration) {
							FileUtils.delete(new File(baseFolder, File.separator + "minecraft" + File.separator + "config"));
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}	
			}
		}

		protected void installMods(String modPackName, String dir) throws IOException {
			System.out.println("Installing");
			String installPath = Settings.getSettings().getInstallPath();
			LaunchFrame.jarMods = new String[new File(installPath + "/temp/" + modPackName + "/instMods").listFiles().length];

			try {
				FileInputStream fstream = new FileInputStream(installPath + "/temp/" + modPackName + "/modlist");
				DataInputStream in1 = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in1));
				String strLine;
				int i = 0;
				while ((strLine = br.readLine()) != null) {
					// Print the content on the console
					LaunchFrame.jarMods[i] = strLine;
					i++;
				}
				//Close the input stream
				in1.close();
			} catch (Exception e) { System.err.println("Error: " + e.getMessage()); }
			LaunchFrame.jarMods = reverse(LaunchFrame.jarMods);
		}
		
		protected String[] reverse(String[] x) {
			String buffer[] = new String[x.length];
			for(int i = 0; i < x.length; i++) {
				buffer[i] = x[x.length - i - 1];
			}
			return buffer;
		}
	}

	/**
	 * Create the frame.
	 */
	public ModManager(JFrame owner, Boolean model) {
		super(owner, model);
		setResizable(false);
		setTitle("Downloading...");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 313, 138);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(LaunchFrame.getInstance());

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 63, 278, 22);
		contentPane.add(progressBar);

		JLabel lblDownloadingModPack = new JLabel("Downloading mod pack...\nPlease Wait");
		lblDownloadingModPack.setHorizontalAlignment(SwingConstants.CENTER);
		lblDownloadingModPack.setBounds(10, 11, 278, 14);
		contentPane.add(lblDownloadingModPack);

		label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(10, 36, 278, 14);
		contentPane.add(label);

		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				ModManagerWorker worker = new ModManagerWorker() {
					@Override
					protected void done() {
						setVisible(false);
						super.done();
					}
				};
				worker.execute();
			}
			@Override public void windowActivated(WindowEvent e) { }
			@Override public void windowClosed(WindowEvent e) { }
			@Override public void windowClosing(WindowEvent e) { }
			@Override public void windowDeactivated(WindowEvent e) { }
			@Override public void windowDeiconified(WindowEvent e) { }
			@Override public void windowIconified(WindowEvent e) { }
		});
	}

	private boolean upToDate() throws IOException {
		
		ModPack pack = ModPack.getPack(LaunchFrame.getSelectedModIndex());
		File version = new File(Settings.getSettings().getInstallPath() + File.separator + pack.getDir() + File.separator + "version");
		if(!version.exists()) {
			System.out.println("File not found.");
			version.getParentFile().mkdirs();
			version.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(version));
			out.write(pack.getVersion());
			out.flush();
			out.close();
			return false;
		}
		BufferedReader in = new BufferedReader(new FileReader(version));
		String line;
		
		if((line = in.readLine()) == null || Integer.parseInt(pack.getVersion()) > Integer.parseInt(line)) {
			System.out.println("File found, out of date.");
			ModpackUpdateDialog p = new ModpackUpdateDialog(LaunchFrame.getInstance(), true);
			p.setVisible(true);
			in.close();
			if(!update) {
				return true;
			}
			

			BufferedWriter out = new BufferedWriter(new FileWriter(version));
			out.write(pack.getVersion());
			out.flush();
			out.close();

			return false;
		} else {
			System.out.println("File found, up to date.");
			in.close();
			return true;
		}
	}

	public static void cleanUp() {
		ModPack pack = ModPack.getPack(LaunchFrame.getSelectedModIndex());
		File tempFolder = new File(Settings.getSettings().getInstallPath() + File.separator + "temp" + File.separator + pack.getDir() + File.separator);
		for(String file : tempFolder.list()) {
			Logger.logInfo("cleanUp:" + file);
			if(!file.equals(pack.getLogoName()) && !file.equals(pack.getImageName()) && !file.equals("version")) {
				try {
					FileUtils.delete(new File(tempFolder, file));
				} catch (IOException e) { }
			}
		}
	}
	
	private void cleanModFolder(File folder, ArrayList<Pattern> uninstallList, String base) {
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				cleanModFolder(f, uninstallList, base + f.getName() + "/");
			}
			else if (f.isFile()) {
				if (mustBeUninstall(f, base, uninstallList)) {
					f.delete();
				}
			}
		}
	}
	
	private void cleanModFolder(File folder, ArrayList<Pattern> uninstallList) {
		cleanModFolder(folder, uninstallList, folder.getName() + "/");
	}
	
	private boolean mustBeUninstall(File file, String base, ArrayList<Pattern> uninstallList) {
		
		if (!file.isFile()) {
			return false;
		}
		
		if (uninstallList.size() == 0) {
			return true;
		}
		
		String path = (base == null ? "" : base + file.getName());
		
		for (Pattern p : uninstallList) {
			if (p.matcher(path).matches()) {
				return true;
			}
		}
		
		return false;
	}
}

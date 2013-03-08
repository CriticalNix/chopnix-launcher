package net.ftb.gui;

import ircclient.Main;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.ftb.locale.I18N;
import net.ftb.locale.I18N.Locale;
import net.ftb.mclauncher.MinecraftLauncher;
import net.ftb.updater.UpdateChecker;

import com.chopnix.data.LoginResponse;
import com.chopnix.data.ModPack;
import com.chopnix.data.Settings;
import com.chopnix.data.UserManager;
import com.chopnix.dialogs.LauncherUpdateDialog;
import com.chopnix.dialogs.PasswordDialog;
import com.chopnix.dialogs.PlayOfflineDialog;
import com.chopnix.dialogs.ProfileAdderDialog;
import com.chopnix.dialogs.ProfileEditorDialog;
import com.chopnix.log.Logger;
import com.chopnix.panes.ILauncherPane;
import com.chopnix.panes.ModpacksPane;
import com.chopnix.panes.NewsPane;
import com.chopnix.panes.Online;
import com.chopnix.panes.OptionsPane;
import com.chopnix.panes.TexturepackPane;
import com.chopnix.ru.Repo;
import com.chopnix.tools.MinecraftVersionDetector;
import com.chopnix.tools.ModManager;
import com.chopnix.tools.TexturePackManager;
import com.chopnix.util.ErrorUtils;
import com.chopnix.util.FileUtils;
import com.chopnix.util.OSUtils;
import com.chopnix.workers.GameUpdateWorker;
import com.chopnix.workers.LoginWorker;

public class LaunchFrame extends JFrame {

	private static String version = Repo.VERSION;
	private static int buildNumber = Repo.BUILD_NUMBER;
	private static final String FORGENAME = "MinecraftForge.zip";
	private static String currentmd5 = "";

	@SuppressWarnings("unused")
	private Online online;
	private NewsPane newsPane;
	private OptionsPane optionsPane;
	private ModpacksPane modPacksPane;
	private TexturepackPane tpPane;
	private JPanel panel = new JPanel();
	private static JLabel backgroundLabel;
	private JPanel footer = new JPanel();
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JButton launch = new JButton(), edit = new JButton(),
			tpInstall = new JButton();
	private JLabel tpInstallLocLbl = new JLabel();
	private static String[] dropdown_ = { "Select Profile", "Create Profile" };
	private static JComboBox users, tpInstallLocation;
	private static final long serialVersionUID = 1L;
	private static LaunchFrame instance = null;
	private LoginResponse RESPONSE;

	protected static UserManager userManager;

	public static String[] jarMods;
	public static String tempPass = "";
	public static Panes currentPane = Panes.MODPACK;

	public enum Panes {
		NEWS, OPTIONS, MODPACK, TEXTURE
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            - CLI arguments
	 */
	public static void main1(String[] args) {

		// Load settings
		try {
			Settings.initSettings();
		} catch (IOException e) {
		}

		Logger.logInfo("ChopNix up (version " + version + ")");
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		String now = dateFormatGmt.format(new Date());
		Logger.logInfo("Now is: " + now);
		Logger.logInfo("Java version: " + System.getProperty("java.version"));
		Logger.logInfo("Java vendor: " + System.getProperty("java.vendor"));
		Logger.logInfo("Java home: " + System.getProperty("java.home"));
		Logger.logInfo("Java specification: "
				+ System.getProperty("java.vm.specification.name")
				+ " version: "
				+ System.getProperty("java.vm.specification.version") + " by "
				+ System.getProperty("java.vm.specification.vendor"));
		Logger.logInfo("Java vm: " + System.getProperty("java.vm.name")
				+ " version: " + System.getProperty("java.vm.version") + " by "
				+ System.getProperty("java.vm.vendor"));
		Logger.logInfo("OS: " + System.getProperty("os.arch") + " "
				+ System.getProperty("os.name") + " "
				+ System.getProperty("os.version"));
		Logger.logInfo("Working directory: " + System.getProperty("user.dir"));
		Logger.logInfo("Max Memory: " + Runtime.getRuntime().maxMemory());

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Color baseColor = new Color(40, 40, 40);

				UIManager.put("control", baseColor);
				UIManager.put("text", new Color(222, 222, 222));
				UIManager.put("nimbusBase", new Color(0, 0, 0));
				UIManager.put("nimbusFocus", baseColor);
				UIManager.put("nimbusBorder", baseColor);
				UIManager.put("nimbusLightBackground", baseColor);
				UIManager.put("info", new Color(55, 55, 55));
				UIManager.put("TabbedPane.contentOpaque", false);

				try {
					for (LookAndFeelInfo info : UIManager
							.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							final JTabbedPane tabbedPane = new JTabbedPane();
							break;
						}
					}
				} catch (Exception e) {
					try {
						UIManager.setLookAndFeel(UIManager
								.getCrossPlatformLookAndFeelClassName());
					} catch (ClassNotFoundException e1) {
						Logger.logWarn("Exception occurred", e1);
					} catch (InstantiationException e1) {
						Logger.logWarn("Exception occurred", e1);
					} catch (IllegalAccessException e1) {
						Logger.logWarn("Exception occurred", e1);
					} catch (UnsupportedLookAndFeelException e1) {
						Logger.logWarn("Exception occurred", e1);
					}
				}

				// Setup localizations
				I18N.setLocale(Settings.getSettings().getLocale());
				I18N.setupLocale();

				File installDir = new File(Settings.getSettings()
						.getInstallPath());
				if (!installDir.exists()) {
					installDir.mkdirs();
				}

				userManager = new UserManager(new File(installDir, "logindata"));

				if (Settings.getSettings().isConsoleVisible()) {
					LauncherConsole con = new LauncherConsole();
					con.setVisible(true);
				}

				LaunchFrame frame = new LaunchFrame(2);
				instance = frame;
				frame.setVisible(true);

				ModPack.addListener(frame.modPacksPane);
				ModPack.loadAll();

				// TexturePack.addListener(frame.tpPane);
				// TexturePack.loadAll();

				UpdateChecker updateChecker = new UpdateChecker(buildNumber);
				if (updateChecker.shouldUpdate()) {
					LauncherUpdateDialog p = new LauncherUpdateDialog(
							updateChecker);
					p.setVisible(true);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LaunchFrame(final int tab) {
		setFont(new Font("a_FuturaOrto", Font.PLAIN, 12));
		setResizable(false);
		setTitle("ChopNix v" + version);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/home/home.png")));

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 480);
		setLocationRelativeTo(null);

		panel.setBounds(0, 0, 850, 480);
		panel.setLayout(null);
		panel.setOpaque(true);
		backgroundLabel = new JLabel("");
		backgroundLabel.setForeground(Color.WHITE);

		Random r = new Random();
		int intScreen = 1 + r.nextInt(4);

		backgroundLabel.setIcon(new ImageIcon(LaunchFrame.class
				.getResource("/gui/bg" + intScreen + ".png")));
		backgroundLabel.setBounds(0, 0, 850, 452);
		footer.setBounds(0, 380, 850, 72);
		footer.setLayout(null);
		// footer.setBackground(new Color(20, 20, 20));
		footer.setOpaque(false);
		// tabbedPane.setBackground(new Color(0, 102, 204));

		tabbedPane.setBorder(null);
		tabbedPane.setBounds(0, 0, 850, 393);
		tabbedPane.setOpaque(false);
		panel.add(tabbedPane);
		panel.add(footer);
		panel.add(backgroundLabel);
		setContentPane(panel);
		userManager.read();
		dropdown_[0] = I18N.getLocaleString("PROFILE_SELECT");
		dropdown_[1] = I18N.getLocaleString("PROFILE_CREATE");

		String[] dropdown = merge(dropdown_,
				UserManager.getNames().toArray(new String[] {}));
		users = new JComboBox(dropdown);
		if (Settings.getSettings().getLastUser() != null) {
			for (int i = 0; i < dropdown.length; i++) {
				if (dropdown[i].equalsIgnoreCase(Settings.getSettings()
						.getLastUser())) {
					users.setSelectedIndex(i);
				}
			}
		}

		users.setBounds(550, 20, 150, 30);
		users.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (users.getSelectedIndex() == 1) {
					ProfileAdderDialog p = new ProfileAdderDialog(
							getInstance(), true);
					users.setSelectedIndex(0);
					p.setVisible(true);
				}
				edit.setEnabled(users.getSelectedIndex() > 1);
			}
		});

		edit = new JButton(I18N.getLocaleString("EDIT_BUTTON"));
		edit.setBounds(480, 20, 60, 30);
		edit.setVisible(true);
		edit.setEnabled(users.getSelectedIndex() > 1);
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (users.getSelectedIndex() > 1) {
					ProfileEditorDialog p = new ProfileEditorDialog(
							getInstance(), (String) users.getSelectedItem(),
							true);
					users.setSelectedIndex(0);
					p.setVisible(true);
				}
				edit.setEnabled(users.getSelectedIndex() > 1);
			}
		});

		launch.setText(I18N.getLocaleString("LAUNCH_BUTTON"));
		launch.setBounds(711, 20, 100, 30);
		launch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (users.getSelectedIndex() > 1
						&& ModpacksPane.packPanels.size() > 0) {
					saveSettings();
					doLogin(UserManager.getUsername(users.getSelectedItem()
							.toString()), UserManager.getPassword(users
							.getSelectedItem().toString()));
				}
			}
		});

		tpInstall.setBounds(650, 20, 160, 30);
		tpInstall.setText(I18N.getLocaleString("INSTALL_TEXTUREPACK"));
		tpInstall.setVisible(false);
		tpInstall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TexturePackManager.installDir = (String) tpInstallLocation
						.getSelectedItem();
				TexturePackManager man = new TexturePackManager(new JFrame(),
						true);
				man.setVisible(true);
				TexturePackManager.cleanUp();
			}
		});

		tpInstallLocation = new JComboBox();

		tpInstallLocation.setBounds(480, 20, 160, 30);
		tpInstallLocation.setToolTipText("Install to...");
		tpInstallLocation.setVisible(false);
		tpInstallLocLbl.setText("Install to...");
		tpInstallLocLbl.setBounds(480, 20, 80, 30);
		tpInstallLocLbl.setVisible(false);

		footer.add(edit);
		footer.add(users);
		footer.add(launch);
		// footer.add(tpInstall);
		// footer.add(tpInstallLocation);

		online = new Online();
		newsPane = new NewsPane();
		modPacksPane = new ModpacksPane();
		tpPane = new TexturepackPane();
		optionsPane = new OptionsPane();

		getRootPane().setDefaultButton(launch);

		JButton btnNewButton = new JButton("IRC Chat");
		btnNewButton
				.setToolTipText("Opens a mini IRC client to give you the ability to connect to chopnix IRC. Remember to have your nickname set in the \"create a profile page\"");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					Main.main(new String[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(10, 20, 100, 30);
		footer.add(btnNewButton);

		loadSettings();
		updateLocale();

		tabbedPane.add(newsPane, 0);
		tabbedPane.setIconAt(
				0,
				new ImageIcon(this.getClass().getResource(
						"/image/tabs/news.png")));

		tabbedPane.add(optionsPane, 1);
		tabbedPane.setIconAt(
				1,
				new ImageIcon(this.getClass().getResource(
						"/image/tabs/options.png")));

		tabbedPane.add(modPacksPane, 2);
		tabbedPane.setIconAt(
				2,
				new ImageIcon(this.getClass().getResource(
						"/image/tabs/modpacks.png")));

		// TODO: Add php based online check
		tabbedPane.add(online, 3);
		tabbedPane.setIconAt(
				3,
				new ImageIcon(this.getClass().getResource(
						"/image/tabs/stats.png")));

		// TODO: De momento los paquetes de textura ocultos.
		// tabbedPane.add(tpPane, 3);
		// tabbedPane.setIconAt(3, new
		// ImageIcon(this.getClass().getResource("/image/tabs/texturepacks.png")));

		tabbedPane.setSelectedIndex(tab);

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				if (tabbedPane.getSelectedComponent() instanceof ILauncherPane) {
					((ILauncherPane) tabbedPane.getSelectedComponent())
							.onVisible();
					currentPane = Panes.values()[tabbedPane.getSelectedIndex()];
					updateFooter();
				}
			}
		});
	}

	/**
	 * call this to login
	 */
	public void doLogin(final String username, String password) {
		if (password.isEmpty()) {
			PasswordDialog p = new PasswordDialog(this, true);
			p.setVisible(true);
			if (tempPass.isEmpty()) {
				enableObjects();
				return;
			}
			password = tempPass;
		}

		Logger.logInfo("Logging in...");

		tabbedPane.setEnabledAt(0, false);
		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		// TODO: De momento los paquetes de textura ocultos.
		// tabbedPane.setEnabledAt(3, false);
		tabbedPane.getSelectedComponent().setEnabled(false);

		launch.setEnabled(false);
		users.setEnabled(false);
		edit.setEnabled(false);
		tpInstall.setEnabled(false);
		tpInstallLocation.setEnabled(false);

		LoginWorker loginWorker = new LoginWorker(username, password) {
			@Override
			public void done() {
				String responseStr;
				try {
					responseStr = get();
				} catch (InterruptedException err) {
					Logger.logError(err.getMessage(), err);
					enableObjects();
					return;
				} catch (ExecutionException err) {
					if (err.getCause() instanceof IOException
							|| err.getCause() instanceof MalformedURLException) {
						Logger.logError(err.getMessage(), err);
						PlayOfflineDialog d = new PlayOfflineDialog("mcDown",
								username);
						d.setVisible(true);
					}
					enableObjects();
					return;
				}

				LoginResponse response;
				try {
					response = new LoginResponse(responseStr);
					RESPONSE = response;
				} catch (IllegalArgumentException e) {
					if (responseStr.contains(":")) {
						Logger.logError("Received invalid response from server.");
					} else {
						if (responseStr.equalsIgnoreCase("bad login")) {
							Logger.logWarn("Invalid username or password.");
							ErrorUtils
									.tossError("Invalid username or password.");
						} else if (responseStr.equalsIgnoreCase("old version")) {
							Logger.logWarn("Outdated launcher.");
							ErrorUtils.tossError("Outdated launcher.");
						} else {
							Logger.logWarn("Login failed: " + responseStr);
							ErrorUtils
									.tossError("Login failed: " + responseStr);
						}
					}
					enableObjects();
					return;
				}
				Logger.logInfo("Login complete.");
				runGameUpdater(response);
			}
		};
		loginWorker.execute();
	}

	public static void updateTpInstallLocs(String[] locations) {
		tpInstallLocation.removeAllItems();
		for (int i = 0; i < locations.length; i++) {
			tpInstallLocation.addItem(locations[i]);
		}
	}

	public void runGameUpdater(final LoginResponse response) {
		final String installPath = Settings.getSettings().getInstallPath();
		final ModPack modpack = ModPack.getPack(modPacksPane
				.getSelectedModIndex());
		MinecraftVersionDetector mvd = new MinecraftVersionDetector();
		updateFolderStructure();

		if (!new File(installPath + "/" + modpack.getDir()
				+ "/minecraft/bin/minecraft.jar").exists()
				|| mvd.shouldUpdate(modpack.getMcVersion(), installPath + "/"
						+ modpack.getDir() + "/minecraft")) {
			final ProgressMonitor progMonitor = new ProgressMonitor(this,
					"Downloading minecraft...", "", 0, 100);
			final GameUpdateWorker updater = new GameUpdateWorker(
					modpack.getMcVersion(), "minecraft.jar",
					new File(new File(installPath, modpack.getDir()),
							"minecraft/bin").getPath(), false) {
				@Override
				public void done() {
					progMonitor.close();
					try {
						if (get()) {
							Logger.logInfo("Game update complete");
							initializeMods();
							FileUtils.killMetaInf();
							launchMinecraft(
									installPath + "/" + modpack.getDir()
											+ "/minecraft",
									RESPONSE.getUsername(),
									RESPONSE.getSessionID());
						} else {
							Logger.logError("Error occurred during downloading the game");
							ErrorUtils
									.tossError("Error occurred during downloading the game");
						}
					} catch (CancellationException e) {
						ErrorUtils.tossError("Game update canceled");
						enableObjects();
					} catch (InterruptedException e) {
						ErrorUtils.tossError("Game update interrupted");
					} catch (ExecutionException e) {
						ErrorUtils.tossError("Failed to download game");
					}
				}
			};

			updater.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (progMonitor.isCanceled()) {
						updater.cancel(false);
					}
					if (!updater.isDone()) {
						int prog = updater.getProgress();
						if (prog < 0) {
							prog = 0;
						} else if (prog > 100) {
							prog = 100;
						}
						progMonitor.setProgress(prog);
						progMonitor.setNote(updater.getStatus());
					}
				}
			});
			updater.execute();
		} else {
			initializeMods();
			launchMinecraft(
					installPath + "/" + modpack.getDir() + "/minecraft",
					RESPONSE.getUsername(), RESPONSE.getSessionID());
		}
	}

	/**
	 * @param url
	 *            - the name of the file, as saved to the repo (including
	 *            extension)
	 * @return - the direct link
	 * @throws NoSuchAlgorithmException
	 *             - see md5
	 */

	public static String getFullLink(String url)
			throws NoSuchAlgorithmException {
		String resolved;

		if (url.startsWith("bs:")) {
			resolved = Repo.BASE_URL + url.substring(3);
		} else if (url.startsWith("db:")) {
			resolved = "https://dl.dropbox.com/" + url.substring(3) + "?dl=1";
		} else if (url.startsWith("cn:")) {
			resolved = "https://chopnix.info/" + url.substring(3) + "?dl=1";
		} else if (url.startsWith("gh:")) {
			resolved = "https://raw.github.com/" + url.substring(3);
		} else if (url.startsWith("http:") || url.startsWith("https:")) {
			resolved = url;
		} else if (url.startsWith("ch:")) {
			if (currentmd5.isEmpty()) {
				currentmd5 = md5("mcepoch1" + getTime());
			}
			resolved = "http://repo.creeperhost.net/direct/FTB2/" + currentmd5
					+ "/" + url.substring(3);
		} else if (url.startsWith("sc:")) {
			resolved = "http://repo.creeperhost.net/static/FTB2/"
					+ url.substring(3);
		} else {
			resolved = url;
			Logger.logWarn("Url not resolved: " + url);
		}

		Logger.logInfo(resolved);

		return resolved;
	}

	/**
	 * @param input
	 *            - String to hash
	 * @return - hashed string
	 * @throws NoSuchAlgorithmException
	 *             - in case "MD5" isnt a correct input
	 */
	public static String md5(String input) throws NoSuchAlgorithmException {
		String result = input;
		if (input != null) {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			while (result.length() < 32) {
				result = "0" + result;
			}
		}
		return result;
	}

	/**
	 * @param filename
	 *            - what to save it as on the system
	 * @param urlString
	 *            - the url to download
	 * @throws IOException
	 *             - various
	 */
	public void downloadUrl(String filename, String urlString)
			throws IOException {
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			in = new BufferedInputStream(new URL(urlString).openStream());
			fout = new FileOutputStream(filename);

			byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (fout != null) {
				fout.flush();
				fout.close();
			}
		}
	}

	/**
	 * launch the game with the mods in the classpath
	 * 
	 * @param workingDir
	 *            - install path
	 * @param username
	 *            - the MC username
	 * @param password
	 *            - the MC password
	 */
	public void launchMinecraft(String workingDir, String username,
			String password) {
		int result = MinecraftLauncher.launchMinecraft(workingDir, username,
				password, FORGENAME, Settings.getSettings().getRamMax());
		Logger.logInfo("MinecraftLauncher said: " + result);
		if (result > 0) {
			System.exit(0);
		}
	}

	/**
	 * @param modPackName
	 *            - The pack to install (should already be downloaded)
	 * @throws IOException
	 */
	protected void installMods(String modPackName) throws IOException {
		String installpath = Settings.getSettings().getInstallPath();
		ModPack pack = ModPack.getPack(modPacksPane.getSelectedModIndex());

		Logger.logInfo("dirs mk'd");
		if (new File(installpath, pack.getDir() + "/instMods/").exists()) {
			new File(installpath, pack.getDir() + "/instMods/").delete();
		}
		if (new File(installpath, pack.getDir() + "/minecraft/mods/").exists()) {
			new File(installpath, pack.getDir() + "/minecraft/mods/").delete();
		}
		if (new File(installpath, pack.getDir() + "/minecraft/coremods/")
				.exists()) {
			new File(installpath, pack.getDir() + "/minecraft/coremods/")
					.delete();
		}
		File source = new File(installpath, "temp/" + pack.getDir()
				+ "/.minecraft");
		if (!source.exists()) {
			source = new File(installpath, "temp/" + pack.getDir()
					+ "/minecraft");
		}

		FileUtils.copyFolder(new File(source, "coremods"), new File(installpath
				+ "/" + pack.getDir() + "/minecraft/coremods/"));
		FileUtils.copyFolder(new File(source, "config"), new File(installpath
				+ "/" + pack.getDir() + "/minecraft/config/"));
		FileUtils.copyFolder(new File(source, "mods"), new File(installpath
				+ "/" + pack.getDir() + "/minecraft/mods/"));
		FileUtils.copyFolder(new File(installpath + "/temp/" + pack.getDir()
				+ "/instMods/"), new File(installpath + "/" + pack.getDir()
				+ "/instMods/"));
		FileUtils.copyFolder(source, new File(installpath + "/" + pack.getDir()
				+ "/minecraft/"), false);
	}

	/**
	 * "Loads" the settings from the settings class into their respective GUI
	 * controls.
	 */
	private void loadSettings() {
		Settings settings = Settings.getSettings();
		optionsPane.loadSettings(settings);
	}

	/**
	 * "Saves" the settings from the GUI controls into the settings class.
	 */
	public void saveSettings() {
		Settings settings = Settings.getSettings();
		settings.setLastUser((String) users.getSelectedItem());
		instance.optionsPane.saveSettingsInto(settings);
		try {
			settings.save();
		} catch (FileNotFoundException e) {
			Logger.logError("Exception occurred", e);
			ErrorUtils.tossError("Failed to save config file: "
					+ e.getMessage());
		} catch (IOException e) {
			Logger.logError("Exception occurred", e);
			ErrorUtils.tossError("Failed to save config file: "
					+ e.getMessage());
		}
	}

	/**
	 * @param user
	 *            - user added/edited
	 */
	public static void writeUsers(String user) {
		try {
			userManager.write();
		} catch (IOException e) {
			Logger.logError("Exception occurred", e);
		}
		String[] usernames = merge(dropdown_,
				UserManager.getNames().toArray(new String[] {}));
		users.removeAllItems();
		for (int i = 0; i < usernames.length; i++) {
			users.addItem(usernames[i]);
			if (usernames[i].equals(user)) {
				users.setSelectedIndex(i);
			}
		}
	}

	/**
	 * @param A
	 *            - First string array
	 * @param B
	 *            - Second string array
	 * @return - Outputs resulting merged string array from the passed arrays
	 */
	public static String[] merge(String[] A, String[] B) {
		String[] merged = new String[A.length + B.length];
		System.arraycopy(A, 0, merged, 0, A.length);
		System.arraycopy(B, 0, merged, A.length, B.length);
		return merged;
	}

	/**
	 * @return - Outputs selected modpack index
	 */
	public static int getSelectedModIndex() {
		return instance.modPacksPane.getSelectedModIndex();
	}

	/**
	 * @return - Outputs selected texturepack index
	 */
	public static int getSelectedTexturePackIndex() {
		return instance.tpPane.getSelectedTexturePackIndex();
	}

	/**
	 * @return - Outputs LaunchFrame instance
	 */
	public static LaunchFrame getInstance() {
		return instance;
	}

	/**
	 * Enables all items that are disabled upon launching
	 */
	private void enableObjects() {
		tabbedPane.setEnabledAt(0, true);
		tabbedPane.setEnabledAt(1, true);
		tabbedPane.setEnabledAt(2, true);
		// TODO: De momento los paquetes de textura ocultos.
		// tabbedPane.setEnabledAt(3, true);
		// tabbedPane.getSelectedComponent().setEnabled(true);
		updateFooter();
		tpInstall.setEnabled(true);
		launch.setEnabled(true);
		users.setEnabled(true);
		tpInstallLocation.setEnabled(true);
	}

	/**
	 * Install mods
	 */
	private void initializeMods() {
		Logger.logInfo(ModPack.getPack(modPacksPane.getSelectedModIndex())
				.getDir());
		ModManager man = new ModManager(new JFrame(), true);
		man.setVisible(true);
		try {
			installMods(ModPack.getPack(modPacksPane.getSelectedModIndex())
					.getDir());
			ModManager.cleanUp();
		} catch (IOException e) {
			Logger.logError("Exception occurred", e);
		}
	}

	public void hLink(URI uri) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(uri);
			} catch (Exception exc) {
				Logger.logError("Exception occurred durring opening Link", exc);
			}
		} else if (OSUtils.getCurrentOS() == OSUtils.OS.UNIX) {
			File xdg = new File("/usr/bin/xdg-open");
			if (xdg.exists()) {
				ProcessBuilder pb = new ProcessBuilder("/usr/bin/xdg-open",
						uri.toString());
				try {
					pb.start();
				} catch (IOException e) {
				}
			} else {
				Logger.logWarn("Desktop not supported.");
			}
		} else {
			Logger.logWarn("Desktop not supported.");
		}
	}

	public static String getTime() {
		String content = null;
		Scanner scanner = null;
		try {
			URLConnection connection = new URL(
					"http://repo.creeperhost.net/getdate").openConnection();
			scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			content = scanner.next();
		} catch (java.net.UnknownHostException uhe) {
		} catch (Exception ex) {
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return content;
	}

	public void disableMainButtons() {
		launch.setVisible(false);
		edit.setVisible(false);
		users.setVisible(false);
	}

	public void disableTextureButtons() {
		tpInstall.setVisible(true);
	}

	public void updateFooter() {
		boolean result;
		switch (currentPane) {
		case TEXTURE:
			tpInstall.setVisible(true);
			tpInstallLocation.setVisible(true);
			disableMainButtons();
			break;
		default:
			result = ModpacksPane.type.equals("Server");
			launch.setVisible(!result);
			edit.setEnabled(users.getSelectedIndex() > 1);
			edit.setVisible(!result);
			users.setVisible(!result);
			disableTextureButtons();
			break;
		}
	}

	public void updateLocale() {
		if (I18N.currentLocale == Locale.deDE) {
			edit.setBounds(420, 20, 120, 30);
			tpInstallLocation.setBounds(420, 20, 190, 30);
			tpInstall.setBounds(620, 20, 190, 30);

		} else {
			edit.setBounds(480, 20, 60, 30);
			tpInstallLocation.setBounds(480, 20, 160, 30);
			tpInstall.setBounds(650, 20, 160, 30);
		}
		launch.setText(I18N.getLocaleString("LAUNCH_BUTTON"));
		edit.setText(I18N.getLocaleString("EDIT_BUTTON"));
		tpInstall.setText(I18N.getLocaleString("INSTALL_TEXTUREPACK"));
		dropdown_[0] = I18N.getLocaleString("PROFILE_SELECT");
		dropdown_[1] = I18N.getLocaleString("PROFILE_CREATE");
		writeUsers((String) users.getSelectedItem());
		optionsPane.updateLocale();
		modPacksPane.updateLocale();
		tpPane.updateLocale();
	}

	private void updateFolderStructure() {
		File temp = new File(Settings.getSettings().getInstallPath(), ModPack
				.getPack(getSelectedModIndex()).getDir() + "/.minecraft");
		if (temp.exists()) {
			temp.renameTo(new File(Settings.getSettings().getInstallPath(),
					ModPack.getPack(getSelectedModIndex()).getDir()
							+ "/minecraft"));
		}
	}
}

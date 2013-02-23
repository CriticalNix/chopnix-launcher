package com.chopnix.data;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.chopnix.data.events.ModPackListener;
import com.chopnix.workers.ModpackLoader;

import net.ftb.gui.LaunchFrame;

public class ModPack {
	private String name, author, version, url, dir, mcVersion, logoName, info,
			imageName, modFileName;
	private Image logo, image;
	private int index;
	private boolean uptodate = true;

	private final static ArrayList<ModPack> packs = new ArrayList<ModPack>();

	/*
	 * List of Listeners that will be informed if a modpack was added
	 */
	private static List<ModPackListener> listeners = new ArrayList<ModPackListener>();

	/*
	 * Invoking async Load of Modpacks
	 */
	public static void loadAll() {
		ModpackLoader loader = new ModpackLoader();
		loader.start();
	}

	/*
	 * Add a Listener that will be informed if a pack has been added
	 */
	public static void addListener(ModPackListener listener) {
		listeners.add(listener);
	}

	/*
	 * Function to add a Modpack to the Model (used by the ModPackLoader) this
	 * will also inform listeners.
	 */
	public static void addPack(ModPack pack) {
		synchronized (packs) {
			packs.add(pack);
		}
		for (ModPackListener listener : listeners) {
			listener.onModPackAdded(pack);
		}
	}

	public static ArrayList<ModPack> getPackArray() {
		return packs;
	}

	public static ModPack getPack(int i) {
		return packs.get(i);
	}

	public ModPack(String name, String author, String version, String logo,
			String url, String image, String dir, String mcVersion,
			String info, int idx) throws IOException, NoSuchAlgorithmException {
		this.index = idx;
		this.name = name;
		this.author = author;
		this.version = version;
		this.dir = dir;
		this.mcVersion = mcVersion;
		this.url = url;
		this.info = (info == null ? "" : info);
		this.modFileName = getFileName(url);
		this.logoName = logo;
		this.imageName = image;

		String installPath = Settings.getSettings().getInstallPath();
		File tempDir = new File(installPath, "temp" + File.separator + dir);
		File verFile = new File(tempDir, "version");
		String logoFileName = getFileName(logo);
		String imageFileName = getFileName(image);

		URL url_;
		if (!upToDate(verFile)) {
			url_ = new URL(LaunchFrame.getFullLink(logo));
			this.logo = Toolkit.getDefaultToolkit().createImage(url_);
			BufferedImage tempImg = ImageIO.read(url_);
			ImageIO.write(tempImg, "png", new File(tempDir, logoFileName));
			tempImg.flush();
			url_ = new URL(LaunchFrame.getFullLink(image));
			this.image = Toolkit.getDefaultToolkit().createImage(url_);
			tempImg = ImageIO.read(url_);
			ImageIO.write(tempImg, "png", new File(tempDir, imageFileName));
			tempImg.flush();
		} else {
			if (new File(tempDir, logoFileName).exists()) {
				this.logo = Toolkit.getDefaultToolkit().createImage(
						tempDir.getPath() + File.separator + logoFileName);
			} else {
				url_ = new URL(LaunchFrame.getFullLink(logo));
				this.logo = Toolkit.getDefaultToolkit().createImage(url_);
				BufferedImage tempImg = ImageIO.read(url_);
				ImageIO.write(tempImg, "png", new File(tempDir, logoFileName));
				tempImg.flush();
			}

			if (new File(tempDir, imageFileName).exists()) {
				this.image = Toolkit.getDefaultToolkit().createImage(
						tempDir.getPath() + File.separator + imageFileName);
			} else {
				url_ = new URL(LaunchFrame.getFullLink(image));
				this.image = Toolkit.getDefaultToolkit().createImage(url_);
				BufferedImage tempImg = ImageIO.read(url_);
				ImageIO.write(tempImg, "png", new File(tempDir, imageFileName));
				tempImg.flush();
			}
		}
	}

	private String getFileName(String url) {
		String resolved = null;
		if (url != null) {
			Matcher m = Pattern.compile("/(\\w+(\\.\\w+)*\\.(png|gif|zip))")
					.matcher(url);

			if (m.find()) {
				resolved = m.group(1);
			} else {
				resolved = url;
			}
		}
		return resolved;
	}

	private boolean upToDate(File verFile) {
		boolean result = true;
		try {
			if (!verFile.exists()) {
				verFile.getParentFile().mkdirs();
				verFile.createNewFile();
				result = false;
				uptodate = false;
			}
			BufferedReader in = new BufferedReader(new FileReader(verFile));
			String line;
			if ((line = in.readLine()) == null
					|| Integer.parseInt(version) > Integer.parseInt(line)) {
				BufferedWriter out = new BufferedWriter(new FileWriter(verFile));
				out.write(version);
				out.flush();
				out.close();
				result = false;
				uptodate = false;
			}
			in.close();
		} catch (IOException e) {
		}
		return result;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public String getVersion() {
		return version;
	}

	public Image getLogo() {
		return logo;
	}

	public String getUrl() {
		return url;
	}

	public Image getImage() {
		return image;
	}

	public String getDir() {
		return dir;
	}

	public String getMcVersion() {
		return mcVersion;
	}

	public String getInfo() {
		return info;
	}

	public String getLogoName() {
		return logoName;
	}

	public String getImageName() {
		return imageName;
	}

	public String getModFileName() {
		return modFileName;
	}

	public boolean isUpToDate() {
		return uptodate;
	}

	public static ModPack getSelectedPack() {
		// TODO Auto-generated method stub
		return null;
	}
}

package com.chopnix.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Properties;

import com.chopnix.util.OSUtils;
import com.chopnix.util.PathUtils;

import net.ftb.updater.Channel;

public class Settings extends Properties {
	private static final long serialVersionUID = 1L;
	private static Settings settings;
	private File configPath;
	private boolean forceUpdate;

	public static void initSettings() throws IOException {
		File cfgFile = new File(PathUtils.combine(OSUtils.getDefInstallPath(),
				"launcher.cfg"));
		if (cfgFile.exists()) {
			LoadSettings(cfgFile);
			return;
		}
		settings = new Settings();
		settings.setConfigFile(cfgFile);
	}

	public static void LoadSettings(File file) throws FileNotFoundException,
			IOException {
		settings = new Settings(file);
	}

	public static Settings getSettings() {
		return settings;
	}

	public void setNewsDate() {
		setProperty("newsDate",
				Long.toString(Calendar.getInstance().getTime().getTime()));
	}

	public String getNewsDate() {
		return getProperty("newsDate", Long.toString(new Date(0).getTime()));
	}

	public Settings() {
	}

	public Settings(File file) throws IOException {
		configPath = file;
		load(new FileInputStream(file));
	}

	public void save() throws IOException {
		store(new FileOutputStream(configPath), "ChopNix Config File");
	}

	public String getRamMax() {
		return getProperty("ramMax", Integer.toString(1024));
	}

	public void setRamMax(String max) {
		setProperty("ramMax", max);
	}

	public String getLastUser() {
		return getProperty("lastUser", null);
	}

	public void setLastUser(String user) {
		setProperty("lastUser", user);
	}

	public String getInstallPath() {
		return getProperty("installPath", OSUtils.getDefInstallPath());
	}

	public void setInstallPath(String path) {
		setProperty("installPath", path);
	}

	public boolean getForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean force) {
		forceUpdate = force;
	}

	public void setConfigFile(File path) {
		configPath = path;
	}

	public Channel getChannel() {
		return Channel.fromName(getProperty("channel"));
	}

	public void setChannel(Channel channel) {
		setProperty("channel", channel.name());
	}

	public String getLocale() {
		return getProperty("locale", "enUS");
	}

	public void setLocale(String locale) {
		setProperty("locale", locale);
	}

	public File getConfigFile() {
		return configPath;
	}

	public void setConsoleActive(boolean console) {
		setProperty("consoleActive", String.valueOf(console));
	}

	public boolean getConsoleActive() {
		return Boolean.valueOf(getProperty("consoleActive", "true"));
	}

	public boolean isConsoleVisible() {
		return (getProperty("console", "true").equalsIgnoreCase("false") ? false
				: true);
	}

	public boolean isLogging() {
		return (getProperty("logs", "true").equalsIgnoreCase("false") ? false
				: true);
	}
}

package net.ftb.locale;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

import com.chopnix.log.Logger;

/**
 * i18n class, hooray! \o/
 * 
 */
public class I18N {
	private static Properties locales = new Properties();
	private static Properties fallback = new Properties();
	public static HashMap<String, String> localeFiles = new HashMap<String, String>();
	public static HashMap<Integer, String> localeIndices = new HashMap<Integer, String>();
	public static Locale currentLocale = Locale.enUS;

	public enum Locale {
		enUS, deDE, esES
	}

	/**
	 * Set available locales and load fallback locale
	 */
	public static void setupLocale() {
		// TODO: Find a "nicer" way to do this :p
		localeFiles.put("enUS", "English");
		localeIndices.put(0, "enUS");
		localeFiles.put("deDE", "Deutsch");
		localeIndices.put(1, "deDE");
		localeFiles.put("esES", "Español");
		localeIndices.put(2, "esES");
		try {
			fallback.clear();
			fallback.load(I18N.class.getResourceAsStream("/i18n/enUS"));
			Logger.logInfo("[i18n] Fallback enUS loaded");
		} catch (IOException e) {
			Logger.logError("[i18n] Could not load fallback file", e);
		}
	}

	/**
	 * Sets the locale for the launcher
	 * 
	 * @param locale
	 *            the language file to be loaded
	 */
	public static void setLocale(String locale) {
		if (locale.equalsIgnoreCase("deDE")) {
			currentLocale = Locale.deDE;
		} else if (locale.equalsIgnoreCase("esES")) {
			currentLocale = Locale.esES;
		} else {
			currentLocale = Locale.enUS;
		}
		try {
			locales.clear();
			locales.load(new InputStreamReader(I18N.class.getResource(
					"/i18n/" + locale).openStream(), "UTF8"));
			Logger.logInfo("[i18n] " + locale + " "
					+ locales.getProperty("LOCALE_LOADED", "loaded"));
		} catch (IOException e) {
			Logger.logError("[i18n] Could not load locale file", e);
		}
	}

	/**
	 * Gets the default translation for the key (enUS)
	 * 
	 * @param key
	 *            The key for the string
	 * @return the default string
	 */
	private static String getFallbackString(String key) {
		return fallback.getProperty(key, key);
	}

	/**
	 * Gets the localized string for the field, if not defined, returns the key
	 * 
	 * @param key
	 *            The key for the string
	 * @return The localized string or fallback value
	 */
	public static String getLocaleString(String key) {
		return locales.getProperty(key, getFallbackString(key));
	}
}

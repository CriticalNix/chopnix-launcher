package net.ftb.mclauncher;

import ircclient.Main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.chopnix.data.Settings;
import com.chopnix.log.Logger;
import com.chopnix.util.OSUtils;

//import mineshafter.proxy.MineProxy;
import net.ftb.gui.LauncherConsole;

/**
 * 
 *  Support Class for starting Minecraft with custom Memory options
 *
 */
public class MinecraftLauncher {
	
	private static String basepath;

	public static int launchMinecraft(String workingDir, String username, String password, String forgename, String rmax) {
		int success = -1;
		try {
			String[] jarFiles = new String[] {"minecraft.jar", "lwjgl.jar", "lwjgl_util.jar", "jinput.jar" };
			StringBuilder cpb = new StringBuilder("");
			File tempDir = new File(new File(workingDir).getParentFile(), "/instMods/");

			if(tempDir.isDirectory()) {
				for(String name : tempDir.list()) {
					if(name.toLowerCase().contains("forge") && name.toLowerCase().endsWith(".zip")) {
						if(!name.toLowerCase().equalsIgnoreCase(forgename)) {
							if(new File(tempDir, forgename).exists()) {
								new File(tempDir, name).delete();
							} else {
								new File(tempDir, name).renameTo(new File(tempDir, forgename));
							}
						}
					}
					if(!name.equalsIgnoreCase(forgename)) {
						if(name.toLowerCase().endsWith(".zip") || name.toLowerCase().endsWith(".jar")) {
							cpb.append(OSUtils.getJavaDelimiter());
							cpb.append(new File(tempDir, name).getAbsolutePath());
						}
					}
				}
			} else {
				Logger.logInfo("Not a directory.");
			}

			cpb.append(OSUtils.getJavaDelimiter());
			cpb.append(new File(tempDir, forgename).getAbsolutePath());

			for(String jarFile : jarFiles) {
				cpb.append(OSUtils.getJavaDelimiter());
				cpb.append(new File(new File(workingDir, "bin"), jarFile).getAbsolutePath());
			}

			List<String> arguments = new ArrayList<String>();

			String separator = System.getProperty("file.separator");
			String path = System.getProperty("java.home") + separator + "bin" + separator + "java";
			arguments.add(path);

			setMemory(arguments, rmax);

			arguments.add("-cp");
			arguments.add(System.getProperty("java.class.path") + cpb.toString());

			arguments.add(MinecraftLauncher.class.getCanonicalName());
			arguments.add(workingDir);
			arguments.add(forgename);
			arguments.add(username);
			arguments.add(password);

			ProcessBuilder processBuilder = new ProcessBuilder(arguments);
			try {
				processBuilder.start();
				success = 1;
			} catch (IOException e) { Logger.logError("Error during Minecraft launch", e); }
		} catch (Exception e) { Logger.logError("Exception during launch of Minecraft", e);	}
		return success;
	}

	private static void setMemory(List<String> arguments, String rmax) {
		boolean memorySet = false;
		try {
			int min = 256;
			if (rmax != null && Integer.parseInt(rmax) > 0) {
				arguments.add("-Xms" + min + "M");
				Logger.logInfo("Setting MinMemory to " + min);
				arguments.add("-Xmx" + rmax + "M");
				Logger.logInfo("Setting MaxMemory to " + rmax);
				memorySet = true;
			}
		} catch (Exception e) {
			Logger.logError("Error parsing memory settings", e);
		}
		if (!memorySet) {
			arguments.add("-Xms" + 256 + "M");
			Logger.logInfo("Defaulting MinMemory to " + 256);
			arguments.add("-Xmx" + 1024 + "M");
			Logger.logInfo("Defaulting MaxMemory to " + 1024);
		}
	}


	public static void main(String[] args) {
		basepath = args[0];
		String forgename = args[1];
		String username = args[2];
		String password = args[3];

		try {
			Color baseColor = new Color(40, 40, 40);
			UIManager.put("control", baseColor);
			UIManager.put("text", new Color(222, 222, 222));
			UIManager.put("nimbusBase", new Color(0, 0, 0));
			UIManager.put("nimbusFocus", baseColor);
			UIManager.put("nimbusBorder", baseColor);
			UIManager.put("nimbusLightBackground", baseColor);
			UIManager.put("info", new Color(55, 55, 55));
			try {
				for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (Exception e) {
				try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				} catch (ClassNotFoundException e1) { Logger.logWarn("Exception occurred",e1); 
				} catch (InstantiationException e1) { Logger.logWarn("Exception occurred",e1); 
				} catch (IllegalAccessException e1) { Logger.logWarn("Exception occurred",e1); 
				} catch (UnsupportedLookAndFeelException e1) { Logger.logWarn("Exception occurred",e1); }
			}
			
			try {
				Settings.initSettings();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (Settings.getSettings().isConsoleVisible()) {
				LauncherConsole con = new LauncherConsole();
				Main.main(new String[0]);
				con.setVisible(true);
			}
			
			System.out.println("Loading jars...");
			String[] jarFiles = new String[] {"minecraft.jar", "lwjgl.jar", "lwjgl_util.jar", "jinput.jar" };
			HashMap<Integer, File> map = new HashMap<Integer, File>();
			int counter = 0;
			File tempDir = new File(new File(basepath).getParentFile(), "/instMods/");
			if(tempDir.isDirectory()) {
				for(String name : tempDir.list()) {
					if(!name.equalsIgnoreCase(forgename)) {
						if(name.toLowerCase().endsWith(".zip") || name.toLowerCase().endsWith(".jar")) {
							map.put(counter, new File(tempDir, name));
							counter++;
						}
					}
				}
			}

			map.put(counter, new File(tempDir, forgename));
			counter++;
			for(String jarFile : jarFiles) {
				map.put(counter, new File(new File(basepath, "bin"), jarFile));
				counter++;
			}	

			URL[] urls = new URL[map.size()];
			for(int i = 0; i < counter; i++) {
				try {
					urls[i] = map.get(i).toURI().toURL();
				} catch (MalformedURLException e) { e.printStackTrace(); }
				System.out.println("Loading URL: " + urls[i].toString());
			}

			System.out.println("Loading natives...");
			String nativesDir = new File(new File(basepath, "bin"), "natives")
			.toString();

			System.setProperty("org.lwjgl.librarypath", nativesDir);
			System.setProperty("net.java.games.input.librarypath", nativesDir);

			System.setProperty("user.home", new File(basepath).getParent());

			
			
			// FIXME: Mineproxy Start
		/*	Logger.logInfo("Starting Mineproxy");
			MineProxy proxy = new MineProxy();
			proxy.start();
			System.setProperty("http.proxyHost", "127.0.0.1");
			System.setProperty("http.proxyPort", Integer.toString(proxy.getPort()));
			System.setProperty("java.net.preferIPv4Stack", "true");
			*/
			@SuppressWarnings("resource")
			URLClassLoader cl = new URLClassLoader(urls, MinecraftLauncher.class.getClassLoader());

			Class<?> mc = cl.loadClass("net.minecraft.client.Minecraft");

			Field[] fields = mc.getDeclaredFields();

			for (Field f : fields) {
				if (f.getType() != File.class) {
					continue;
				}
				if (0 == (f.getModifiers() & (Modifier.PRIVATE | Modifier.STATIC))) {
					continue;
				}
				f.setAccessible(true);
				f.set(null, new File(basepath));
				System.out.println("Fixed Minecraft Path: Field was " + f.toString());
				break;
			}

			String[] mcArgs = new String[2];
			mcArgs[0] = username;
			mcArgs[1] = password;

			String mcDir = mc.getMethod("a", String.class).invoke(null, (Object) "minecraft").toString();

			System.out.println("MCDIR: " + mcDir);

			mc.getMethod("main", String[].class).invoke(null, (Object) mcArgs);
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (IllegalArgumentException e) { e.printStackTrace();
		} catch (IllegalAccessException e) { e.printStackTrace();
		} catch (InvocationTargetException e) { e.printStackTrace();
		} catch (NoSuchMethodException e) { e.printStackTrace();
		} catch (SecurityException e) { e.printStackTrace(); }
		
		//Onclose
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				
				try {
					Settings.initSettings();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				File root = new File(".");
				
				copyLogs(root, Logger.getInstance().getLogFolder());
				
				File mcFolder = new File(MinecraftLauncher.basepath);
				if (mcFolder.exists()) {
					copyLogs(mcFolder, Logger.getInstance().getLogFolder());
				}
			}
			
			private void copyLogs(File source, File dest) {
				for (File f : source.listFiles()) {
					if (f.getName().endsWith(".log")) {
						try {
							if (Settings.getSettings().isLogging()) {
								f.renameTo(new File(dest, f.getName()));
							}
							else {
								f.delete();
							}
						} catch (Exception e) {
						}
					}
				}
			}
		});
		
	}
}
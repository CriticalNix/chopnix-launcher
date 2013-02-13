package ru;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.ftb.log.Logger;

public class LoginManager {
	
	private static float PROXY_VERSION = 3.7F;
	
	public static String login(String user, String password) throws UnsupportedEncodingException {		  
		  String targetURL = "http://mineshafter.appspot.com/game/getversion.jsp?proxy=" + PROXY_VERSION;
		  String urlParameters =  "user=" + URLEncoder.encode(user, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + 13;
		  
		  HttpURLConnection localHttpURLConnection = null;

			try {
				byte[] arrayOfByte1 = urlParameters.getBytes();
				URL localObject1 = new URL(targetURL);

				localHttpURLConnection = (HttpURLConnection) ((URL) localObject1).openConnection();
				localHttpURLConnection.setRequestMethod("POST");
				localHttpURLConnection.setDoOutput(true);
				localHttpURLConnection.setDoInput(true);

				localHttpURLConnection.setRequestProperty("Host",localObject1.getHost());
				localHttpURLConnection.setRequestProperty("Content-Length",Integer.toString(arrayOfByte1.length));
				localHttpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

				BufferedOutputStream localObject2 = new BufferedOutputStream(localHttpURLConnection.getOutputStream());
				localObject2.write(arrayOfByte1);
				localObject2.flush();
				localObject2.close();

				InputStream localObject3 = localHttpURLConnection.getInputStream();
				ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
				byte[] arrayOfByte2 = new byte[4096];
				int read;
				while (true) {
					try {
						read = localObject3.read(arrayOfByte2);
						if (read == -1)
							break;
					} catch (IOException localIOException) {
						break;
					}
					localByteArrayOutputStream.write(arrayOfByte2, 0, read);
				}
				localByteArrayOutputStream.flush();
				return new String(localByteArrayOutputStream.toByteArray());
			}
			catch (FileNotFoundException localFileNotFoundException) {
				Logger.logWarn("executePost failed: server sent 404.");
			}
			catch (Exception localException) {
				localException.printStackTrace();
				Logger.logWarn("executePost failed.");
			}

			return null;
	}
}

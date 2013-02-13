package mineshafter.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;

public class MineProxyHandler extends Thread {
	private DataInputStream fromClient;
	private DataOutputStream toClient;
	private Socket connection;

	private MineProxy proxy;

	public MineProxyHandler(MineProxy proxy, Socket conn) throws IOException {
		setName("MineProxyHandler Thread");

		this.proxy = proxy;

		connection = conn;
		fromClient = new DataInputStream(conn.getInputStream());
		toClient = new DataOutputStream(conn.getOutputStream());
	}

	public void run() {
		HashMap<String, String> headers = new HashMap<String, String>();

		// Read the incoming request
	    String[] requestLine = readUntil(fromClient, '\n').split(" ");
	    String method = requestLine[0].trim().toUpperCase();
	    String url = requestLine[1].trim();

	 	MineProxy.log("[MineProxy ]Request: " + method + " " + url);

		// Read the incoming headers
		// System.out.println("Headers:");

	 	String header;
		do {
			header = readUntil(fromClient, '\n').trim();
			// System.out.println("H: " + header + ", " + header.length());
			int splitPoint = header.indexOf(':');
			if (splitPoint != -1) {
				headers.put(header.substring(0, splitPoint).toLowerCase()
						.trim(), header.substring(splitPoint + 1).trim());
			}

		} while (header.length() > 0);

		// run matchers
		Matcher skinMatcher = MineProxy.SKIN_URL.matcher(url);
		Matcher cloakMatcher = MineProxy.CLOAK_URL.matcher(url);
		Matcher getversionMatcher = MineProxy.GETVERSION_URL.matcher(url);
		Matcher joinserverMatcher = MineProxy.JOINSERVER_URL.matcher(url);
		Matcher checkserverMatcher = MineProxy.CHECKSERVER_URL.matcher(url);
		
		
		Matcher snoopMCMatcher = MineProxy.SNOOP_MC.matcher(url);

		byte[] data = null;
		String contentType = null;
		String params;

		// If Skin Request
		if(skinMatcher.matches()) {
			MineProxy.log("Skin");

			String username = skinMatcher.group(1);
			if(proxy.skinCache.containsKey(username)) { // Is the skin in the cache?
				MineProxy.log("Skin from cache");

				data = proxy.skinCache.get(username);  // Then get it from there
			} else {
				url = "http://" + MineProxy.authServer + "/skin/" + username + ".png";
				//url = "http://" + MineProxy.authServer + "/game/getskin.php?name=" + username;
				//url = "http://" + MineProxy.authServer + "/game/getskin/" + username;

				MineProxy.log("To: " + url);

				data = getRequest(url); // Then get it...
				MineProxy.log("Response length: " + data.length);

				proxy.skinCache.put(username, data); // And put it in there
			}

		} 
		// If Cloak Request
		else if(cloakMatcher.matches()) {
			MineProxy.log("Cloak");

			String username = cloakMatcher.group(1);
			if(proxy.cloakCache.containsKey(username)) {
				MineProxy.log("Cloak from cache");
				data = proxy.cloakCache.get(username);
			} else {
				//url = "http://" + MineProxy.authServer + "/cloak/get.jsp?user=" + username;
				//url = "http://" + MineProxy.authServer + "/game/getcloak.php?user=" + username;
				url = "http://" + MineProxy.authServer + "/game/getcloak/" + username;

				MineProxy.log("To: " + url);

				data = getRequest(url);
				MineProxy.log("Response length: " + data.length);

				proxy.cloakCache.put(username, data);
			}

		} 
		// If Version Request
		else if(getversionMatcher.matches()) {
			MineProxy.log("GetVersion");

			url = "http://" + MineProxy.authServer + "/game/getversion.jsp?proxy=" + MineProxy.VERSION_MINESHAFFTER;
			//url = "http://" + MineProxy.authServer + "/game/getversion/" + proxy.version;
			MineProxy.log("To: " + url);

			try {
				int postlen = Integer.parseInt(headers.get("content-length"));
				char[] postdata = new char[postlen];
				InputStreamReader reader = new InputStreamReader(fromClient);
				reader.read(postdata);

				data = postRequest(url, new String(postdata), "application/x-www-form-urlencoded");

			} catch(IOException e) {
				MineProxy.log("Unable to read POST data from getversion request");

				e.printStackTrace();
			}

		} 
		// If JoinServer Request
		else if(joinserverMatcher.matches()) {
			MineProxy.log("JoinServer");

			params = joinserverMatcher.group(1);
			//url = "http://" + MineProxy.authServer + "/game/joinserver.php" + params;
			url = "http://" + MineProxy.authServer + "/game/joinserver" + params;
			MineProxy.log("To: " + url);
			data = getRequest(url);
			contentType = "text/plain";
		}
		// If Check Server Request
		else if(checkserverMatcher.matches()) {
			MineProxy.log("CheckServer");

			params = checkserverMatcher.group(1);
			//url = "http://" + MineProxy.authServer + "/game/checkserver.php" + params;
			url = "http://" + MineProxy.authServer + "/game/checkserver" + params;
			MineProxy.log("To: " + url);
			data = getRequest(url);

		}
		else if (snoopMCMatcher.matches()) {
			MineProxy.log("Snooping blocked :)");
		}
		else {
			data = getRequest(url);
		}

		try {
			if (data != null) {
				toClient.writeBytes("HTTP/1.0 200 OK\r\nConnection: close\r\nProxy-Connection: close\r\nContent-Length: "
						+ data.length + "\r\n");
				if (contentType != null) {
					toClient.writeBytes("Content-Type: " + contentType + "\r\n");
				}

				toClient.writeBytes("\r\n");
				try {
					toClient.write(data);
				} catch (Exception e) {
					
				}
				toClient.flush();
			}
			this.toClient.close();
			this.connection.close();
			fromClient.close();
			toClient.close();
			connection.close();
			//System.out.println(data.length);
			//System.out.println(new String(data));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] getRequest(String url) {
		return getRequest(url, null);
	}

	public static byte[] getRequest(String url, String method) {
		try {
			HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection(Proxy.NO_PROXY);
			c.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");
			c.setConnectTimeout(5000);
			c.setReadTimeout(5000);
			c.setInstanceFollowRedirects(true);
			c.setUseCaches(false);

			if (method != null) {
				c.setRequestMethod(method);
			}

			int code = c.getResponseCode();
			MineProxy.log("Response: " + code);
			if(code / 100 == 4) {
				return new byte[0];
			}
			
			BufferedInputStream in = new BufferedInputStream(c.getInputStream());

			return grabData(in);

		} catch (MalformedURLException e) {
			MineProxy.log("Bad URL in getRequest:");
			//e.printStackTrace();
		} catch (IOException e) {
			MineProxy.log("IO error during a getRequest:");
			//e.printStackTrace();
		}

		return new byte[0];
	}

	public static byte[] postRequest(String url, String postdata, String contentType) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(out);
		//System.out.println("Postdata: " + postdata);

		try {
			writer.write(postdata);
			writer.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}

		byte[] rd = postRequest(url, out.toByteArray(), contentType);

		return rd;
	}

	public static byte[] postRequest(String url, byte[] postdata, String contentType) {
		try {
			URL u = new URL(url);

			HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
			c.setDoOutput(true);
			c.setRequestMethod("POST");

			//System.out.println("Postdata_bytes: " + new String(postdata));

			c.setRequestProperty("Host", u.getHost());
			c.setRequestProperty("Content-Length", Integer.toString(postdata.length));
			c.setRequestProperty("Content-Type", contentType);

			BufferedOutputStream out = new BufferedOutputStream(c.getOutputStream());
			out.write(postdata);
			out.flush();
			out.close();

			byte[] data = grabData(new BufferedInputStream(c.getInputStream()));
			return data;

		} catch(java.net.UnknownHostException e) {
			MineProxy.log("Unable to resolve remote host, returning null");
		} catch (MalformedURLException e) {
			MineProxy.log("Bad URL when doing postRequest:");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] grabData(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		while(true) {
			int len;
			try {
				len = in.read(buffer);
				if(len == -1) break;
			} catch(IOException e) {
				break;
			}
			out.write(buffer, 0, len);
		}

		return out.toByteArray();
	}


	public static String readUntil(DataInputStream is, String endSequence) {
		return readUntil(is, endSequence.getBytes());
	}

	public static String readUntil(DataInputStream is, char endSequence) {
		return readUntil(is, new byte[] { (byte) endSequence });
	}

	public static String readUntil(DataInputStream is, byte endSequence) {
		return readUntil(is, new byte[] { endSequence });
	}

	public static String readUntil(DataInputStream is, byte[] endSequence) { // If there is an edge case, make sure we can see it
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String r = null;

		try {
			int i = 0;

			while (true) {
				boolean end = false;
				byte b = is.readByte(); // Read a byte
				if (b == endSequence[i]) { // If equal to current byte of endSequence
					if (i == endSequence.length - 1) {
						end = true; // If we hit the end of endSequence, we're done
					}

					i++; // Increment for next round
				} else {
					i = 0; // Reset
				}

				out.write(b);
				if (end)
					break;
			}
		} catch (IOException ex) {
			MineProxy.log("readUntil unable to read from InputStream, endSeq: " + new String(endSequence));
			ex.printStackTrace();
		}

		try {
			r = out.toString("UTF-8");
		} catch (java.io.UnsupportedEncodingException ex) {
			MineProxy.log("readUntil unable to encode data: " + out.toString());
			ex.printStackTrace();
		}

		return r;
	}
}
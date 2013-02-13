package mineshafter.proxy;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.regex.Pattern;

import net.ftb.log.Logger;

public class MineProxy extends Thread
{
  public static String authServer = "mineshafter.appspot.com";
  
  public static Pattern SKIN_URL = Pattern.compile("http://skins\\.minecraft\\.net/MinecraftSkins/(.+?)\\.png");
  public static Pattern CLOAK_URL = Pattern.compile("http://skins\\.minecraft\\.net/MinecraftCloaks/(.+?)\\.png");
  public static Pattern GETVERSION_URL = Pattern.compile("http://session\\.minecraft\\.net/game/getversion\\.jsp");
  public static Pattern JOINSERVER_URL = Pattern.compile("http://session\\.minecraft\\.net/game/joinserver\\.jsp(.*)");
  public static Pattern CHECKSERVER_URL = Pattern.compile("http://session\\.minecraft\\.net/game/checkserver\\.jsp(.*)");
  public static Pattern SNOOP_MC = Pattern.compile("http://snoop\\.minecraft\\.net/client.*");
  
  
  private int port = -1;
  public Hashtable<String, byte[]> skinCache;
  public Hashtable<String, byte[]> cloakCache;

  public  static final float VERSION_MINESHAFFTER = 3.7F;
  
  public static final boolean DEBUG = false;
  
  public static void log(String message) {
	  if (DEBUG) {
		  Logger.logInfo(message);
	  }
  }

  public MineProxy()
  {
    setName("MineProxy Thread");
    try
    {

      this.skinCache = new Hashtable<String, byte[]> ();
      this.cloakCache = new Hashtable<String, byte[]> ();
    } catch (Exception e) {
      System.out.println("Proxy starting error:");
      e.printStackTrace();
    }
  }

  @SuppressWarnings("resource")
public void run() {
    try {
      ServerSocket server = null;
      int port = 9010;
      while (port < 12000) {
        try {
          System.out.println("Trying to proxy on port " + port);
          byte[] loopback = { 127, 0, 0, 1 };
          server = new ServerSocket(port, 16, InetAddress.getByAddress(loopback));
          this.port = port;
          System.out.println("Proxying successful");
          break;
        }
        catch (BindException ex) {
          port++;
        }
      }
      while (true)
      {
        Socket connection = server.accept();

        MineProxyHandler handler = new MineProxyHandler(this, connection);
        handler.start();
      }
    } catch (IOException e) {
      System.out.println("Error in server accept loop:");
      e.printStackTrace();
    }
  }

  public int getPort() {
    while (this.port < 0) {
      try {
        sleep(50L);
      } catch (InterruptedException e) {
        System.out.println("Interrupted while waiting for port");
        e.printStackTrace();
      }
    }
    return this.port;
  }
}
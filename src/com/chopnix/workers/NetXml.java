package com.chopnix.workers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

 
public class NetXml {
 
	public static void main(String argv[]) throws IOException {
 
	  Properties p = new Properties();

	    p.setProperty("name", ("chopnix.com"));
	    p.setProperty("server", ("irc.chopnix.com"));
	    p.setProperty("port", ("6667"));
	    p.setProperty("nick", (""));
	    p.setProperty("nickPass", (""));
	    p.setProperty("channels", ("#chopnixserver"));
	    p.setProperty("autoConnect", ("false"));
	    p.setProperty("log", ("false"));
	    p.setProperty("ssl", ("false"));

	    try {
	        File f = new File("networks/.xml");

	        if (!f.exists() && new File("networks/" + "/").mkdir()
	                && new File("networks/" + "/log").mkdir()) {
	            f.createNewFile();
	        }

	        FileOutputStream fos = new FileOutputStream("networks/" + ".xml");
	        p.storeToXML(fos, ("chopnix"));
	        fos.close();
	    } catch (FileNotFoundException fnfe) {
	    } catch (IOException ioe) {
	    }
}
}
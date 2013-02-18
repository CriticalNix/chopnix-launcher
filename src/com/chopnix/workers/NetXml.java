package com.chopnix.workers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

 
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
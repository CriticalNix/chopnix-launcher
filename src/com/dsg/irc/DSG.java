package com.dsg.irc;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.Random;

import javax.swing.JOptionPane;


public class DSG {
	public static String IRC_Channel = "";
	public static String IRC_name = "";
	public static boolean IRC_connected = false;
	public static boolean ConnectToChopnix = false;
	
	
	public static String ColorFilter(String SendText) {
		try{
			char ColorChar = 3; //ColorSuport for server
			char replaceCC = 59; //;
			
			SendText = SendText.replace(replaceCC, ColorChar);
		}catch (Exception ex){
			System.out.print("DSG decoder has failed");
		}
		return SendText;
	}
 	
	public static int GetScreenSizeX(Window window){
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		 
		int w = window.getSize().width;
		int x = (dim.width-w);
		return x;
	}
	
	public static int GetScreenSizeY(Window window){
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		 
		int h = window.getSize().height;
		int y = (dim.height-h);
		return y;
	}
	
	public static String nullNick(){
		String nick = JOptionPane.showInputDialog(null , "Please enter your nickname");
		Random r = null;
		
		if(nick == null){nick = "ChopnixLauncher_" + r.nextInt();}
		return nick;
	}
	
}


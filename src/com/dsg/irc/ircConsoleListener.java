package com.dsg.irc;

import ircclient.irc.ServerConnection;

public class ircConsoleListener {

	
	public static void ChatListener(String chatline, ServerConnection serv){
		
		if(chatline.contains(DSG.IRC_name + " MODE " + DSG.IRC_name)){
			try{
				serv.getServerPanel().joinChan("#chopnixserver");
				frmColors.main(null);
			}catch (Exception iox){
				System.out.println("Failed to join channel");
			}
		}
	}
	
	
	
	
}

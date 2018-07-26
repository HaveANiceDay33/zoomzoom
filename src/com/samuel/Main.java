package com.samuel;

import com.osreboot.hvol.base.HvlGameInfo;
import com.samuel.client.MainClient;
import com.samuel.server.MainServer;

public class Main{
	public static String 
	INFO_GAME = "Zoom Zoom", 
	INFO_VERSION = "0.1";
	
	public static final int INFO_PORT = 25565;
	
	public static void main(String [] args){
		//new MainClient(new HvlGameInfo(INFO_GAME, INFO_VERSION, INFO_PORT));
		new MainServer(new HvlGameInfo(INFO_GAME, INFO_VERSION, INFO_PORT));
	}
}

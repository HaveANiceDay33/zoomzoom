package com.samuel;

import com.osreboot.hvol.base.HvlKey;

public class KC {

	public static HvlKey key_PlayerUsername(HvlKey key){
		return key.with("username");
	}
	
	public static HvlKey key_PlayerLobbyInfo(HvlKey key){
		return key.with("lobbyinfo");
	}
	
	public static HvlKey key_PlayerGameInfo(HvlKey key){
		return key.with("gameinfo");
	}
	
	public static HvlKey key_PlayerListIndex(HvlKey key){
		return key.with("listindex");
	}
	
	public static HvlKey key_GameUsernameList(){
		return new HvlKey("game").with("usernamelist");
	}
	
	public static HvlKey key_GameLobbyInfoList(){
		return new HvlKey("game").with("lobbyinfolist");
	}
	
	public static HvlKey key_GameGameInfoList(){
		return new HvlKey("game").with("gameinfolist");
	}
	
	public static HvlKey key_GameReadyTimer(){
		return new HvlKey("game").with("readytimer");
	}
	
	public static HvlKey key_GameState(){
		return new HvlKey("game").with("state");
	}
	
	public static HvlKey key_GameMap(){
		return new HvlKey("game").with("map");
	}
	
}

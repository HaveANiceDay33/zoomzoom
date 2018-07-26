package com.samuel.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.newdawn.slick.Color;

import com.osreboot.hvol.base.HvlGameInfo;
import com.osreboot.hvol.base.HvlMetaServer.SocketWrapper;
import com.osreboot.hvol.dgameserver.HvlTemplateDGameServer2D;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.display.collection.HvlDisplayModeResizable;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.samuel.GameState;
import com.samuel.InfoLobby;
import com.samuel.KC;

public class MainServer extends HvlTemplateDGameServer2D{

	public final static int FONT_INDEX = 0;
	
	public MainServer(HvlGameInfo gameInfoArg){
		super(144, 512, 512, "Zoom zoom Server", new HvlDisplayModeResizable(),"localhost" , 25565, .05f, gameInfoArg);
	}
	
	public static HvlFontPainter2D font;
	
	private LinkedHashMap<SocketWrapper, String> usernames;
	private LinkedHashMap<SocketWrapper, InfoLobby> lobbyInfo;
	
	private GameState state;
	private float readyTimer;

	@Override
	public void initialize(){
		getTextureLoader().loadResource("osfont");
		
		font =  new HvlFontPainter2D(getTexture(FONT_INDEX), HvlFontPainter2D.Preset.FP_INOFFICIAL, 0.125f, 8f, 0);
	
		usernames = new LinkedHashMap<>();
		lobbyInfo = new LinkedHashMap<>();
		
		state = GameState.LOBBY;
		readyTimer = 1f;
	}

	@Override
	public void update(float delta){
		font.drawWord(getServer().getTable().toString(), 0,  0, Color.white);
		
		sendListUpdates();
		
		if(usernames.size() == lobbyInfo.size() && lobbyInfo.size() == getAuthenticatedUsers().size()){
			int valid = 0;
			for(SocketWrapper s : lobbyInfo.keySet()){
				if(lobbyInfo.get(s).ready) valid++;
			}
			if(valid == usernames.size() && valid > 1f){
				readyTimer = HvlMath.stepTowards(readyTimer, delta/5f, 0f);
				if(readyTimer == 0) state = GameState.RUNNING;
			}else{
				readyTimer = 1f;
			}
		}
		
		getServer().setValue(KC.key_GameState(), state, false);
		getServer().setValue(KC.key_GameReadyTimer(), readyTimer, false);
	}
	
	@Override
	public void onConnection(SocketWrapper target){
		getServer().addMember(target, KC.key_GameUsernameList());
		getServer().addMember(target, KC.key_GameLobbyInfoList());
		getServer().addMember(target, KC.key_GameReadyTimer());
		getServer().addMember(target, KC.key_GameState());
	}

	@Override
	public void onDisconnection(SocketWrapper target){
		usernames.remove(target);
		lobbyInfo.remove(target);
	}
	
	private void sendListUpdates(){
		usernames.clear();
		lobbyInfo.clear();
		for(SocketWrapper s : getAuthenticatedUsers()){
			if(getServer().hasValue(KC.key_PlayerUsername(getUIDK(s)))){
				usernames.put(s, getServer().<String>getValue(KC.key_PlayerUsername(getUIDK(s))));
				if(getServer().hasValue(KC.key_PlayerLobbyInfo(getUIDK(s)))){
					lobbyInfo.put(s, getServer().<InfoLobby>getValue(KC.key_PlayerLobbyInfo(getUIDK(s))));
				}
			}
		}
		getServer().setValue(KC.key_GameUsernameList(), new ArrayList<>(usernames.values()), false);
		getServer().setValue(KC.key_GameLobbyInfoList(), new ArrayList<>(lobbyInfo.values()), false);
		for(SocketWrapper s : getAuthenticatedUsers()){
			if(getServer().hasValue(KC.key_PlayerUsername(getUIDK(s)))){
				getServer().setValue(KC.key_PlayerListIndex(getUIDK(s)), new ArrayList<>(usernames.keySet()).indexOf(s), false);
			}
		}
	}

}

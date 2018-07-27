package com.samuel.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.newdawn.slick.Color;

import com.osreboot.hvol.base.HvlGameInfo;
import com.osreboot.hvol.base.HvlMetaServer.SocketWrapper;
import com.osreboot.hvol.dgameserver.HvlTemplateDGameServer2D;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.display.collection.HvlDisplayModeResizable;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.samuel.GameState;
import com.samuel.InfoGame;
import com.samuel.InfoLobby;
import com.samuel.KC;
import com.samuel.client.TrackGenerator;

public class MainServer extends HvlTemplateDGameServer2D{

	public final static int FONT_INDEX = 0;

	public MainServer(HvlGameInfo gameInfoArg){
		super(144, 512, 512, "Zoom zoom Server", new HvlDisplayModeResizable(),"localhost" , 25565, .016f, gameInfoArg);
	}

	public static HvlFontPainter2D font;

	private LinkedHashMap<SocketWrapper, String> usernames;
	private LinkedHashMap<SocketWrapper, InfoLobby> lobbyInfo;
	private LinkedHashMap<SocketWrapper, InfoGame> gameInfo;

	private GameState state;
	private float readyTimer;
	private int map;

	@Override
	public void initialize(){
		getTextureLoader().loadResource("osfont");

		font =  new HvlFontPainter2D(getTexture(FONT_INDEX), HvlFontPainter2D.Preset.FP_INOFFICIAL, 0.125f, 8f, 0);

		usernames = new LinkedHashMap<>();
		lobbyInfo = new LinkedHashMap<>();
		gameInfo = new LinkedHashMap<>();

		state = GameState.LOBBY;
		readyTimer = 1f;
		map = 0;
	}

	@Override
	public void update(float delta){
		font.drawWord(getServer().getTable().toString(), 0,  0, Color.white);

		sendLobbyListUpdates();

		if(usernames.size() == 0){
			state = GameState.LOBBY;
		}

		if(state == GameState.LOBBY){
			if(usernames.size() == lobbyInfo.size() && lobbyInfo.size() == getAuthenticatedUsers().size()){
				int valid = 0;
				for(SocketWrapper s : lobbyInfo.keySet()){
					if(lobbyInfo.get(s).ready) valid++;
				}
				if(valid == usernames.size() && valid > 1f){
					readyTimer = HvlMath.stepTowards(readyTimer, delta/5f, 0f);
					if(readyTimer == 0){
						state = GameState.MAP;
						pickNewMap();
						readyTimer = 1f;
						for(SocketWrapper s : lobbyInfo.keySet()){
							gameInfo.put(s, new InfoGame(new HvlCoord2D(TrackGenerator.START_X, TrackGenerator.START_Y), 0f, lobbyInfo.get(s).carTexture, lobbyInfo.get(s).color, usernames.get(s)));
							getServer().setValue(KC.key_PlayerGameInfo(getUIDK(s)), gameInfo.get(s), false);
						}
					}
				}else{
					readyTimer = 1f;
				}
			}
		}else if(state == GameState.RUNNING){
			boolean allFinished = true;
			for(SocketWrapper s : gameInfo.keySet()){
				if(gameInfo.get(s).finishTime == -1f) allFinished = false;
			}
			if(allFinished){
				readyTimer = HvlMath.stepTowards(readyTimer, delta/5f, 0f);
				if(readyTimer == 0){
					state = GameState.LOBBY;
					gameInfo.clear();
					readyTimer = 1f;
				}
			}
		}else if(state == GameState.MAP){
			readyTimer = HvlMath.stepTowards(readyTimer, delta/5f, 0f);
			if(readyTimer == 0){
				state = GameState.RUNNING;
				readyTimer = 1f;
			}
		}

		getServer().setValue(KC.key_GameState(), state, false);
		getServer().setValue(KC.key_GameReadyTimer(), readyTimer, false);
	}

	private void pickNewMap(){
		map = 0;
		getServer().setValue(KC.key_GameMap(), map, false);
	}
	
	@Override
	public void onConnection(SocketWrapper target){
		if(state == GameState.RUNNING) getServer().kick(target);
		
		getServer().addMember(target, KC.key_GameUsernameList());
		getServer().addMember(target, KC.key_GameLobbyInfoList());
		getServer().addMember(target, KC.key_GameReadyTimer());
		getServer().addMember(target, KC.key_GameState());
		getServer().addMember(target, KC.key_GameGameInfoList());
		getServer().addMember(target, KC.key_GameMap());
	}

	@Override
	public void onDisconnection(SocketWrapper target){
		usernames.remove(target);
		lobbyInfo.remove(target);
	}

	private void sendLobbyListUpdates(){
		usernames.clear();
		lobbyInfo.clear();
		gameInfo.clear();
		for(SocketWrapper s : getAuthenticatedUsers()){
			if(getServer().hasValue(KC.key_PlayerUsername(getUIDK(s)))){
				usernames.put(s, getServer().<String>getValue(KC.key_PlayerUsername(getUIDK(s))));
				if(getServer().hasValue(KC.key_PlayerLobbyInfo(getUIDK(s)))){
					lobbyInfo.put(s, getServer().<InfoLobby>getValue(KC.key_PlayerLobbyInfo(getUIDK(s))));
				}
				if(getServer().hasValue(KC.key_PlayerGameInfo(getUIDK(s)))){
					gameInfo.put(s, getServer().<InfoGame>getValue(KC.key_PlayerGameInfo(getUIDK(s))));
				}
			}
		}
		getServer().setValue(KC.key_GameUsernameList(), new ArrayList<>(usernames.values()), false);
		getServer().setValue(KC.key_GameLobbyInfoList(), new ArrayList<>(lobbyInfo.values()), false);
		getServer().setValue(KC.key_GameGameInfoList(), new ArrayList<>(gameInfo.values()), false);
		for(SocketWrapper s : getAuthenticatedUsers()){
			if(getServer().hasValue(KC.key_PlayerUsername(getUIDK(s)))){
				getServer().setValue(KC.key_PlayerListIndex(getUIDK(s)), new ArrayList<>(usernames.keySet()).indexOf(s), false);
			}
		}
	}

}

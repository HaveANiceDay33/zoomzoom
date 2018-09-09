package com.samuel.server;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.hvol.base.HvlGameInfo;
import com.osreboot.hvol.base.HvlMetaServer.SocketWrapper;
import com.osreboot.hvol.dgameserver.HvlTemplateDGameServer2D;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction1;
import com.osreboot.ridhvl.display.collection.HvlDisplayModeResizable;
import com.osreboot.ridhvl.input.HvlInput;
import com.osreboot.ridhvl.menu.HvlComponentDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.HvlButton;
import com.osreboot.ridhvl.menu.component.HvlComponentDrawable;
import com.osreboot.ridhvl.menu.component.HvlSpacer;
import com.osreboot.ridhvl.menu.component.HvlTextBox;
import com.osreboot.ridhvl.menu.component.collection.HvlLabeledButton;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.samuel.GameState;
import com.samuel.InfoGame;
import com.samuel.InfoLobby;
import com.samuel.KC;
import com.samuel.Main;
import com.samuel.client.MenuManager;
import com.samuel.client.TrackGenerator;

public class MainServer extends HvlTemplateDGameServer2D{

	public final static int FONT_INDEX = 0;

	public MainServer(HvlGameInfo gameInfoArg){
		super(144, 512, 512, "Zoom zoom Server", new HvlDisplayModeResizable(),"localhost" , 25565, .016f, gameInfoArg);
	}

	public static HvlFontPainter2D font;

	public static LinkedHashMap<SocketWrapper, String> usernames;
	public static LinkedHashMap<SocketWrapper, InfoLobby> lobbyInfo;
	public static LinkedHashMap<SocketWrapper, InfoGame> gameInfo;

	public static GameState state;
	public static float readyTimer;
	public static int map, nextMap;
	public static boolean debug = false;

	private static HvlInput commandSubmit;

	public static HvlMenu main;

	@Override
	public void initialize(){
		getTextureLoader().loadResource("osfont");

		font =  new HvlFontPainter2D(getTexture(FONT_INDEX), HvlFontPainter2D.Preset.FP_INOFFICIAL, 0.125f, 8f, 0);

		usernames = new LinkedHashMap<>();
		lobbyInfo = new LinkedHashMap<>();
		gameInfo = new LinkedHashMap<>();

		state = GameState.LOBBY;
		readyTimer = 1f;
		map = HvlMath.randomInt(MenuManager.NUM_TRACKS);
		pickNextMap();

		commandSubmit = new HvlInput(new HvlInput.InputFilter(){
			@Override
			public float getCurrentOutput(){
				return Keyboard.isKeyDown(Keyboard.KEY_RETURN) ? 1f : 0f;
			}
		});
		commandSubmit.setPressedAction(new HvlAction1<HvlInput>(){
			@Override
			public void run(HvlInput aArg){
				if(main.getFirstArrangerBox().getFirstOfType(HvlArrangerBox.class).getFirstOfType(HvlTextBox.class).getText() != ""){
					CommandManager.executeCommand(main.getFirstArrangerBox().getFirstOfType(HvlArrangerBox.class).getFirstOfType(HvlTextBox.class).getText());
					main.getFirstArrangerBox().getFirstOfType(HvlArrangerBox.class).getFirstOfType(HvlTextBox.class).setText("");
				}
			}
		});

		CommandManager.initialize();

		HvlTextBox defaultTextBox = new HvlTextBox(512, 32, "", new HvlComponentDrawable(){
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg){
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Color.gray);
			}
		}, new HvlComponentDrawable(){
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg){
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Color.darkGray);
			}
		}, font);
		defaultTextBox.setOffsetX(4f);
		defaultTextBox.setOffsetY(6f);
		HvlComponentDefault.setDefault(defaultTextBox);

		HvlComponentDefault.setDefault(HvlLabeledButton.class, new HvlLabeledButton.Builder().setWidth(64).setHeight(32).setFont(font).setTextColor(Color.white).setOnDrawable(new HvlComponentDrawable() {
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height,Color.lightGray);
			}
		}).setOffDrawable(new HvlComponentDrawable() {
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height,Color.darkGray);	
			}
		}).setHoverDrawable(new HvlComponentDrawable() {
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height,Color.gray);
			}
		}).build());

		main = new HvlMenu();

		main.add(new HvlArrangerBox(Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL));
		main.getFirstArrangerBox().add(new HvlSpacer(0, Display.getHeight() - 48));
		main.getFirstArrangerBox().add(new HvlArrangerBox(Display.getWidth(), 48, ArrangementStyle.HORIZONTAL));
		main.getFirstArrangerBox().getFirstOfType(HvlArrangerBox.class).add(new HvlTextBox.Builder().setBlacklistCharacters("[\r\n]").setText("").build());
		main.getFirstArrangerBox().getFirstOfType(HvlArrangerBox.class).add(new HvlSpacer(8, 0));
		main.getFirstArrangerBox().getFirstOfType(HvlArrangerBox.class).add(new HvlLabeledButton.Builder().setText("run").setClickedCommand(new HvlAction1<HvlButton>(){
			@Override
			public void run(HvlButton aArg){
				commandSubmit.getPressedAction().run(null);
			}
		}).build());

		HvlMenu.setCurrent(main);
	}

	@Override
	public void update(float delta){
		main.getFirstArrangerBox().setDimensions(Display.getWidth(), Display.getHeight());
		main.getFirstArrangerBox().getFirstOfType(HvlSpacer.class).setHeight(Display.getHeight() - 48);
		main.getFirstArrangerBox().getFirstOfType(HvlArrangerBox.class).setWidth(Display.getWidth());
		main.getFirstArrangerBox().getFirstOfType(HvlArrangerBox.class).getFirstOfType(HvlTextBox.class).setWidth(Display.getWidth() - 64 - 24);

		HvlMenu.updateMenus(delta);

		font.drawWord(getServer().getTable().toString(), 8,  8, Color.white);
		font.drawWord("Version: " + Main.INFO_VERSION, Display.getWidth() - font.getLineWidth("Version: " + Main.INFO_VERSION) - 8, 8, Color.white);
		sendLobbyListUpdates();
		
		CommandManager.update(delta);

		if(usernames.size() == 0){
			state = GameState.LOBBY;
			map = HvlMath.randomInt(MenuManager.NUM_TRACKS);
		}

		if(state == GameState.LOBBY){
			if(usernames.size() == lobbyInfo.size() && lobbyInfo.size() == getAuthenticatedUsers().size()){
				int valid = 0;
				for(SocketWrapper s : lobbyInfo.keySet()){
					if(lobbyInfo.get(s).ready) valid++;
				}
				if(valid == usernames.size() && valid >= (debug ? 1 : 2)){
					readyTimer = HvlMath.stepTowards(readyTimer, delta/5f, 0f);
					if(readyTimer == 0){
						state = GameState.MAP;
						
						map = nextMap;
						getServer().setValue(KC.key_GameMap(), map, false);
						pickNextMap();
						
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

	private void pickNextMap(){
		int oldMap = map;
		while(oldMap == nextMap){
			nextMap = HvlMath.randomInt(MenuManager.NUM_TRACKS);
		}
	}

	@Override
	public void onConnection(SocketWrapper target){
		if(state != GameState.LOBBY) getServer().kick(target);

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

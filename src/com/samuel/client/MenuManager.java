package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.action.HvlAction1;
import com.osreboot.ridhvl.action.HvlAction2;
import com.osreboot.ridhvl.input.HvlInput;
import com.osreboot.ridhvl.menu.HvlComponentDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.HvlButton;
import com.osreboot.ridhvl.menu.component.HvlCheckbox;
import com.osreboot.ridhvl.menu.component.HvlComponentDrawable;
import com.osreboot.ridhvl.menu.component.HvlLabel;
import com.osreboot.ridhvl.menu.component.HvlSlider;
import com.osreboot.ridhvl.menu.component.HvlSlider.Direction;
import com.osreboot.ridhvl.menu.component.HvlSpacer;
import com.osreboot.ridhvl.menu.component.HvlTextBox;
import com.osreboot.ridhvl.menu.component.collection.HvlLabeledButton;
import com.samuel.GameState;
import com.samuel.InfoGame;
import com.samuel.InfoLobby;
import com.samuel.KC;
import com.samuel.Main;
import com.samuel.client.cars.AcuraNSX;
import com.samuel.client.cars.GolfGTI;
import com.samuel.client.cars.SubaruWRX;
import com.samuel.client.tracks.TestTrack;

public class MenuManager {

	public static final float PLAYER_LIST_SPACING = 48f;

	public static HvlMenu game, menuCar, ip, menuMap;
	static HvlInput changeToGame;
	static Car selectedCar;
	static Level selectedTrack;

	public static String username;
	public static Color color;

	public static void initialize() {
		game = new HvlMenu();
		menuCar = new HvlMenu();
		menuMap = new HvlMenu();
		ip = new HvlMenu();

		HvlComponentDefault.setDefault(HvlLabeledButton.class, new HvlLabeledButton.Builder().setWidth(100).setHeight(100).setFont(MainClient.gameFont).setTextColor(Color.white).setTextScale(0.25f).setOnDrawable(new HvlComponentDrawable() {
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

		HvlComponentDefault.setDefault(HvlLabel.class, new HvlLabel.Builder().setColor(Color.white).setFont(MainClient.gameFont).setScale(0.8f).build());

		HvlSlider defaultSlider = new HvlSlider(64, 256, Direction.VERTICAL, 64, 32, 1f, new HvlComponentDrawable(){
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg) {
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Color.gray);
			}
		}, new HvlComponentDrawable(){
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg) {
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Color.lightGray);
			}
		}, new HvlComponentDrawable(){
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg) {
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Color.darkGray);
			}
		});
		defaultSlider.setValue(1f);
		HvlComponentDefault.setDefault(defaultSlider);

		HvlCheckbox defaultCheckbox = new HvlCheckbox(180, 75, false, new HvlComponentDrawable(){
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg) {
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Color.darkGray);
				MainClient.gameFont.drawWordc("Not ready!", xArg+(widthArg/2), yArg+(heightArg/2), Color.red);
			}
		}, new HvlComponentDrawable(){
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg) {
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Color.darkGray);
				MainClient.gameFont.drawWordc("Ready!", xArg+(widthArg/2), yArg+(heightArg/2), Color.green);
			}
		});
		HvlComponentDefault.setDefault(defaultCheckbox);

		ip.add(new HvlArrangerBox.Builder().setStyle(ArrangementStyle.VERTICAL).setWidth(250).setHeight(400).setX((Display.getWidth()/2)-125).setY((Display.getHeight()/2)-200).build());

		ip.getFirstArrangerBox().add(new HvlSpacer(30, 30));
		ip.getFirstArrangerBox().add(new HvlLabel.Builder().setText("IP").build());
		ip.getFirstArrangerBox().add(new HvlSpacer(10, 10));


		ip.getFirstArrangerBox().add(new HvlTextBox.Builder().setWidth(200).setHeight(50).setFont(MainClient.gameFont).setTextColor(Color.darkGray).setTextScale(0.8f).setOffsetY(20).setOffsetX(20).setText("localhost").setFocusedDrawable(new HvlComponentDrawable() {	
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height, Color.lightGray);	
			}
		}).setUnfocusedDrawable(new HvlComponentDrawable() {

			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height, Color.green);	
			}
		}).build());

		ip.getFirstArrangerBox().add(new HvlSpacer(30, 30));
		ip.getFirstArrangerBox().add(new HvlLabel.Builder().setText("Username").build());
		ip.getFirstArrangerBox().add(new HvlSpacer(10, 10));


		ip.getFirstArrangerBox().add(new HvlTextBox.Builder().setWidth(200).setHeight(50).setFont(MainClient.gameFont).setTextColor(Color.darkGray).setTextScale(0.8f).setOffsetY(20).setOffsetX(20).setText("").setFocusedDrawable(new HvlComponentDrawable() {	
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height, Color.lightGray);	
			}
		}).setUnfocusedDrawable(new HvlComponentDrawable() {

			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height, Color.green);	
			}
		}).build());
		ip.getChildOfType(HvlArrangerBox.class, 0).add(new HvlSpacer(30, 30));

		ip.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Start Game").setTextScale(0.8f).setWidth(200).setClickedCommand(new HvlAction1<HvlButton>() {

			@Override
			public void run(HvlButton a) {
				if(!ip.getFirstArrangerBox().getFirstOfType(HvlTextBox.class).getText().equals("") && !ip.getFirstArrangerBox().getChildOfType(HvlTextBox.class, 1).getText().equals("")){
					String newIP = ip.getFirstArrangerBox().getFirstOfType(HvlTextBox.class).getText();
					username = ip.getFirstArrangerBox().getChildOfType(HvlTextBox.class, 1).getText();
					MainClient.getNewestInstance().reInstantiateClient(newIP, Main.INFO_PORT);
					MainClient.getNClient().start();
				}
			}
		}).build());


		menuCar.add(new HvlArrangerBox.Builder().setStyle(HvlArrangerBox.ArrangementStyle.HORIZONTAL).setX(Display.getWidth()/2).setY(Display.getHeight()/2).build());
		menuCar.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new SubaruWRX();
				//HvlMenu.setCurrent(menuMap);
			}

		}).build());
		menuCar.getFirstArrangerBox().add(new HvlSpacer(100,50));
		menuCar.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new GolfGTI();
				//HvlMenu.setCurrent(menuMap);
			}

		}).build());
		HvlMenu.setCurrent(menuCar);
		menuCar.getFirstArrangerBox().add(new HvlSpacer(100,50));
		menuCar.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new AcuraNSX();
				//HvlMenu.setCurrent(menuMap);
			}

		}).build());
		menuCar.getChildOfType(HvlArrangerBox.class, 0).add(new HvlSpacer(30, 30));
		menuCar.getFirstArrangerBox().add(new HvlSlider.Builder().setValueChangedCommand(new HvlAction2<HvlSlider, Float>(){
			@Override
			public void run(HvlSlider aArg, Float bArg) {
				color.r = bArg;
			}
		}).build());
		menuCar.getChildOfType(HvlArrangerBox.class, 0).add(new HvlSpacer(30, 30));
		menuCar.getFirstArrangerBox().add(new HvlSlider.Builder().setValueChangedCommand(new HvlAction2<HvlSlider, Float>(){
			@Override
			public void run(HvlSlider aArg, Float bArg) {
				color.g = bArg;
			}
		}).build());
		menuCar.getChildOfType(HvlArrangerBox.class, 0).add(new HvlSpacer(30, 30));
		menuCar.getFirstArrangerBox().add(new HvlSlider.Builder().setValueChangedCommand(new HvlAction2<HvlSlider, Float>(){
			@Override
			public void run(HvlSlider aArg, Float bArg) {
				color.b = bArg;
			}
		}).build());
		menuCar.getFirstArrangerBox().add(new HvlSpacer(100,50));
		menuCar.getFirstArrangerBox().add(new HvlCheckbox.Builder().build());

		menuMap.add(new HvlArrangerBox.Builder().setStyle(HvlArrangerBox.ArrangementStyle.HORIZONTAL).setX(Display.getWidth()/2).setY(Display.getHeight()/2).build());
		menuMap.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setWidth(200).setHeight(200).setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedTrack = new TestTrack();				
				HvlMenu.setCurrent(game);
				Game.initialize();
			}

		}).build());

		HvlMenu.setCurrent(ip);

		color = new Color(Color.white);
	}
	public static void update(float delta) {
		HvlMenu.updateMenus(delta);
		if(MainClient.getNewestInstance().isAuthenticated() && HvlMenu.getCurrent() == ip){
			HvlMenu.setCurrent(menuCar);
			menuCar.getFirstArrangerBox().getFirstOfType(HvlCheckbox.class).setChecked(false);
		}

		if(HvlMenu.getCurrent() == menuCar){
			if(MainClient.getNClient().<GameState>getValue(KC.key_GameState()) == GameState.RUNNING){
				selectedTrack = new TestTrack();//TODO server set track
				HvlMenu.setCurrent(game);
				Game.initialize();
			}

			if(!MainClient.getNClient().hasValue(KC.key_PlayerUsername(MainClient.getNUIDK()))){
				MainClient.getNClient().setValue(KC.key_PlayerUsername(MainClient.getNUIDK()), username, false);
				MainClient.getNClient().setValue(KC.key_PlayerLobbyInfo(MainClient.getNUIDK()), new InfoLobby(-1, false, Color.gray), false);
			}

			if(selectedCar != null){
				InfoLobby info = MainClient.getNClient().<InfoLobby>getValue(KC.key_PlayerLobbyInfo(MainClient.getNUIDK()));
				int newTexture = -1;
				if(selectedCar instanceof AcuraNSX) newTexture = MainClient.NSX_INDEX;
				if(selectedCar instanceof GolfGTI) newTexture = MainClient.GTI_INDEX;
				if(selectedCar instanceof SubaruWRX) newTexture = MainClient.WRX_INDEX;
				if(info.carTexture != newTexture || info.ready != menuCar.getFirstArrangerBox().getFirstOfType(HvlCheckbox.class).getChecked()){
					info.carTexture = newTexture;
					info.color = color;
					info.ready = menuCar.getFirstArrangerBox().getFirstOfType(HvlCheckbox.class).getChecked();
					MainClient.getNClient().setValue(KC.key_PlayerLobbyInfo(MainClient.getNUIDK()), info, false);
				}
				if(newTexture != -1){
					hvlDrawQuad(Display.getWidth() - 96f - 16f, 64f, 64f, 64f, MainClient.getTexture(newTexture));
					hvlDrawQuad(Display.getWidth() - 96f - 16f, 64f, 64f, 64f, MainClient.getTexture(newTexture + 1), color);
				}
			}
			MainClient.gameFont.drawWordc("Select a Car and choose and a Color, then Press Ready", Display.getWidth()/2, 200, Color.white);
			hvlDrawQuadc(menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getY()+50, 100, 100, MainClient.getTexture(MainClient.WRX_INDEX));
			hvlDrawQuadc(menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getY()+50, 100, 100, MainClient.getTexture(MainClient.WRX_A_INDEX), color);
			hvlDrawQuadc(menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 1).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 1).getY()+50, 100, 100, MainClient.getTexture(MainClient.GTI_INDEX));
			hvlDrawQuadc(menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 1).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 1).getY()+50, 100, 100, MainClient.getTexture(MainClient.GTI_A_INDEX), color);
			hvlDrawQuadc(menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 2).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 2).getY()+50, 100, 100, MainClient.getTexture(MainClient.NSX_INDEX));
			hvlDrawQuadc(menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 2).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 2).getY()+50, 100, 100, MainClient.getTexture(MainClient.NSX_A_INDEX), color);

			MainClient.gameFont.drawWord(username, Display.getWidth() - MainClient.gameFont.getLineWidth(username) - 16, 16, color);

			MainClient.gameFont.drawWordc("Subaru WRX", menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getY()+150, color, 0.7f);
			MainClient.gameFont.drawWordc("Volkswagen GTI", menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 1).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 1).getY()+150, color, 0.7f);
			MainClient.gameFont.drawWordc("Acura NSX", menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 2).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 2).getY()+150, color, 0.7f);

			drawPlayerLobbyInfoList();
		} else if(HvlMenu.getCurrent() == game) {
			if(MainClient.getNClient().<GameState>getValue(KC.key_GameState()) == GameState.LOBBY){
				HvlMenu.setCurrent(menuCar);
			}
			Game.update(delta);
			if(MainClient.getNClient().hasValue(KC.key_PlayerGameInfo(MainClient.getNUIDK()))){
				InfoLobby lobbyInfo = MainClient.getNClient().<InfoLobby>getValue(KC.key_PlayerLobbyInfo(MainClient.getNUIDK()));
				InfoGame gameInfo = MainClient.getNClient().<InfoGame>getValue(KC.key_PlayerGameInfo(MainClient.getNUIDK()));
				gameInfo.location = new HvlCoord2D(Game.player.getXPos(), Game.player.getYPos());
				gameInfo.rotation = Game.player.turnAngle;
				gameInfo.carTexture = lobbyInfo.carTexture;
				gameInfo.color = color;
				if(Game.player.finalTrackTime != 0)
					gameInfo.finishTime = Game.player.finalTrackTime;
				MainClient.getNClient().setValue(KC.key_PlayerGameInfo(MainClient.getNUIDK()), gameInfo, false);
			}
			MainClient.gameFont.drawWord(username, Display.getWidth() - MainClient.gameFont.getLineWidth(username) - 16, 16, color);
		} else if(HvlMenu.getCurrent() == menuMap) {
			hvlDrawQuadc(menuMap.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getX()+100, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getY()+50, 200, 200, MainClient.getTexture(MainClient.TEST_TRACK_INDEX));

			MainClient.gameFont.drawWordc("Test Track", menuMap.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getX()+100, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getY()+200, Color.white, 0.7f);
		}
	}

	private static void drawPlayerLobbyInfoList(){
		if(MainClient.getNewestInstance().isAuthenticated() && 
				MainClient.getNClient().hasValue(KC.key_GameUsernameList()) &&
				MainClient.getNClient().hasValue(KC.key_GameLobbyInfoList()) &&
				MainClient.getNClient().hasValue(KC.key_PlayerListIndex(MainClient.getNUIDK()))){
			int counter = 0;
			float offset = 0;
			for(String s : MainClient.getNClient().<ArrayList<String>>getValue(KC.key_GameUsernameList())){
				if(counter != MainClient.getNClient().<Integer>getValue(KC.key_PlayerListIndex(MainClient.getNUIDK()))){
					Color userColor = Color.white;
					if(MainClient.getNClient().<ArrayList<InfoLobby>>getValue(KC.key_GameLobbyInfoList()).size() >= counter
							&& MainClient.getNClient().<ArrayList<InfoLobby>>getValue(KC.key_GameLobbyInfoList()).get(counter) != null){
						InfoLobby info = MainClient.getNClient().<ArrayList<InfoLobby>>getValue(KC.key_GameLobbyInfoList()).get(counter);
						if(info.color != null) userColor = info.color;
						if(info.carTexture != -1){
							hvlDrawQuad(256, offset * PLAYER_LIST_SPACING + 16f, PLAYER_LIST_SPACING - 4f, PLAYER_LIST_SPACING - 4f, MainClient.getTexture(info.carTexture));
							hvlDrawQuad(256, offset * PLAYER_LIST_SPACING + 16f, PLAYER_LIST_SPACING - 4f, PLAYER_LIST_SPACING - 4f, MainClient.getTexture(info.carTexture + 1), userColor);
						}
						MainClient.gameFont.drawWord("[ " + (info.ready ? "READY" : "  ") + " ]", 256f + 48f, offset * PLAYER_LIST_SPACING + 16f, userColor);
					}
					MainClient.gameFont.drawWord(s, 16, offset * PLAYER_LIST_SPACING + 16f, userColor);
					offset++;
				}
				counter++;
			}
			if(MainClient.getNClient().hasValue(KC.key_GameReadyTimer()) && 
					MainClient.getNClient().<Float>getValue(KC.key_GameReadyTimer()) < 1f){
				hvlDrawQuad(0, Display.getHeight() - 64f, Display.getWidth() * MainClient.getNClient().<Float>getValue(KC.key_GameReadyTimer()), 64, Color.red);
			}
		}
	}


}

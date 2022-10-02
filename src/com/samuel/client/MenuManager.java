package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;
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
import com.samuel.client.cars.Camaro;
import com.samuel.client.cars.GolfGTI;
import com.samuel.client.cars.Mini;
import com.samuel.client.cars.SubaruWRX;
import com.samuel.client.cars.Supra;
import com.samuel.client.effects.CarEffectApplicator;
import com.samuel.client.effects.MysteryUnlocker;
import com.samuel.client.tracks.CurveyBoi;
import com.samuel.client.tracks.OS_Drag;
import com.samuel.client.tracks.OS_Hell;
import com.samuel.client.tracks.Retro_Track;
import com.samuel.client.tracks.TestTrack;
import com.samuel.client.tracks.YourMom;
import com.samuel.server.MainServer;

public class MenuManager {

	public static final float PLAYER_LIST_SPACING = 48f;
	public static final int NUM_TRACKS = 6;

	public static HvlMenu game, menuCar, ip, menuMap;
	static HvlInput changeToGame;
	static Car selectedCar;
	static Level selectedTrack;

	public static String username;
	public static Color color;
	
	public static boolean singlePlayer;
	
	public static void drawStats(float x, float y) {
		if(selectedCar != null) {
			MainClient.gameFont.drawWord(selectedCar.NAME,x , y, color, 1.2f);
			hvlDrawQuad(x, y+40, 300, 1, color);
			MainClient.gameFont.drawWord("Acc: "+selectedCar.ACCELERATION,x , y+50, Color.white);
			MainClient.gameFont.drawWord("Gear Count: "+selectedCar.GEAR_COUNT,x , y+100, Color.white);
			MainClient.gameFont.drawWord("Grip: "+selectedCar.TIRE_GRIP,x , y+150, Color.white);
			MainClient.gameFont.drawWord("Max RPM: "+selectedCar.MAX_RPM,x , y+200, Color.white);
			MainClient.gameFont.drawWord("Top Speed: "+selectedCar.maxSpeedsPerGear[selectedCar.GEAR_COUNT - 1],x , y+250, Color.white);

		}
	}

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

		HvlSlider defaultSlider = new HvlSlider(64, 256, Direction.VERTICAL, 64, 16, 1f, new HvlComponentDrawable(){
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
		defaultSlider.setValue(0.5f);
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


		ip.getFirstArrangerBox().add(new HvlTextBox.Builder().setWidth(200).setHeight(50).setMaxCharacters(10).setFont(MainClient.gameFont).setTextColor(Color.darkGray).setTextScale(0.8f).setOffsetY(20).setOffsetX(20).setText("").setFocusedDrawable(new HvlComponentDrawable() {	
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

		ip.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Connect").setTextScale(0.8f).setWidth(200).setClickedCommand(new HvlAction1<HvlButton>() {

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
		
		ip.getFirstArrangerBox().add(new HvlSpacer(30, 30));
		
		ip.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Single Player").setTextScale(0.8f).setWidth(200).setClickedCommand(new HvlAction1<HvlButton>() {

			@Override
			public void run(HvlButton a) {
				singlePlayer = true;
				HvlMenu.setCurrent(menuCar);
			}
		}).build());


		menuCar.add(new HvlArrangerBox.Builder().setStyle(HvlArrangerBox.ArrangementStyle.HORIZONTAL).setX(Display.getWidth()/2).setY(Display.getHeight()/2).build());
		
		menuCar.getFirstArrangerBox().add(new HvlArrangerBox.Builder().setStyle(HvlArrangerBox.ArrangementStyle.VERTICAL).setX(100).build());

		menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new SubaruWRX();
				MysteryUnlocker.enterCharacter('0');
			}

		}).build());
		menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).add(new HvlSpacer(100,50));
		menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new GolfGTI();
				MysteryUnlocker.enterCharacter('1');
			}

		}).build());
		HvlMenu.setCurrent(menuCar);
		menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).add(new HvlSpacer(100,50));
		menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new AcuraNSX();
				MysteryUnlocker.enterCharacter('2');
			}

		}).build());
		menuCar.getFirstArrangerBox().add(new HvlSpacer(200, 00));
		menuCar.getFirstArrangerBox().add(new HvlArrangerBox.Builder().setStyle(HvlArrangerBox.ArrangementStyle.VERTICAL).build());
		menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new Camaro();
				MysteryUnlocker.enterCharacter('3');
			}

		}).build());
		menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).add(new HvlSpacer(100,50));
		menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new Mini();
				MysteryUnlocker.enterCharacter('4');
			}

		}).build());
		menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).add(new HvlSpacer(100,50));
		menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new Supra();
				MysteryUnlocker.enterCharacter('5');
			}

		}).build());
		menuCar.getChildOfType(HvlArrangerBox.class, 0).add(new HvlSpacer(300, 100));
		menuCar.getFirstArrangerBox().add(new HvlSlider.Builder().setValueChangedCommand(new HvlAction2<HvlSlider, Float>(){
			@Override
			public void run(HvlSlider aArg, Float bArg) {
				color.r = 1f - bArg;
			}
		}).build());
		menuCar.getChildOfType(HvlArrangerBox.class, 0).add(new HvlSpacer(30, 30));
		menuCar.getFirstArrangerBox().add(new HvlSlider.Builder().setValueChangedCommand(new HvlAction2<HvlSlider, Float>(){
			@Override
			public void run(HvlSlider aArg, Float bArg) {
				color.g = 1f - bArg;
			}
		}).build());
		menuCar.getChildOfType(HvlArrangerBox.class, 0).add(new HvlSpacer(30, 30));
		menuCar.getFirstArrangerBox().add(new HvlSlider.Builder().setValueChangedCommand(new HvlAction2<HvlSlider, Float>(){
			@Override
			public void run(HvlSlider aArg, Float bArg) {
				color.b = 1f - bArg;
			}
		}).build());
		menuCar.getFirstArrangerBox().add(new HvlSpacer(100,50));
		menuCar.getFirstArrangerBox().add(new HvlCheckbox.Builder().build());

		HvlMenu.setCurrent(ip);

		color = new Color(Color.white);
	}
	public static void update(float delta) {
		HvlMenu.updateMenus(delta);
		if(MainClient.getNewestInstance().isAuthenticated() && HvlMenu.getCurrent() == ip){
			HvlMenu.setCurrent(menuCar);
			MysteryUnlocker.initialize();
			menuCar.getFirstArrangerBox().getFirstOfType(HvlCheckbox.class).setChecked(false);
		}

		if(HvlMenu.getCurrent() == menuCar){
			hvlDrawQuad(0,0, 1920, 1080, new Color(19,80, 255, 75));
			
			if(!singlePlayer) {
				if(MainClient.getNClient().<GameState>getValue(KC.key_GameState()) == GameState.MAP){
					HvlMenu.setCurrent(menuMap);
				}
			
				if(!MainClient.getNClient().hasValue(KC.key_PlayerUsername(MainClient.getNUIDK()))){
					MainClient.getNClient().setValue(KC.key_PlayerUsername(MainClient.getNUIDK()), username, false);
					MainClient.getNClient().setValue(KC.key_PlayerLobbyInfo(MainClient.getNUIDK()), new InfoLobby(-1, false, Color.gray), false);
				}
			}
			if(selectedCar != null){
				InfoLobby info = null;
				if(!singlePlayer) {
					info = MainClient.getNClient().<InfoLobby>getValue(KC.key_PlayerLobbyInfo(MainClient.getNUIDK()));
				}
				int newTexture = -1;
				if(selectedCar instanceof AcuraNSX) newTexture = MainClient.NSX_INDEX;
				if(selectedCar instanceof GolfGTI) newTexture = MainClient.GTI_INDEX;
				if(selectedCar instanceof SubaruWRX) newTexture = MainClient.WRX_INDEX;
				if(selectedCar instanceof Camaro) newTexture = MainClient.CAMARO_INDEX;
				if(selectedCar instanceof Mini) newTexture = MainClient.MINI_INDEX;
				if(selectedCar instanceof Supra) newTexture = MainClient.SUPRA_INDEX;

				if(!singlePlayer) {
					if(info.carTexture != newTexture || info.ready != menuCar.getFirstArrangerBox().getFirstOfType(HvlCheckbox.class).getChecked()){
						info.carTexture = newTexture;
						info.color = color;
						info.ready = menuCar.getFirstArrangerBox().getFirstOfType(HvlCheckbox.class).getChecked();
						MainClient.getNClient().setValue(KC.key_PlayerLobbyInfo(MainClient.getNUIDK()), info, false);
					}
				}
				
				if(newTexture != -1){
					hvlDrawQuad(Display.getWidth() - 96f - 16f, 64f, 64f, 64f, MainClient.getTexture(newTexture));
					hvlDrawQuad(Display.getWidth() - 96f - 16f, 64f, 64f, 64f, MainClient.getTexture(newTexture + 1), color);
				}
			}
			drawStats(20, 350);
			MainClient.gameFont.drawWordc("Select a Car and choose and a Color, then Press Ready", Display.getWidth()/2, 200, Color.white);
			CarEffectApplicator.drawCar(MysteryUnlocker.myUnlockedEffect, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 0).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 0).getY()+50, 0f, MainClient.WRX_INDEX, color);
			CarEffectApplicator.drawCar(MysteryUnlocker.myUnlockedEffect, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 1).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 1).getY()+50, 0f, MainClient.GTI_INDEX, color);
			CarEffectApplicator.drawCar(MysteryUnlocker.myUnlockedEffect, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 2).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 2).getY()+50, 0f, MainClient.NSX_INDEX, color);
			CarEffectApplicator.drawCar(MysteryUnlocker.myUnlockedEffect, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 0).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 0).getY()+50, 0f, MainClient.CAMARO_INDEX, color);
			CarEffectApplicator.drawCar(MysteryUnlocker.myUnlockedEffect, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 1).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 1).getY()+50, 0f, MainClient.MINI_INDEX, color);
			CarEffectApplicator.drawCar(MysteryUnlocker.myUnlockedEffect, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 2).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 2).getY()+50, 0f, MainClient.SUPRA_INDEX, color);

			if(!singlePlayer) {
				MainClient.gameFont.drawWord(username, Display.getWidth() - MainClient.gameFont.getLineWidth(username) - 16, 16, color);
			}
		
			MainClient.gameFont.drawWordc("Subaru WRX", menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 0).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 0).getY()+120, color, 0.7f);
			MainClient.gameFont.drawWordc("Volkswagen GTI", menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 1).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 1).getY()+120, color, 0.7f);
			MainClient.gameFont.drawWordc("Acura NSX", menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 2).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 0).getChildOfType(HvlLabeledButton.class, 2).getY()+120, color, 0.7f);
			MainClient.gameFont.drawWordc("Chevy Camaro", menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 0).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 0).getY()+120, color, 0.7f);
			MainClient.gameFont.drawWordc("Mini Cooper", menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 1).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 1).getY()+120, color, 0.7f);
			MainClient.gameFont.drawWordc("Toyota Supra MK4", menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 2).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlArrangerBox.class, 1).getChildOfType(HvlLabeledButton.class, 2).getY()+120, color, 0.7f);

			MainClient.gameFont.drawWordc("Ready Up!", menuCar.getFirstArrangerBox().getChildOfType(HvlCheckbox.class, 0).getX()+90, menuCar.getFirstArrangerBox().getChildOfType(HvlCheckbox.class, 0).getY()-50, Color.white);
			MainClient.gameFont.drawWordc("Red", menuCar.getFirstArrangerBox().getChildOfType(HvlSlider.class, 0).getX()+32, menuCar.getFirstArrangerBox().getChildOfType(HvlSlider.class, 0).getY()-50, Color.white);
			MainClient.gameFont.drawWordc("Green", menuCar.getFirstArrangerBox().getChildOfType(HvlSlider.class, 1).getX()+32, menuCar.getFirstArrangerBox().getChildOfType(HvlSlider.class, 1).getY()-50, Color.white);
			MainClient.gameFont.drawWordc("Blue", menuCar.getFirstArrangerBox().getChildOfType(HvlSlider.class, 2).getX()+32, menuCar.getFirstArrangerBox().getChildOfType(HvlSlider.class, 2).getY()-50, Color.white);

			drawPlayerLobbyInfoList();
			
			if(menuCar.getFirstArrangerBox().getFirstOfType(HvlCheckbox.class).getChecked()) {
				HvlMenu.setCurrent(menuMap);
			}
			
		} else if(HvlMenu.getCurrent() == game) {
			if(!singlePlayer) {
				if(MainClient.getNClient().<GameState>getValue(KC.key_GameState()) == GameState.LOBBY){
					HvlMenu.setCurrent(menuCar);
					MysteryUnlocker.initialize();
					menuCar.getFirstArrangerBox().getFirstOfType(HvlCheckbox.class).setChecked(false);
				}
			}
			
			Game.update(delta);
			if(!singlePlayer) {
				if(MainClient.getNClient().hasValue(KC.key_PlayerGameInfo(MainClient.getNUIDK()))){
					InfoLobby lobbyInfo = MainClient.getNClient().<InfoLobby>getValue(KC.key_PlayerLobbyInfo(MainClient.getNUIDK()));
					InfoGame gameInfo = MainClient.getNClient().<InfoGame>getValue(KC.key_PlayerGameInfo(MainClient.getNUIDK()));
					gameInfo.location = new HvlCoord2D(Game.player.getXPos(), Game.player.getYPos());
					gameInfo.rotation = Game.player.turnAngle;
					gameInfo.carTexture = lobbyInfo.carTexture;
					gameInfo.color = color;
					gameInfo.effect = MysteryUnlocker.myUnlockedEffect;
					if(Game.player.finalTrackTime != 0)
						gameInfo.finishTime = Game.player.finalTrackTime;
					MainClient.getNClient().setValue(KC.key_PlayerGameInfo(MainClient.getNUIDK()), gameInfo, false);
				}
				MainClient.gameFont.drawWord(username, Display.getWidth() - MainClient.gameFont.getLineWidth(username) - 16, 16, color);
			}
		
		} else if(HvlMenu.getCurrent() == menuMap) {
			int trackNum = 1;
//			if(!singlePlayer) {
//				trackNum = MainClient.getNClient().<Integer>getValue(KC.key_GameMap());
//			}else {
//				trackNum = HvlMath.randomIntBetween(0, 6);
//			}
			
			switch(trackNum) {
				case 0:
					selectedTrack = new TestTrack();
					hvlDrawQuad(0,0, 1920, 1080, MainClient.getTexture(MainClient.TEST_TRACK_INDEX));
					break;
				case 1:
					selectedTrack = new CurveyBoi();
					hvlDrawQuad(0,0, 1920, 1080, MainClient.getTexture(MainClient.CURVE_TRACK_INDEX));
					break;
				case 2:
					selectedTrack = new OS_Hell();
					hvlDrawQuad(0,0, 1920, 1080, MainClient.getTexture(MainClient.OSHELL_TRACK_INDEX));
					break;
				case 3:
					selectedTrack = new YourMom();
					hvlDrawQuad(0,0, 1920, 1080, MainClient.getTexture(MainClient.YOUR_MOM_INDEX));
					break;
				case 4:
					selectedTrack = new Retro_Track();
					hvlDrawQuad(0,0, 1920, 1080, MainClient.getTexture(MainClient.RETRO_INDEX));
					break;
				case 5:
					selectedTrack = new OS_Drag();
					hvlDrawQuad(0,0, 1920, 1080, MainClient.getTexture(MainClient.DRAG_INDEX));
					break;
				default:
					selectedTrack = new TestTrack();
					break;
			}
			MainClient.gameFont.drawWordc("Now traveling to "+selectedTrack.name+"...", Display.getWidth()/2, 100, Color.black, 3f);
			
			if(!singlePlayer) {
				if(MainClient.getNClient().<GameState>getValue(KC.key_GameState()) == GameState.RUNNING){
					Game.initialize();
					HvlMenu.setCurrent(game);
				}
			}else {
				Game.initialize();
				HvlMenu.setCurrent(game);	
			}
			
			
		}else if(HvlMenu.getCurrent() == ip) {
			hvlDrawQuad(0,0, 1920, 1080, new Color(19,80, 255, 75));
			MainClient.gameFont.drawWordc("If nothing happens, it either means: \n\n1) The game you are trying to join is in a race, \n2) The entered IP is incorrect, \n3) The client and server versions mismatch" , 420, 540, Color.white);
			MainClient.gameFont.drawWordc("Contact me: \n\nInstagram: @munro.samuel\nSnapchat: munrosamuel1\nSteam: HaveANiceDay33" , 280, 960, Color.white);
			MainClient.gameFont.drawWordc("Controls: \n\nW: Forward\nA: Turn Left\nD: Turn Right\nS/Space: Brake\nP: Shift up\nL: Shift Down" , 200, 200, Color.white);
			MainClient.gameFont.drawWordc("Version: "+Main.INFO_VERSION , 1800, 50, Color.white, 0.7f);
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

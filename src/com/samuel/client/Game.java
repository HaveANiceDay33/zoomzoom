package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlDebugUtil;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.action.HvlAction1;
import com.osreboot.ridhvl.input.HvlInput;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.painter.HvlCamera2D;
import com.samuel.InfoGame;
import com.samuel.KC;
import com.samuel.client.effects.CarEffect;
import com.samuel.client.effects.CarEffectApplicator;

public class Game {
	static int currentGear;
	public static int speed;
	static int currentRPM;
	static int currentRPMGoal;
	static int clutchTimer;
	static float rpmMod;
	static int FRICTION;

	static float accurateAngleRPM;
	static float accurateAngleSpeed;
	static float speedGoal;

	static HvlInput shiftUpInput;
	static HvlInput shiftDownInput;
	static HvlCamera2D tracker;
	HvlMenu menu;
	HvlMenu game;

	static float minutesElap; 
	static float secsElap;

	static float startTimer;
	static float trackTimer;

	static float endTimer;

	public static Player player;
	static TrackGenerator trackGen;

	public static void drawOtherPlayers(float xPos, float yPos, float turnAngle, int textureIndex, Color customColor, CarEffect carEffect, String userName) {
		CarEffectApplicator.drawCar(carEffect, xPos, yPos, turnAngle, textureIndex, customColor);
		MainClient.gameFont.drawWordc(userName, xPos + 1, yPos - 79, Color.black, 0.75f);
		MainClient.gameFont.drawWordc(userName, xPos, yPos - 80, customColor, 0.75f);
	}

	public static void drawPlayerCars(){
		if(MainClient.getNClient().hasValue(KC.key_GameGameInfoList())){
			int counter = 0;
			for(String s : MainClient.getNClient().<ArrayList<String>>getValue(KC.key_GameUsernameList())){
				if(counter != MainClient.getNClient().<Integer>getValue(KC.key_PlayerListIndex(MainClient.getNUIDK()))){
					if(MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList()).size() >= counter
							&& MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList()).get(counter) != null){
						InfoGame info = MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList()).get(counter);
						Game.drawOtherPlayers(info.location.x, info.location.y, info.rotation, info.carTexture, info.color, info.effect, s);
					}
				}
				counter++;
			}
		}
	}

	public static void drawPlayerTimes() {
		ArrayList<InfoGame> preSort = new ArrayList<>();

		if(MainClient.getNClient().hasValue(KC.key_GameGameInfoList())){
			int counter = 0;
			for(String s : MainClient.getNClient().<ArrayList<String>>getValue(KC.key_GameUsernameList())){
				if(MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList()).size() >= counter
						&& MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList()).get(counter) != null){
					InfoGame info = MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList()).get(counter);
					if(info.finishTime != -1f) 
						preSort.add(info);
				}
				counter++;
			}
		}
		if(preSort.size() > 0){
			ArrayList<InfoGame> postSort = new ArrayList<>();
			while(preSort.size() > 0){
				InfoGame lowest = null;
				for(InfoGame g : new ArrayList<>(preSort)){
					if(lowest == null || g.finishTime < lowest.finishTime) lowest = g;
				}
				preSort.remove(lowest);
				postSort.add(lowest);
			}

			float offset = 150f;
			for(InfoGame g : postSort){
				offset += MenuManager.PLAYER_LIST_SPACING;
				MainClient.gameFont.drawWord(g.username + " : " + HvlMath.cropDecimals(g.finishTime, 2), 1500f, offset, g.color, 1.5f);
			}
		}
	}

	public static void drawTach(int x, int y) {
		accurateAngleRPM = HvlMath.map(currentRPM, 0, 8000, -145, 145);
		hvlDrawQuadc(x, y, 330, 330, MainClient.getTexture(MainClient.CIRCLE_INDEX), Color.black);
		hvlDrawQuadc(x,y, 330, 330, MainClient.getTexture(MainClient.TACH_INDEX));
		hvlRotate(x, y, accurateAngleRPM);
		hvlDrawQuadc(x, y, 5, 255, MainClient.getTexture(MainClient.NEEDLE_INDEX));
		hvlDrawQuadc(x, y, 30, 30, MainClient.getTexture(MainClient.CIRCLE_INDEX));
		hvlDrawQuadc(x, y, 25, 25, MainClient.getTexture(MainClient.CIRCLE_INDEX), Color.black);
		hvlResetRotation();
		for(float i = 0; i < 62; i++) {
			double radiansRot = (Math.toRadians(4.8) * i)-Math.toRadians(-124);
			hvlRotate(x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137),(float)radiansRot * i);
			hvlDrawQuadc(x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137), 5, 30, new Color(0,0,255,0.4f));
			hvlResetRotation();
		}
		for(float i = 0; i < 9; i++) {
			double radiansRot = (Math.toRadians(37) * i)-Math.toRadians(-123);
			if(i == 8 || i == 7) {
				MainClient.gameFont.drawWordc((int)(i)+"",x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137), Color.red);
			} else {
				MainClient.gameFont.drawWordc((int)(i)+"",x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137), Color.white);
			}
		}


		MainClient.gameFont.drawWordc(""+currentGear, x, y+75,Color.white);
		if(currentRPM >= player.selectedCar.MAX_RPM-500) {
			hvlDrawQuadc(x+30, y+75, 10,10, MainClient.getTexture(MainClient.CIRCLE_INDEX));
		}
	}
	public static void drawSpeed(int x, int y) {
		accurateAngleSpeed = HvlMath.map(speed, 0, 180, -145, 145);
		hvlDrawQuadc(x, y, 330, 330, MainClient.getTexture(MainClient.CIRCLE_INDEX), Color.black);

		hvlDrawQuadc(x,y, 330, 330, MainClient.getTexture(MainClient.TACH_INDEX));
		hvlRotate(x, y,accurateAngleSpeed);
		hvlDrawQuadc(x, y, 5, 255, MainClient.getTexture(MainClient.NEEDLE_INDEX));
		hvlDrawQuadc(x, y, 30, 30, MainClient.getTexture(MainClient.CIRCLE_INDEX));
		hvlDrawQuadc(x, y, 25, 25, MainClient.getTexture(MainClient.CIRCLE_INDEX), Color.black);

		hvlResetRotation();
		for(float i = 0; i < 62; i++) {
			double radiansRot = (Math.toRadians(4.8) * i)-Math.toRadians(-124);
			hvlRotate(x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137),(float)radiansRot * i);
			hvlDrawQuadc(x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137), 5, 30, new Color(0,0,255,0.4f));
			hvlResetRotation();
		}
		for(float i = 0; i < 10; i++) {
			double radiansRot = (Math.toRadians(33) * i)-Math.toRadians(-123);
			MainClient.gameFont.drawWordc((int)(i*20)+"",x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137), Color.white);
		}
		MainClient.gameFont.drawWordc(speed+" MPH",x, y+75, Color.white);
	}
	public static void keyPresses() {
		shiftUpInput = new HvlInput(new HvlInput.InputFilter() {
			@Override
			public float getCurrentOutput() {
				if(Keyboard.isKeyDown(Keyboard.KEY_P)&& startTimer <= 0.1) {
					return 1;
				}
				else {
					return 0;
				}
			}
		});
		shiftDownInput = new HvlInput(new HvlInput.InputFilter() {
			@Override
			public float getCurrentOutput() {
				if(Keyboard.isKeyDown(Keyboard.KEY_L)) {
					return 1;
				}
				else {
					return 0;
				}
			}
		});
	}

	public static final boolean CAMERA_MODE = false;

	public static void initialize() {
		player = new Player(Display.getWidth()/2, Display.getHeight()/2);
		tracker = new HvlCamera2D(Display.getWidth()/2, Display.getHeight()/2, 0 , CAMERA_MODE ? 0.1f : 1f, HvlCamera2D.ALIGNMENT_CENTER);
		trackGen = new TrackGenerator();
		player.turnAngle = 0;
		startTimer = 6;
		endTimer = 11;
		trackTimer = 0;
		secsElap = 0;
		currentGear = 1;
		currentRPM = player.selectedCar.MIN_RPM;
		currentRPMGoal = player.selectedCar.MIN_RPM;
		speedGoal = 0;
		currentRPMGoal = 0;
		speed = 0;
		FRICTION = 20;

		shiftUpInput.setPressedAction(new HvlAction1<HvlInput>() {
			@Override
			public void run(HvlInput a) {
				if(currentGear < player.selectedCar.GEAR_COUNT) {
					currentGear++;
					currentRPMGoal = (int)((float)player.selectedCar.MAX_RPM * (float)speed / (float)player.selectedCar.maxSpeedsPerGear[currentGear - 1]);				
				}
			}
		});
		shiftDownInput.setPressedAction(new HvlAction1<HvlInput>() {
			@Override
			public void run(HvlInput a) {
				if(currentGear > 1) {
					currentGear--;
					currentRPMGoal = (int)((float)player.selectedCar.MAX_RPM * (float)speed / (float)player.selectedCar.maxSpeedsPerGear[currentGear - 1]);
				}
			}
		});

		trackGen.generateTrack();
		TerrainGenerator.generateTerrain();
	}
	public static void update(float delta) {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			rpmMod = (player.selectedCar.ACCELERATION / currentGear);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			rpmMod = (-50 / currentGear);
		} else {
			rpmMod = (-FRICTION/currentGear);
		}
		
		if(startTimer <= 0.1) {
			speedGoal = HvlMath.stepTowards(speedGoal, 0.5f * 142 * delta, (((float)currentRPMGoal / (float)player.selectedCar.MAX_RPM) * (float)player.selectedCar.maxSpeedsPerGear[currentGear - 1]));
		}

		if(speedGoal <= 0) {
			speedGoal = 0;
		}

		if(currentRPMGoal <= player.selectedCar.MIN_RPM) {
			currentRPMGoal = player.selectedCar.MIN_RPM;
		}
		if(currentRPMGoal >= player.selectedCar.MAX_RPM) {
			currentRPMGoal = player.selectedCar.MAX_RPM;
		}
		if(!TrackGenerator.onTrack) {
			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				rpmMod = (float)(player.selectedCar.ACCELERATION / currentGear/3.5);
			}
			if(currentRPMGoal > 1300) {
				currentRPMGoal -= (20 * 142 *delta);
			}		
		}
		if(Border.hitWall) {
			if(Player.xSpeed > 0) {
				Player.xPos -= 32;
			}
			if(Player.xSpeed < 0) {
				Player.xPos += 32;
			}
			if(Player.ySpeed > 0) {
				Player.yPos += 32;
			}
			if(Player.ySpeed < 0) {
				Player.yPos -= 32;
			}
			Player.xSpeed = 0;
			Player.ySpeed = 0;
			
			currentRPMGoal = 0;
			Player.throttle = 0;
		}
		currentRPMGoal += (rpmMod * 142 *delta);
		if(startTimer >= 0.1 && currentRPMGoal > 3000) {
			currentRPMGoal = 3000;
		}
		currentRPM = (int) HvlMath.stepTowards(currentRPM, 35 * 142 * delta, currentRPMGoal); 
		speed = (int) HvlMath.stepTowards(speed, 1, speedGoal);

		//Main.gameFont.drawWordc(currentRPM + " RPM", 600, 345,Color.white);

		player.update(delta);
		tracker.setX(player.getXPos());
		tracker.setY(player.getYPos());
		tracker.doTransform(new HvlAction0() {
			@Override
			public void run() {
				if(CAMERA_MODE) {
					hvlDrawQuadc(player.getXPos(), player.getYPos(), 40000, 40000, new Color(70, 116, 15));

				}else {
					hvlDrawQuadc(player.getXPos(), player.getYPos(), 1920, 1080, new Color(70, 116, 15));

				}
				trackGen.update(delta);
				TerrainGenerator.draw(delta);
				if(!CAMERA_MODE) {
					drawPlayerCars();
					player.draw(delta);
				}

			}
		});
		drawTach(190, 870);
		drawSpeed(1730, 870);
		if(startTimer >= 0.1) {
			startTimer = HvlMath.stepTowards(startTimer,  delta, 0);
			MainClient.gameFont.drawWordc((int)startTimer + "", Display.getWidth()/2, Display.getHeight()/2 - 200, Color.black, 10f);
		} 
		else {
			if(TrackGenerator.trackComplete == true) {
				player.finalTrackTime = trackTimer;
				MainClient.gameFont.drawWordc("Your final time is: "+HvlMath.cropDecimals(player.finalTrackTime, 2), 1500, 100, Color.black, 2.5f);
				//MainClient.gameFont.drawWordc("Time Until Next Race: "+(int)endTimer, 1500, 200, Color.black, 2f);

				endTimer = HvlMath.stepTowards(endTimer,  delta, 0);
				if(endTimer < 0.1) {
					//HvlMenu.setCurrent(MenuManager.menuCar);
				}
			} else {
				trackTimer += delta;
				minutesElap = (int)trackTimer/60;
				secsElap = trackTimer % 60;
			}

		}
		MainClient.gameFont.drawWord((int)minutesElap+":"+ HvlMath.cropDecimals(secsElap, 2), 100, 100, Color.black, 2f);
		drawPlayerTimes();


		HvlDebugUtil.drawFPSCounter(MainClient.gameFont, 200, 20, 1f, Color.black);
	}
}

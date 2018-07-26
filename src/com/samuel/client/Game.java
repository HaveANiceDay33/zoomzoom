 package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlDebugUtil;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.HvlTimer;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.action.HvlAction1;
import com.osreboot.ridhvl.input.HvlInput;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.painter.HvlCamera2D;

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
	
	static Player player;
	static TrackGenerator trackGen;
	
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
	public static void initialize() {
		player = new Player(Display.getWidth()/2, Display.getHeight()/2);
		tracker = new HvlCamera2D(Display.getWidth()/2, Display.getHeight()/2, 0 , 1f, HvlCamera2D.ALIGNMENT_CENTER);
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
			rpmMod = player.selectedCar.ACCELERATION / currentGear;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			rpmMod = -50 / currentGear;
		} else {
			rpmMod = -FRICTION/currentGear;
		}
		if(startTimer <= 0.1) {
			speedGoal = HvlMath.stepTowards(speedGoal, 0.5f, (((float)currentRPMGoal / (float)player.selectedCar.MAX_RPM) * (float)player.selectedCar.maxSpeedsPerGear[currentGear - 1]));
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
				rpmMod = player.selectedCar.ACCELERATION / currentGear/4;
			}
			if(currentRPMGoal > 2500) {
				currentRPMGoal -= 15;
			}		
		}
		currentRPMGoal += rpmMod;
		if(startTimer >= 0.1 && currentRPMGoal > 3000) {
			currentRPMGoal = 3000;
		}
		currentRPM = (int) HvlMath.stepTowards(currentRPM, 35, currentRPMGoal); 
		speed = (int) HvlMath.stepTowards(speed, 1, speedGoal);
		
		//Main.gameFont.drawWordc(currentRPM + " RPM", 600, 345,Color.white);

		player.update(delta);
		tracker.setX(player.getXPos());
		tracker.setY(player.getYPos());
		tracker.doTransform(new HvlAction0() {
			@Override
			public void run() {
				hvlDrawQuadc(Display.getWidth()/2, Display.getHeight()/2, 10000, 10000, new Color(70, 116, 15));
				trackGen.update(delta);
				TerrainGenerator.draw(delta);
				
				player.draw(delta);
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
				MainClient.gameFont.drawWordc("Time Until Next Race: "+(int)endTimer, 1500, 200, Color.black, 2f);

				endTimer = HvlMath.stepTowards(endTimer,  delta, 0);
				if(endTimer < 0.1) {
					HvlMenu.setCurrent(MenuManager.menuCar);
				}
			} else {
				trackTimer += delta;
				minutesElap = (int)trackTimer/60;
				secsElap = trackTimer % 60;
			}

		}
		MainClient.gameFont.drawWord(minutesElap+":"+ HvlMath.cropDecimals(secsElap, 2), 100, 100, Color.black, 2f);
		//HvlDebugUtil.drawFPSCounter(Main.gameFont, 20, 20, 1f, Color.black);
	}
}

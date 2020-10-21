package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlDebugUtil;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.painter.HvlCamera2D;
import com.osreboot.ridhvl2.HvlConfig;
import com.samuel.InfoGame;
import com.samuel.KC;
import com.samuel.Network;
import com.samuel.client.effects.CarEffect;
import com.samuel.client.effects.CarEffectApplicator;

public class Game {
	
	public static final int FRICTION = 20;
	
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
	
	static float generationTimer;
	
	public static int numAlive; 

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

	
	
	public static final boolean CAMERA_MODE = false;

	public static void initialize() {
		tracker = new HvlCamera2D(Display.getWidth()/2, Display.getHeight()/2, 0 , CAMERA_MODE ? 0.1f : 1f, HvlCamera2D.ALIGNMENT_CENTER);
		trackGen = new TrackGenerator();
		trackGen.generateTrack();
		GeneticsHandler.init();
		startTimer = 3;
		endTimer = 5;
		trackTimer = 0;
		secsElap = 0;
		generationTimer = 45;
		numAlive = GeneticsHandler.MAX_POP;
	
		
		TerrainGenerator.generateTerrain();
	}
	
	public static void update(float delta) {
		//Main.gameFont.drawWordc(currentRPM + " RPM", 600, 345,Color.white);
		
		//MARKED
		
		for(Player p : GeneticsHandler.population) {
			p.update(delta);
			if(p.isDead() && !p.dead) {
				p.die();
				numAlive--;
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			Collections.sort(GeneticsHandler.population, GeneticsHandler.compareByScore);
			HvlConfig.PJSON.save(GeneticsHandler.population.get(0).decisionNet, "championNetwork.json");
		}
		//MARKED
		generationTimer -= delta;
		if(Keyboard.isKeyDown(Keyboard.KEY_K) || generationTimer <= 0 || (GeneticsHandler.population.get(0).trackComplete && GeneticsHandler.population.get(1).trackComplete)) {
			numAlive = 0;
		}
		//MARKED
		if(numAlive == 0) {
			Collections.sort(GeneticsHandler.population, GeneticsHandler.compareByScore);
			Player par1 = GeneticsHandler.population.get(0);
			Player par2 = GeneticsHandler.population.get(1);
			for(int p = 2; p < GeneticsHandler.population.size(); p++) {
				Network.deleteNetwork(GeneticsHandler.population.get(p).decisionNet);
			}
			GeneticsHandler.population.clear();
			trackGen.borders.clear();
			trackGen.tracks.clear();
			GeneticsHandler.duplicateParents(par1, par2);
			numAlive = GeneticsHandler.population.size();
			trackGen.generateTrack();
		}
		//MARKED
		Collections.sort(GeneticsHandler.population, GeneticsHandler.compareByScore);
		tracker.setX(GeneticsHandler.population.get(0).getXPos());
		tracker.setY(GeneticsHandler.population.get(0).getYPos());
		
		tracker.doTransform(new HvlAction0() {
			@Override
			public void run() {
				if(CAMERA_MODE) {
					hvlDrawQuadc(GeneticsHandler.population.get(0).getXPos(), GeneticsHandler.population.get(0).getYPos(), 40000, 40000, new Color(70, 116, 15));

				}else {
					hvlDrawQuadc(GeneticsHandler.population.get(0).getXPos(), GeneticsHandler.population.get(0).getYPos(), 1920, 1080, new Color(70, 116, 15));
				}
				trackGen.update(delta);
				TerrainGenerator.draw(delta, GeneticsHandler.population.get(0));
				if(!CAMERA_MODE) {
					drawPlayerCars();
					
					//MARKED
					for(Player p : GeneticsHandler.population) {
						if(!p.dead) {
							p.draw(delta);
						}
					}
					//GeneticsHandler.population.get(0).draw(delta);

					
				}

			}
		});
		
		//MARKED
		GeneticsHandler.population.get(0).drawUI(delta);
		
		if(startTimer >= 0.1) {
			startTimer = HvlMath.stepTowards(startTimer,  delta, 0);
			MainClient.gameFont.drawWordc((int)startTimer + "", Display.getWidth()/2, Display.getHeight()/2 - 200, Color.black, 10f);
		} 
		else {
			//MARKED
			if(GeneticsHandler.population.get(0).trackComplete == true) {
				MainClient.gameFont.drawWordc("Your final time is: "+HvlMath.cropDecimals(GeneticsHandler.population.get(0).finalTrackTime, 2), 1500, 100, Color.black, 2.5f);
				//MainClient.gameFont.drawWordc("Time Until Next Race: "+(int)endTimer, 1500, 200, Color.black, 2f);

				endTimer -= delta;
				if(endTimer <= 0) {
					//MenuManager.menuCar.getFirstArrangerBox().getFirstOfType(HvlCheckbox.class).setChecked(false);
					//HvlMenu.setCurrent(MenuManager.menuCar);
				}
			}
			trackTimer += delta;
			minutesElap = (int)trackTimer/60;
			secsElap = trackTimer % 60;
		}
		MainClient.gameFont.drawWord((int)minutesElap+":"+ HvlMath.cropDecimals(secsElap, 2), 100, 100, Color.black, 2f);
		//MARKED
		MainClient.gameFont.drawWord("Players Alive :  "+numAlive, Display.getWidth()/2, Display.getHeight() - 150, Color.black, 1f);
		MainClient.gameFont.drawWord("Generation :  "+GeneticsHandler.currentGeneration, Display.getWidth()/2, Display.getHeight() - 100, Color.black, 1f);
		drawPlayerTimes();

		HvlDebugUtil.drawFPSCounter(MainClient.gameFont, 200, 20, 1f, Color.black);
	}
}

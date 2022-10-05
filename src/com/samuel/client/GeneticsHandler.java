package com.samuel.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl2.HvlConfig;

public class GeneticsHandler {
	public static final int MAX_POP = 10000;

	public static int currentGeneration = 1;
	public static ArrayList<Player> population;
	public static ArrayList<Player> oldPop;


	// public static Player hero;

	public static void init() {
		population = new ArrayList<>();
		
		
	}

	public static void populate(Player p) {
		population.add(p);
	}

	public static float calcFitness(Player p) {

		int finishIndex = Game.trackGen.tracks.size() - 1;
		int playerTrack = Game.trackGen.tracks.indexOf(p.closestTrack());
		if (p.trackComplete) {
			return (p.finalTrackTime) / 1000;
		} else if (!p.trackComplete && finishIndex - playerTrack == 0) {
			return 0.5f + Math.abs(HvlMath.map(
					HvlMath.distance(p.closestTrack().xPos, p.closestTrack().yPos, p.getXPos(), p.getYPos()), 0,
					1.5f * Track.TRACK_SIZE, 0f, 0.49f));
		} else {
			return (float) ((finishIndex - playerTrack) + HvlMath.distance(Game.trackGen.tracks.get(finishIndex).xPos,Game.trackGen.tracks.get(finishIndex).yPos, p.getXPos(), p.getYPos()) / 
														  HvlMath.distance(Game.trackGen.tracks.get(finishIndex).xPos,Game.trackGen.tracks.get(finishIndex).yPos, Game.trackGen.tracks.get(0).xPos,Game.trackGen.tracks.get(0).yPos));
		}
	}

	
}

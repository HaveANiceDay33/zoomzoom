package com.samuel.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl2.HvlConfig;

import NEAT.com.evo.NEAT.Environment;
import NEAT.com.evo.NEAT.Genome;
import NEAT.com.evo.NEAT.Pool;

public class GeneticsHandler implements Environment{
	
	Pool pool;
	
	int currentGeneration;
	
	public GeneticsHandler() {
		pool = new Pool();
		pool.initializePool();
		currentGeneration = 0;
	}
	
	public float calcFitness(Player p) {

		int finishIndex = Game.trackGen.tracks.size() - 1;
		int playerTrack = Game.trackGen.tracks.indexOf(p.closestTrack());
		if (p.trackComplete) {
			return 10000 - (p.finalTrackTime) / 1000;
		} else if (!p.trackComplete && finishIndex - playerTrack == 0) {
			return 10000 - 0.5f + Math.abs(HvlMath.map(
					HvlMath.distance(p.closestTrack().xPos, p.closestTrack().yPos, p.getXPos(), p.getYPos()), 0,
					1.5f * Track.TRACK_SIZE, 0f, 0.49f));
		} else {
			return 10000 - (float) ((finishIndex - playerTrack) + HvlMath.distance(Game.trackGen.tracks.get(finishIndex).xPos,Game.trackGen.tracks.get(finishIndex).yPos, p.getXPos(), p.getYPos()) / 
														  HvlMath.distance(Game.trackGen.tracks.get(finishIndex).xPos,Game.trackGen.tracks.get(finishIndex).yPos, Game.trackGen.tracks.get(0).xPos,Game.trackGen.tracks.get(0).yPos));
		}
	}

	@Override
	public void evaluateFitness(ArrayList<Genome> population) {
		for(Genome g : population) {
			g.setFitness(calcFitness(g.p));
		}
		
	}

	
}

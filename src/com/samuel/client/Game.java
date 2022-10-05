package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawLine;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlDebugUtil;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.painter.HvlCamera2D;
import com.samuel.InfoGame;
import com.samuel.KC;
import com.samuel.client.effects.CarEffect;
import com.samuel.client.effects.CarEffectApplicator;

import NEAT.com.evo.NEAT.ConnectionGene;
import NEAT.com.evo.NEAT.Genome;
import NEAT.com.evo.NEAT.NodeGene;
import NEAT.com.evo.NEAT.com.evo.NEAT.config.NEAT_Config;

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

	public static boolean showElite;

	public static GeneticsHandler gh;

	public static ArrayList<Genome> allGenes;
	static DecimalFormat df;

	public static void drawOtherPlayers(float xPos, float yPos, float turnAngle, int textureIndex, Color customColor,
			CarEffect carEffect, String userName) {
		CarEffectApplicator.drawCar(carEffect, xPos, yPos, turnAngle, textureIndex, customColor);
		MainClient.gameFont.drawWordc(userName, xPos + 1, yPos - 79, Color.black, 0.75f);
		MainClient.gameFont.drawWordc(userName, xPos, yPos - 80, customColor, 0.75f);
	}

	public static void drawPlayerCars() {
		if (MainClient.getNClient().hasValue(KC.key_GameGameInfoList())) {
			int counter = 0;
			for (String s : MainClient.getNClient().<ArrayList<String>>getValue(KC.key_GameUsernameList())) {
				if (counter != MainClient.getNClient()
						.<Integer>getValue(KC.key_PlayerListIndex(MainClient.getNUIDK()))) {
					if (MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList())
							.size() >= counter
							&& MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList())
									.get(counter) != null) {
						InfoGame info = MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList())
								.get(counter);
						Game.drawOtherPlayers(info.location.x, info.location.y, info.rotation, info.carTexture,
								info.color, info.effect, s);
					}
				}
				counter++;
			}
		}
	}

	public static void drawPlayerTimes() {
		ArrayList<InfoGame> preSort = new ArrayList<>();

		if (MainClient.getNClient().hasValue(KC.key_GameGameInfoList())) {
			int counter = 0;
			for (String s : MainClient.getNClient().<ArrayList<String>>getValue(KC.key_GameUsernameList())) {
				if (MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList()).size() >= counter
						&& MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList())
								.get(counter) != null) {
					InfoGame info = MainClient.getNClient().<ArrayList<InfoGame>>getValue(KC.key_GameGameInfoList())
							.get(counter);
					if (info.finishTime != -1f)
						preSort.add(info);
				}
				counter++;
			}
		}
		if (preSort.size() > 0) {
			ArrayList<InfoGame> postSort = new ArrayList<>();
			while (preSort.size() > 0) {
				InfoGame lowest = null;
				for (InfoGame g : new ArrayList<>(preSort)) {
					if (lowest == null || g.finishTime < lowest.finishTime)
						lowest = g;
				}
				preSort.remove(lowest);
				postSort.add(lowest);
			}

			float offset = 150f;
			for (InfoGame g : postSort) {
				offset += MenuManager.PLAYER_LIST_SPACING;
				MainClient.gameFont.drawWord(g.username + " : " + HvlMath.cropDecimals(g.finishTime, 2), 1500f, offset,
						g.color, 1.5f);
			}
		}
	}

	public static final boolean CAMERA_MODE = false;

	public static void initialize() {
		tracker = new HvlCamera2D(Display.getWidth() / 2, Display.getHeight() / 2, 0, CAMERA_MODE ? 0.1f : 1f,
				HvlCamera2D.ALIGNMENT_CENTER);
		trackGen = new TrackGenerator();
		trackGen.generateTrack();
		startTimer = 3;
		endTimer = 5;
		trackTimer = 0;
		secsElap = 0;
		generationTimer = 20;
		showElite = false;
		MultithreadingManager.init(trackGen.tracks);

		gh = new GeneticsHandler();

		allGenes = gh.pool.getAllGenome();

		df = new DecimalFormat();
		df.setMaximumFractionDigits(3);

		TerrainGenerator.generateTerrain();
	}

	public static void update(float delta) {
		// Main.gameFont.drawWordc(currentRPM + " RPM", 600, 345,Color.white);

		// MARKED
		gh.pool.evaluateFitness(gh);

		Genome top = gh.pool.getTopGenome();
		for (Genome p : allGenes) {
			p.queueJob();
		}

		MultithreadingManager.executeJobs();

		for (Genome g : allGenes) {
			g.fetchJob();
			g.p.update(delta);
			if (g.p.isDead() && !g.p.dead) {
				g.p.die();
				numAlive--;
			}
		}

		// MARKED
		generationTimer -= delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_K) || generationTimer <= 0) {
			numAlive = 0;
		}
		// MARKED
		if (numAlive == 0) {

			gh.pool.evaluateFitness(gh);

			Game.trackTimer = 0;
			gh.currentGeneration++;
			gh.pool.breedNewGeneration();
			Game.generationTimer = 20;
			allGenes = gh.pool.getAllGenome();
			numAlive = allGenes.size();

		}
		// MARKED

		if (!showElite)
			// Collections.sort(gh.pool.getAllGenome(), GeneticsHandler.compareByScore);

			tracker.setX(top.p.getXPos());
		tracker.setY(top.p.getYPos());

		tracker.doTransform(new HvlAction0() {
			@Override
			public void run() {
				if (CAMERA_MODE) {
					hvlDrawQuadc(top.p.getXPos(), top.p.getYPos(), 40000, 40000, new Color(70, 116, 15));

				} else {
					hvlDrawQuadc(top.p.getXPos(), top.p.getYPos(), 1920, 1080, new Color(70, 116, 15));
				}
				trackGen.update(delta);
				TerrainGenerator.draw(delta, top.p);
				if (!CAMERA_MODE) {
					drawPlayerCars();

					// MARKED
					if (!showElite) {
						for (Genome g : gh.pool.getAllGenome()) {
							if (!g.p.dead) {
								g.p.draw(delta);
							}
						}
					} else {
						top.p.draw(delta);
					}

				}

			}
		});

		// MARKED
		top.p.drawUI(delta);
		drawNeatNetwork(top);

		if (startTimer >= 0.1) {
			startTimer = HvlMath.stepTowards(startTimer, delta, 0);
			MainClient.gameFont.drawWordc((int) startTimer + "", Display.getWidth() / 2, Display.getHeight() / 2 - 200,
					Color.black, 10f);
		} else {
			// MARKED
			if (gh.pool.getTopGenome().p.trackComplete == true) {
				MainClient.gameFont.drawWordc("Your final time is: " + HvlMath.cropDecimals(top.p.finalTrackTime, 2),
						1500, 100, Color.black, 2.5f);
				// MainClient.gameFont.drawWordc("Time Until Next Race: "+(int)endTimer, 1500,
				// 200, Color.black, 2f);

				endTimer -= delta;
				if (endTimer <= 0) {
					// MenuManager.menuCar.getFirstArrangerBox().getFirstOfType(HvlCheckbox.class).setChecked(false);
					// HvlMenu.setCurrent(MenuManager.menuCar);
				}
			}
			trackTimer += delta;
			minutesElap = (int) trackTimer / 60;
			secsElap = trackTimer % 60;
		}
		MainClient.gameFont.drawWord((int) minutesElap + ":" + HvlMath.cropDecimals(secsElap, 2), 100, 100, Color.black,
				2f);
		// MARKED
		MainClient.gameFont.drawWord("Players Alive :  " + numAlive, Display.getWidth() / 2, Display.getHeight() - 150,
				Color.black, 1f);
		MainClient.gameFont.drawWord("Generation :  " + gh.currentGeneration, Display.getWidth() / 2,
				Display.getHeight() - 100, Color.black, 1f);
		drawPlayerTimes();

		HvlDebugUtil.drawFPSCounter(MainClient.gameFont, 200, 20, 1f, Color.black);
	}

	public static void drawNeatNetwork(Genome g) {
		TreeMap<Integer, NodeGene> nodes = g.getNodes();

		HashMap<NodeGene, HvlCoord2D> drawSpots = new HashMap<>();

		for (Integer i : nodes.keySet()) {
			if (i <= NEAT_Config.INPUTS) {
				drawSpots.put(nodes.get(i), new HvlCoord2D(320, Display.getHeight() / 2 - 320 + (i * 64)));
			} else if (i < NEAT_Config.HIDDEN_NODES) {
				drawSpots.put(nodes.get(i),
						new HvlCoord2D((float) (384 + (64 * Math.floor((i - NEAT_Config.INPUTS - 1) / 9))),
								Display.getHeight() / 2 - 320 + (64 * ((i - NEAT_Config.INPUTS - 1) % 9))));
			} else {
				drawSpots.put(nodes.get(i), new HvlCoord2D(750,
						Display.getHeight() / 2 - 168 + ((i - NEAT_Config.INPUTS - NEAT_Config.HIDDEN_NODES) * 64)));
			}
		}

		for (ConnectionGene cg : g.getConnectionGeneList()) {
			if (cg.isEnabled()) {
				hvlDrawLine(drawSpots.get(nodes.get(cg.getInto())).x, drawSpots.get(nodes.get(cg.getInto())).y,
						drawSpots.get(nodes.get(cg.getOut())).x, drawSpots.get(nodes.get(cg.getOut())).y,
						toInverseTemperature(cg.getWeight()));
			}
		}

		for (NodeGene i : drawSpots.keySet()) {
			hvlDrawQuadc(drawSpots.get(i).x, drawSpots.get(i).y, 48, 48,
					MainClient.getTexture(MainClient.NEURON_INDEX));
			MainClient.gameFont.drawWordc(String.valueOf(df.format(i.getValue())), drawSpots.get(i).x,
					drawSpots.get(i).y, Color.black, 0.5f);
		}

	}

	protected static Color toInverseTemperature(float xArg) {
		float r = HvlMath.mapl(xArg, 1f, 0f, 0f, 1f);
		float g = HvlMath.limit(1f - Math.abs(xArg), 0f, 1f);
		float b = HvlMath.mapl(xArg, -1f, 0f, 0f, 1f);
		return new Color(g, r, b, Math.abs(xArg));
	}

}

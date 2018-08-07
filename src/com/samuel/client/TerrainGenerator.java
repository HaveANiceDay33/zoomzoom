package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import org.lwjgl.opengl.Display;

import com.osreboot.ridhvl.HvlMath;

public class TerrainGenerator {
	public static int TILE_SIZE_TREE = 200;
	public static int TILE_SIZE_FLOWER = 30;

	public static int ARRAY_SIZE_TREE = 100;
	public static int ARRAY_SIZE_FLOWER = 100;

	public static int MIN_SPACE = 40;
	public static int MAX_SPACE = 150;
	public static int[] xTree = new int[ARRAY_SIZE_TREE];
	public static int[] yTree = new int[ARRAY_SIZE_TREE];
	public static int[] xFL = new int[ARRAY_SIZE_FLOWER];
	public static int[] yFL = new int[ARRAY_SIZE_FLOWER];

	public static void generateTerrain() {
		for(int i = 0; i< ARRAY_SIZE_TREE; i++) {
			xTree[i] = HvlMath.randomIntBetween(MIN_SPACE, MAX_SPACE);
			yTree[i] = HvlMath.randomIntBetween(MIN_SPACE, MAX_SPACE);
		}
		for(int i = 0; i< ARRAY_SIZE_FLOWER; i++) {
			xFL[i] = HvlMath.randomIntBetween(MIN_SPACE, MAX_SPACE);
			yFL[i] = HvlMath.randomIntBetween(MIN_SPACE, MAX_SPACE);
		}
	}
	public static void draw(float delta) {
		for(int i = 0; i < ARRAY_SIZE_TREE; i++) {
			for(int j = 0; j < ARRAY_SIZE_TREE; j++) {
				if(xTree[i] < Game.player.xPos + Display.getWidth()/2&& xTree[i] > Game.player.xPos - Display.getWidth()/2
						&& yTree[i] <  Game.player.yPos + Display.getHeight()/2&& yTree[i] > Game.player.yPos - Display.getHeight()/2) {
					hvlDrawQuadc((i*xTree[i])-4500, i*yTree[i]-4500, TILE_SIZE_TREE, TILE_SIZE_TREE, MainClient.getTexture(MainClient.TREE_INDEX));
				}
			}
		}
		for(int i = 0; i < ARRAY_SIZE_FLOWER; i++) {
				for(int j = 0; j < ARRAY_SIZE_FLOWER; j++) {
					if(xFL[i] < Game.player.xPos + Display.getWidth()/2 && xFL[i] > Game.player.xPos - Display.getWidth()/2
							&& yFL[i] <  Game.player.yPos + Display.getHeight()/2&& yFL[i] > Game.player.yPos - Display.getHeight()/2) {
					hvlDrawQuadc((i*xFL[i])-4500, i*yFL[i]-4500, TILE_SIZE_FLOWER, TILE_SIZE_FLOWER, MainClient.getTexture(MainClient.FLOWER_INDEX));
				}
			}
		}
	}
}

package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.lwjgl.opengl.Display;

public class Track {
	public float xPos;
	public float yPos;
	public int textureSelect;
	public int finishAngle;
	public float turnDirection;
	public static final int TRACK_SIZE = 250;
	public Track(float xPos, float yPos, int textureSelect, float turnDirection) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.textureSelect = textureSelect;
		this.turnDirection = turnDirection;
	}
	
	public void draw(float delta, Player p) {
		if(this.xPos < p.getXPos() + Display.getWidth()/2 + Track.TRACK_SIZE && this.xPos > p.getXPos() - Display.getWidth()/2 - Track.TRACK_SIZE
				&& this.yPos <  p.getYPos() + Display.getHeight()/2 + Track.TRACK_SIZE&& this.yPos > p.getYPos() - Display.getHeight()/2- Track.TRACK_SIZE) {

			if(textureSelect == 0 || textureSelect == 112 || textureSelect == 148) {
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.STR_ROAD_INDEX));
			}
			if(textureSelect == 2 || textureSelect == 172 || textureSelect == 160) {
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.STR_ROAD_INDEX));
			}
			if(textureSelect == 1 || textureSelect == 124 || textureSelect == 184) {
				hvlRotate(this.xPos, this.yPos,90);
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.STR_ROAD_INDEX));
				hvlResetRotation();
			}
			if(textureSelect == 3 || textureSelect == 76 || textureSelect == 200) {
				hvlRotate(this.xPos, this.yPos,90);
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.STR_ROAD_INDEX));
				hvlResetRotation();
			}
			
			if(textureSelect == 4) {
				if(Game.trackGen.tracks.get(Game.trackGen.tracks.size() - 2).textureSelect == 0) {
					finishAngle = 0;
				} else if (Game.trackGen.tracks.get(Game.trackGen.tracks.size() - 2).textureSelect == 1) {
					finishAngle = 90;
				} else if (Game.trackGen.tracks.get(Game.trackGen.tracks.size() - 2).textureSelect == 2) {
					finishAngle = 180;
				} else if (Game.trackGen.tracks.get(Game.trackGen.tracks.size() - 2).textureSelect == 3) {
					finishAngle = 270;
				}
				hvlRotate(this.xPos, this.yPos, finishAngle);
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.FINISH_INDEX));
				hvlResetRotation();
			}
			
			if(textureSelect == 100) {
				hvlRotate(this.xPos, this.yPos,90);
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
				hvlResetRotation();
			}
			if(textureSelect == 88) {
				hvlRotate(this.xPos, this.yPos,270);
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
				hvlResetRotation();
			}
			if(textureSelect == 76) {
				hvlRotate(this.xPos, this.yPos,0);
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
				hvlResetRotation();
			}
			if(textureSelect == 64) {
				hvlRotate(this.xPos, this.yPos,180);
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
				hvlResetRotation();
			}
			if(textureSelect == 52) {
				hvlRotate(this.xPos, this.yPos,0);
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
				hvlResetRotation();
			}
			if(textureSelect == 40) {
				hvlRotate(this.xPos, this.yPos,270);
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
				hvlResetRotation();
			}
			if(textureSelect == 28) {
				hvlRotate(this.xPos, this.yPos,180);
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
				hvlResetRotation();
			}
			if(textureSelect == 16) {
				hvlRotate(this.xPos, this.yPos,90);
				hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
				hvlResetRotation();
			}
		}
	}
}

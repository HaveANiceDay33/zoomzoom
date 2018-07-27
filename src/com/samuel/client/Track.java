package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

public class Track {
	public float xPos;
	public float yPos;
	public int textureSelect;
	public int finishAngle;
	public static final int TRACK_SIZE = 250;
	public Track(float xPos, float yPos, int textureSelect) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.textureSelect = textureSelect;
	}
	public void draw(float delta) {
		if(textureSelect == 0) {
			hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.STR_ROAD_INDEX));
		}
		if(textureSelect == 2) {
			hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.STR_ROAD_INDEX));
		}
		if(textureSelect == 1) {
			hvlRotate(this.xPos, this.yPos,90);
			hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.STR_ROAD_INDEX));
			hvlResetRotation();
		}
		if(textureSelect == 3) {
			hvlRotate(this.xPos, this.yPos,90);
			hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.STR_ROAD_INDEX));
			hvlResetRotation();
		}
		
		if(textureSelect == 4) {
			if(TrackGenerator.tracks.get(TrackGenerator.tracks.size() - 2).textureSelect == 0) {
				finishAngle = 0;
			} else if (TrackGenerator.tracks.get(TrackGenerator.tracks.size() - 2).textureSelect == 1) {
				finishAngle = 90;
			} else if (TrackGenerator.tracks.get(TrackGenerator.tracks.size() - 2).textureSelect == 2) {
				finishAngle = 180;
			} else if (TrackGenerator.tracks.get(TrackGenerator.tracks.size() - 2).textureSelect == 3) {
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
		if(textureSelect == 99) {
			hvlRotate(this.xPos, this.yPos,270);
			hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
			hvlResetRotation();
		}
		if(textureSelect == 98) {
			hvlRotate(this.xPos, this.yPos,0);
			hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
			hvlResetRotation();
		}
		if(textureSelect == 97) {
			hvlRotate(this.xPos, this.yPos,180);
			hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
			hvlResetRotation();
		}
		if(textureSelect == 96) {
			hvlRotate(this.xPos, this.yPos,0);
			hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
			hvlResetRotation();
		}
		if(textureSelect == 95) {
			hvlRotate(this.xPos, this.yPos,270);
			hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
			hvlResetRotation();
		}
		if(textureSelect == 94) {
			hvlRotate(this.xPos, this.yPos,180);
			hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
			hvlResetRotation();
		}
		if(textureSelect == 93) {
			hvlRotate(this.xPos, this.yPos,90);
			hvlDrawQuadc(this.xPos, this.yPos, TRACK_SIZE, TRACK_SIZE, MainClient.getTexture(MainClient.TURN_ROAD_INDEX));
			hvlResetRotation();
		}
	}
}

package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import com.samuel.Main;

public class Border {
	public float xPos, yPos, xSize, ySize;
	public static boolean hitWall;
	public Border(float xArg, float yArg, float wArg, float lArg) {
		this.xPos = xArg;
		this.yPos = yArg;
		this.xSize = wArg;
		this.ySize = lArg;
	}
	public void draw(float delta) {
		
		
		hvlDrawQuadc(this.xPos, this.yPos, this.xSize*64, this.ySize*64, 0, 0, this.xSize, this.ySize, MainClient.getTexture(MainClient.TIRE_INDEX));
		
		
		if(Player.xPos > this.xPos - this.xSize*32 && Player.xPos < this.xPos + this.xSize*32) {
			if(Player.yPos > this.yPos - this.ySize*32 && Player.yPos < this.yPos + this.ySize*32) {
				this.hitWall = true;
			} else {
				this.hitWall = false;
			}
		}
		else {
			this.hitWall = false;
		}
	}
}

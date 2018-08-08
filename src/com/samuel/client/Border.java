package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import org.newdawn.slick.Color;

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
		hvlDrawQuadc(this.xPos, this.yPos, this.xSize, this.ySize, Color.transparent);
		if(Player.xPos > this.xPos - this.xSize/2 && Player.xPos < this.xPos + this.xSize/2) {
			if(Player.yPos > this.yPos - this.ySize/2 && Player.yPos < this.yPos + this.ySize/2) {
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

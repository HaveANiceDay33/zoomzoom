package com.samuel;

import java.io.Serializable;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;

public class InfoGame implements Serializable{
	private static final long serialVersionUID = 8089596510722966271L;
	
	public HvlCoord2D location;
	public int carTexture;
	public Color color;
	public float finishTime;
	
	public InfoGame(HvlCoord2D locationArg, int carTextureArg, Color colorArg){
		location = locationArg;
		carTexture = carTextureArg;
		color = colorArg;
		finishTime = -1f;
	}
	
}

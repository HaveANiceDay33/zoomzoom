package com.samuel;

import java.io.Serializable;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;

public class InfoGame implements Serializable{
	private static final long serialVersionUID = 8089596510722966271L;
	
	public HvlCoord2D location;
	public float rotation;
	public int carTexture;
	public Color color;
	public float finishTime;
	public String username;
	
	public InfoGame(HvlCoord2D locationArg, float rotationArg, int carTextureArg, Color colorArg, String usernameArg){
		location = locationArg;
		rotation = rotationArg;
		carTexture = carTextureArg;
		color = colorArg;
		username = usernameArg;
		finishTime = -1f;
	}
	
}

package com.samuel;

import java.io.Serializable;

import org.newdawn.slick.Color;

public class InfoLobby implements Serializable{
	private static final long serialVersionUID = 2476521782706236499L;
	
	public int carTexture;
	public boolean ready;
	public Color color;
	
	public InfoLobby(int carTextureArg, boolean readyArg, Color colorArg){
		carTexture = carTextureArg;
		ready = readyArg;
		color = colorArg;
	}
	
}

package com.samuel;

import java.io.Serializable;

import com.osreboot.ridhvl.HvlCoord2D;

public class InfoGame implements Serializable{
	private static final long serialVersionUID = 8089596510722966271L;
	
	public HvlCoord2D location;
	
	public InfoGame(HvlCoord2D locationArg){
		location = locationArg;
	}
	
}

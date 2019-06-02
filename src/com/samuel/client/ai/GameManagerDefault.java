package com.samuel.client.ai;

import org.lwjgl.opengl.Display;

import com.osreboot.ridhvl.HvlCoord2D;

public class GameManagerDefault extends GameManager{

	@Override
	public HvlCoord2D getCameraLocation(){
		return new HvlCoord2D(Display.getWidth()/2, Display.getHeight()/2);
	}

}

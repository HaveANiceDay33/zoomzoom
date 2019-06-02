package com.samuel.client.ai;

import org.lwjgl.opengl.Display;

import com.osreboot.ridhvl.HvlCoord2D;
import com.samuel.client.Player;

public class GameManagerDefault extends GameManager{

	public GameManagerDefault(){
		super();
	}
	
	@Override
	public HvlCoord2D getCameraLocation(){
		return new HvlCoord2D(Display.getWidth()/2, Display.getHeight()/2);
	}

	@Override
	public void initializePlayers(boolean singlePlayer){
		players.add(new Player(new ControllerHuman()));
	}

}

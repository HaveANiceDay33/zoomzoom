package com.samuel.client.ai;

import java.util.ArrayList;

import com.osreboot.ridhvl.HvlCoord2D;
import com.samuel.client.Player;

public abstract class GameManager {

	public ArrayList<Player> players;
	
	public GameManager(){
		players = new ArrayList<>();
	}
	
	public abstract HvlCoord2D getCameraLocation();
	public abstract void initialize(boolean singlePlayer);
	public abstract void preUpdate(float delta);
	public void postUpdate(float delta){}
	public abstract Player getUIPlayer();
	public abstract boolean isOverridingGameFinishState();
	public boolean isGameFinished(){
		return false;
	}
	
}

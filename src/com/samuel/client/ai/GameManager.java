package com.samuel.client.ai;

import java.util.ArrayList;

import com.osreboot.ridhvl.HvlCoord2D;
import com.samuel.client.Player;

public abstract class GameManager {

	protected ArrayList<Player> players;
	
	public GameManager(){
		players = new ArrayList<>();
	}
	
	public abstract HvlCoord2D getCameraLocation();
	
	public abstract void initializePlayers(boolean singlePlayer);
	
}

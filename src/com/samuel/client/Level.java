package com.samuel.client;

import java.util.ArrayList;

public class Level {
	public int[] tiles;
	public String generator;
	public String [] indNum;
	public int numBorders;
	public int [] borderArgs;
	public ArrayList<Border> borders;
	public String name;
	public Level(String genArg, String nameArg, int numBorder) {
		generator = genArg;
		name = nameArg;
		indNum = generator.split("");
		tiles = new int[indNum.length];
		for(int i = 0; i < indNum.length; i++) {
			tiles[i] = Integer.parseInt(indNum[i]); 
		}
		numBorders = numBorder;
		borderArgs = new int[numBorders * 4];
	}
}

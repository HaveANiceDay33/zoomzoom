package com.samuel.client;

public class Level {
	public int[] tiles;
	public String generator;
	public String [] indNum;
	public String name;
	public Level(String genArg, String nameArg) {
		generator = genArg;
		name = nameArg;
		indNum = generator.split("");
		tiles = new int[indNum.length];
		for(int i = 0; i < indNum.length; i++) {
			tiles[i] = Integer.parseInt(indNum[i]); 
		}
	}
}

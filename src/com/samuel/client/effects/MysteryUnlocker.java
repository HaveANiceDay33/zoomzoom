package com.samuel.client.effects;

public class MysteryUnlocker {

	private static final String CODE_DRIFTER = "011220";
	
	public static CarEffect myUnlockedEffect;
	
	private static String myCode = "";
	
	public static void initialize(){
		
	}
	
	public static void enterCharacter(char characterArg){
		StringBuilder builder = new StringBuilder(myCode);
		builder.append(characterArg);
		if(builder.length() > 6) builder.deleteCharAt(0);
		myCode = builder.toString();
		
		if(myCode.equals(CODE_DRIFTER)){
			myUnlockedEffect = CarEffect.DRIFTER;
			myCode = "";
		}
	}
	
}

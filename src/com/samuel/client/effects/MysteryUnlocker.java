package com.samuel.client.effects;

public class MysteryUnlocker {

	private static final String 
	CODE_RESET = "000000",
	CODE_DRIFTER = "011220",
	CODE_SHIFTED = "220022",
	CODE_NEON = "333210";
	
	public static CarEffect myUnlockedEffect;
	
	private static String myCode = "";
	
	public static void initialize(){
		
	}
	
	public static void enterCharacter(char characterArg){
		StringBuilder builder = new StringBuilder(myCode);
		builder.append(characterArg);
		if(builder.length() > 6) builder.deleteCharAt(0);
		myCode = builder.toString();
		
		if(myCode.equals(CODE_RESET)){
			myUnlockedEffect = null;
			myCode = "";
		}else if(myCode.equals(CODE_DRIFTER)){
			myUnlockedEffect = CarEffect.DRIFTER;
			myCode = "";
		}else if(myCode.equals(CODE_SHIFTED)){
			myUnlockedEffect = CarEffect.SHIFTED;
			myCode = "";
		}else if(myCode.equals(CODE_NEON)){
			myUnlockedEffect = CarEffect.NEON;
			myCode = "";
		}
	}
	
}

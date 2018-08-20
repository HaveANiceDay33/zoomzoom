package com.samuel.client.effects;

public class MysteryUnlocker {

	private static final String CODE_RESET = "000000";

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
		}else{
			for(CarEffect e : CarEffect.values()){
				if(myCode.equals(e.code)){
					myUnlockedEffect = e;
					myCode = "";
					break;
				}
			}
		}
	}

}

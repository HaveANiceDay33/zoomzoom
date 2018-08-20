package com.samuel.client.effects;

import java.io.Serializable;

public enum CarEffect implements Serializable{

	DRIFTER("011220"), 
	SHIFTED("220022"), 
	NEON("333210"), 
	KART("444333"), 
	THORNS("542100");
	
	public String code;

	private CarEffect(String codeArg){
		code = codeArg;
	}

}

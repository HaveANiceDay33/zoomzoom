package com.samuel.client;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

public abstract class Identified implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final String uid;
	
	public Identified(Random random){
		byte[] uidBytes = new byte[16];
		random.nextBytes(uidBytes);
		uid = UUID.nameUUIDFromBytes(uidBytes).toString().replace("-", "").substring(0, 16);
	}
	
	public final boolean matches(Identified identified){
		return uid.equals(identified.uid);
	}
	
}

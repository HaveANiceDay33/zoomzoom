package com.samuel;

public class Car {
	public int GEAR_COUNT;
	public int[] maxSpeedsPerGear;
	public float TIRE_GRIP;
	public int ACCELERATION;
	public int MAX_RPM;
	public int MIN_RPM;
	public int textureSelect;
	public void setTextureSelect(int textureSelect) {
		this.textureSelect = textureSelect;
	}
	public int getGEAR_COUNT() {
		return GEAR_COUNT;
	}
	public void setMAX_RPM(int mAX_RPM) {
		MAX_RPM = mAX_RPM;
	}
	public void setMIN_RPM(int mIN_RPM) {
		MIN_RPM = mIN_RPM;
	}
	public void setGEAR_COUNT(int gEAR_COUNT) {
		GEAR_COUNT = gEAR_COUNT;
	}
	public void setTIRE_GRIP(float tIRE_GRIP) {
		TIRE_GRIP = tIRE_GRIP;
	}
	public void setACCELERATION(int aCCELERATION) {
		ACCELERATION = aCCELERATION;
	}

}

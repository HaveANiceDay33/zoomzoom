package com.samuel.cars;

import com.samuel.Car;
import com.samuel.Main;

public class AcuraNSX extends Car{
	public AcuraNSX() {
		setACCELERATION(31);
		setGEAR_COUNT(7);
		setTIRE_GRIP(0.38f);
		setMAX_RPM(7600);
		setMIN_RPM(0);
		setTextureSelect(Main.NSX_INDEX);
		maxSpeedsPerGear = new int[getGEAR_COUNT()];
		maxSpeedsPerGear[0] = 45;
		maxSpeedsPerGear[1] = 76;
		maxSpeedsPerGear[2] = 99;
		maxSpeedsPerGear[3] = 110;
		maxSpeedsPerGear[4] = 147;
		maxSpeedsPerGear[5] = 167;
		maxSpeedsPerGear[6] = 180;
	}
}

package com.samuel.client.ai;

public abstract class Controller {
	public abstract boolean isShiftingUp();
	public abstract boolean isShiftingDown();
	public abstract boolean isAccelerating();
	public abstract boolean isTurningLeft();
	public abstract boolean isTurningRight();
	public abstract boolean isBraking();
}

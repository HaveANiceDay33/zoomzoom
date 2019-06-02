package com.samuel.client;

public abstract class PlayerInput {
	public abstract boolean isShiftingUp();
	public abstract boolean isShiftingDown();
	public abstract boolean isAccelerating();
	public abstract boolean isTurningLeft();
	public abstract boolean isTurningRight();
	public abstract boolean isBraking();
}

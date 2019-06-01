package com.samuel.client;

import org.lwjgl.input.Keyboard;

public class HumanInput extends PlayerInput{

	@Override
	public boolean isShiftingUp() {
		return Keyboard.isKeyDown(Keyboard.KEY_P);
	}

	@Override
	public boolean isShiftingDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_L);
	}

	@Override
	public boolean isAccelerating() {
		return Keyboard.isKeyDown(Keyboard.KEY_W);
	}

	@Override
	public boolean isTurningLeft() {
		return Keyboard.isKeyDown(Keyboard.KEY_A);
	}

	@Override
	public boolean isTurningRight() {
		return Keyboard.isKeyDown(Keyboard.KEY_D);
	}

	@Override
	public boolean isBreaking() {
		return Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_SPACE);
	}

}

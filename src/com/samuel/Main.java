package com.samuel;
import org.lwjgl.input.Keyboard;

import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.input.HvlInput;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class Main extends HvlTemplateInteg2D{
	public static void main(String [] args){
		new Main();
	}
	public Main(){
		super(144, 1920, 1080, "Zoom Zoom", new HvlDisplayModeDefault());
	}
	static HvlFontPainter2D gameFont;
	public final static int NEEDLE_INDEX = 0;
	public final static int TACH_INDEX = 1;
	public final static int FONT_INDEX = 2;
	public final static int CIRCLE_INDEX = 3;
	public final static int WRX_INDEX = 4;
	public final static int GTI_INDEX = 5;
	public final static int NSX_INDEX = 6;
	public final static int STR_ROAD_INDEX = 7;
	public final static int TURN_ROAD_INDEX = 8;
	public final static int TREE_INDEX = 9;
	public final static int FLOWER_INDEX = 10;
	public final static int FINISH_INDEX = 11;
	public final static int TEST_TRACK_INDEX = 12;

	@Override
	public void initialize() {		
		getTextureLoader().loadResource("needle"); //0
		getTextureLoader().loadResource("tach"); //1
		getTextureLoader().loadResource("osfont");//2
		getTextureLoader().loadResource("circle");//3
		
		getTextureLoader().loadResource("wrx");//4
		getTextureLoader().loadResource("gti");//5
		getTextureLoader().loadResource("NSX");//6
		
		getTextureLoader().loadResource("StraightRoad");//7
		getTextureLoader().loadResource("fRightTurn"); //8
		getTextureLoader().loadResource("Tree");//9
		getTextureLoader().loadResource("flower");//10
		getTextureLoader().loadResource("FinishLine");//11
		getTextureLoader().loadResource("testTrack");//12

		Game.keyPresses();
		//getSoundLoader().loadResource("engineSound");//0
		gameFont =  new HvlFontPainter2D(getTexture(FONT_INDEX), HvlFontPainter2D.Preset.FP_INOFFICIAL,.18f,8f,0); //font definition
		MenuManager.initialize();

	}
	@Override
	public void update(float delta) {
		MenuManager.update(delta);
	}
}

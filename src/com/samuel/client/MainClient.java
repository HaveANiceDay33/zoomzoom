package com.samuel.client;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import com.osreboot.hvol.base.HvlGameInfo;
import com.osreboot.hvol.dclient.HvlTemplateDClient2D;
import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.input.HvlInput;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class MainClient extends HvlTemplateDClient2D{

	public MainClient(HvlGameInfo gameInfoArg){
		super(144, 1920, 1080, "Zoom Zoom", new HvlDisplayModeDefault(), "localhost", 25565, 0.05f, gameInfoArg);
	}
	static HvlFontPainter2D gameFont;
	public final static int NEEDLE_INDEX = 0;
	public final static int TACH_INDEX = 1;
	public final static int FONT_INDEX = 2;
	public final static int CIRCLE_INDEX = 3;
	public final static int WRX_INDEX = 4;
	public final static int WRX_A_INDEX = 5;
	public final static int GTI_INDEX = 6;
	public final static int GTI_A_INDEX = 7;
	public final static int NSX_INDEX = 8;
	public final static int NSX_A_INDEX = 9;
	public final static int STR_ROAD_INDEX = 10;
	public final static int TURN_ROAD_INDEX = 11;
	public final static int TREE_INDEX = 12;
	public final static int FLOWER_INDEX = 13;
	public final static int FINISH_INDEX = 14;
	public final static int TEST_TRACK_INDEX = 15;

	@Override
	public void initialize() {		
		getTextureLoader().loadResource("needle"); //0
		getTextureLoader().loadResource("tach"); //1
		getTextureLoader().loadResource("osfont");//2
		getTextureLoader().loadResource("circle");//3
		
		getTextureLoader().loadResource("wrx");//4
		getTextureLoader().loadResource("ALPHAwrx");//5
		getTextureLoader().loadResource("gti");//6
		getTextureLoader().loadResource("ALPHAgti");//7
		getTextureLoader().loadResource("NSX");//8
		getTextureLoader().loadResource("ALPHANSX");//9
		
		getTextureLoader().loadResource("StraightRoad");//10
		getTextureLoader().loadResource("fRightTurn"); //11
		getTextureLoader().loadResource("Tree");//12
		getTextureLoader().loadResource("flower");//13
		getTextureLoader().loadResource("FinishLine");//14
		getTextureLoader().loadResource("testTrack");//15
		
		Game.keyPresses();
		//getSoundLoader().loadResource("engineSound");//0
		gameFont =  new HvlFontPainter2D(getTexture(FONT_INDEX), HvlFontPainter2D.Preset.FP_INOFFICIAL,.18f,8f,0); //font definition
		MenuManager.initialize();

	}
	@Override
	public void update(float delta) {
		//gameFont.drawWord(getClient().getTable().toString(), 0, 0, Color.darkGray);
		MenuManager.update(delta);
	}
	@Override
	public void onConnection() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDisconnection() {
		// TODO Auto-generated method stub
		HvlMenu.setCurrent(MenuManager.ip);
	}
}

package com.samuel.server;

import org.newdawn.slick.Color;

import com.osreboot.hvol.base.HvlGameInfo;
import com.osreboot.hvol.base.HvlMetaServer.SocketWrapper;
import com.osreboot.hvol.dgameserver.HvlTemplateDGameServer2D;
import com.osreboot.ridhvl.display.HvlDisplayMode;
import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;

public class MainServer extends HvlTemplateDGameServer2D{

	public MainServer(HvlGameInfo gameInfoArg) {
		super(144, 1080, 720, "Zoom zoom Server", new HvlDisplayModeDefault(),"localhost" , 25565, .05f, gameInfoArg);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onConnection(SocketWrapper target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnection(SocketWrapper target) {
		// TODO Auto-generated method stub
		
	}
	static HvlFontPainter2D gameFont;
	public final static int FONT_INDEX = 0;
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		getTextureLoader().loadResource("osfont");
		
		gameFont =  new HvlFontPainter2D(getTexture(FONT_INDEX), HvlFontPainter2D.Preset.FP_INOFFICIAL,.25f,8f,0); //font definition
		
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		gameFont.drawWord(getServer().getTable().toString(), 0,  0, Color.white);
	}

}

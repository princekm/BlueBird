package com.example.scenes;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import com.example.game.SceneManager;

public class SplashScene extends Scene{
	private SceneManager manager;
	public SplashScene(SceneManager manager){		
		super();
		this.manager=manager;
		createSplashScreen();
	}	
	private void createSplashScreen(){
		setBackground(new Background(0, 0, 0));
	//	Sprite icon = new Sprite(0, 0, manager.RM.splashTR,
		//		manager.getEngine().getVertexBufferObjectManager());
//		icon.setPosition(0,0);
	//	attachChild(icon);		
	}
}

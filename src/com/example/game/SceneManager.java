package com.example.game;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.ui.activity.BaseGameActivity;

import com.example.scenes.GameScene;
import com.example.scenes.MainMenuScene;
import com.example.scenes.SplashScene;


public class SceneManager  {
	private AllScenes currentScene;
	private BaseGameActivity activity;
	private Engine engine;
	private Camera camera;
	private SplashScene  splashScene;
	private GameScene gameScene;
	private MainMenuScene menuScene;
	public ResourceManager RM;
	public SharedPrefs prefs;
	public enum AllScenes {
		SPLASH, MENU, GAME
	}

	public SceneManager(BaseGameActivity act, Engine eng, Camera cam) {
		this.activity = act;
		this.engine = eng;
		this.camera = cam;
		prefs=new SharedPrefs(activity);
		this.RM=new ResourceManager(this);
	}
	public void loadAndcreateAllScenes(){
		splashScene = new SplashScene(this);
		menuScene = new MainMenuScene(this);
		createGameScene();
	}
	public MainMenuScene getMenuScene(){
		return this.menuScene;
	}
	public void createGameScene(){
		gameScene= new GameScene(this);
	}
	public GameScene getGameScene(){
		return this.gameScene;
	}
	public BaseGameActivity getActivivty(){
		return activity;		
	}
	public Engine getEngine(){
		return engine;
	}
	public Camera getCamera(){
		return camera;
	}
	public SplashScene getSplashScene() {
		return splashScene;
	}

	public AllScenes getCurrentSceneID() {
		return currentScene;
	}

	public void setCurrentScene(AllScenes currentScene) {
		this.currentScene = currentScene;
		switch (currentScene) {
		case SPLASH:
			engine.setScene(splashScene);
			break;
		case MENU:
			engine.setScene(menuScene);
			break;
		case GAME:
			engine.setScene(gameScene);			
						break;
		default:
			break;
		}
	}


}
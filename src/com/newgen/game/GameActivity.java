package com.newgen.game;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import com.newgen.game.SceneManager.AllScenes;

public class GameActivity extends BaseGameActivity {

	private int CAMERA_HEIGHT=320, CAMERA_WIDTH = 240;
	private SceneManager sceneManager;
	private Camera mCamera;

	@Override
	public EngineOptions onCreateEngineOptions() {
	//	CAMERA_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		//CAMERA_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		
		 options.getAudioOptions().setNeedsMusic(true);
		 options.getRenderOptions().setDithering(true);
		return options;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		sceneManager = new SceneManager(this, mEngine, mCamera);
		sceneManager.RM.loadSplashResources();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {		
		sceneManager.createSplashScene();
		sceneManager.setCurrentScene(AllScenes.SPLASH);
		pOnCreateSceneCallback.onCreateSceneFinished(sceneManager
				.getSplashScene());
	}
		@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		mEngine.registerUpdateHandler(new TimerHandler(0.2f,
				new ITimerCallback() {
					public void onTimePassed(TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						sceneManager.RM.loadRemainingResources();
						sceneManager.loadMenuScene();
						sceneManager.loadGameScene();
						sceneManager.setCurrentScene(AllScenes.MENU);
						sceneManager.getSplashScene().detachSelf();
					}
				}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}
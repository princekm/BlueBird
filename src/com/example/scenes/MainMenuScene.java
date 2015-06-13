package com.example.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import android.util.Log;

import com.example.game.SceneManager;
import com.example.game.SceneManager.AllScenes;

public class MainMenuScene extends MenuScene implements
		IOnMenuItemClickListener {
	private final int MENU_PLAY = 0;
	private final int MENU_OPT = 1;
	private final float WIDTH = 480;
	private SceneManager manager;
	private Sprite gameOver;
	private Text scoreText;
	public MainMenuScene(SceneManager manager) {
		super(manager.getCamera());
		this.manager = manager;
		createMenuScene();
		gameOver = new Sprite(50,manager.getCamera().getHeight()/2+manager.RM.menuBtnPlayReg.getHeight()+10,manager.RM.gameOverRegion,
				manager.getEngine().getVertexBufferObjectManager());
		attachChild(gameOver);		
		setBackground(new Background(0, 0, 0));
		gameOver.setVisible(false);
		scoreText = new Text(20, manager.getCamera().getHeight()/2+manager.RM.menuBtnPlayReg.getHeight()+60,manager.RM.font, "HScore: 0123456789",
				manager.getEngine().getVertexBufferObjectManager());
		scoreText.setText("HScore: " + manager.prefs.getHScore());
		attachChild(scoreText);		
	}
	public void updateScore() {
		manager.getEngine().runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				scoreText.setText("HScore: " + manager.prefs.getHScore());
			}
		});
	}
	public void setGameOverVisibility(boolean visibility){
		gameOver.setVisible(visibility);
		
	}
	public void createMenuScene() {
		final IMenuItem playButton = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_PLAY, 200, 70, manager.RM.menuBtnPlayReg, manager
						.getEngine().getVertexBufferObjectManager()), 1.1f, 1);
		playButton.setPosition(WIDTH / 2 - playButton.getWidth() / 2, 100);
		addMenuItem(playButton);
		buildAnimations();
		setOnMenuItemClickListener(this);
	}
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_PLAY:
			((GameScene) manager.getGameScene()).setHandlers();
			manager.setCurrentScene(AllScenes.GAME);
			((GameScene) manager.getGameScene()).attachPlayer();
			((GameScene) manager.getGameScene()).updateScore();

			break;
		case MENU_OPT:
			break;
		}
		return false;
	}
}

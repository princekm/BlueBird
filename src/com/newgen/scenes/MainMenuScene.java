package com.newgen.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import android.util.Log;

import com.newgen.game.SceneManager;
import com.newgen.game.SceneManager.AllScenes;

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
		Sprite icon = new Sprite(0, 0, manager.RM.splashTR,
				manager.getEngine().getVertexBufferObjectManager());
		attachChild(icon);		
		createMenuScene();
		Log.d("width",""+(manager.getCamera().getWidth()-manager.RM.gameOverRegion.getWidth()/2));
		gameOver = new Sprite((manager.getCamera().getWidth()-manager.RM.gameOverRegion.getWidth())/2,manager.getCamera().getHeight()/2+manager.RM.menuBtnPlayReg.getHeight()+10,manager.RM.gameOverRegion,
				manager.getEngine().getVertexBufferObjectManager());
		attachChild(gameOver);		
		setBackground(new Background(0, 0, 0));
		gameOver.setVisible(false);
		scoreText = new Text(20, manager.getCamera().getHeight()/2+manager.RM.menuBtnPlayReg.getHeight()+60,manager.RM.font, "HScore: 0123456789",
				manager.getEngine().getVertexBufferObjectManager());
		scoreText.setColor(new Color(255,0,0));
		scoreText.setText("High Score: " + manager.prefs.getHScore());

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
				new SpriteMenuItem(MENU_PLAY,manager.RM.gameOverRegion.getWidth(),manager.RM.gameOverRegion.getHeight(), manager.RM.menuBtnPlayReg, manager
						.getEngine().getVertexBufferObjectManager()),  1f,1.1f);
		playButton.setPosition((manager.getCamera().getWidth()-manager.RM.gameOverRegion.getWidth())/2,manager.getCamera().getHeight()/2);
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
		}
		return false;
	}
}

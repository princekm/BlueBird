package com.newgen.scenes;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.newgen.game.SceneManager;
import com.newgen.game.SceneManager.AllScenes;
import com.newgen.sprites.BorderSprite;
import com.newgen.sprites.PipeSprite;
import com.newgen.sprites.PlayerSprite;

public class GameScene extends Scene implements IOnSceneTouchListener {
	private Engine engine;
	private BaseGameActivity activity;
	private Camera camera;
	private PhysicsWorld physicsWorld;
	private TimerHandler spriteTimerHandler, del, point, gameover;
	private SceneManager manager;
	private ArrayList<Sprite> pipelist;
	private Random random;
	private HUD gameHUD;
	private Text scoreText;
	private int score;
	private PlayerSprite sPlayer;
	private float ESCAPE_HEIGHT = 85, MIN_HEIGHT = 70, GRAVITY = 18;
	public boolean GAME_OVER = false;

		public GameScene(SceneManager manager) {
		super();
		this.manager = manager;
		this.activity = manager.getActivivty();
		this.camera = manager.getCamera();
		random = new Random();
		pipelist = new<PipeSprite> ArrayList();
		engine = activity.getEngine();
		createPhysicsWorld();
		setOnSceneTouchListener(this);
	//	setParallax();
		createWalls();
		createHUD();
		createContactListener();
		this.setBackground(manager.RM.autoParallaxBackground);
	}
		private void createContactListener() {
			ContactListener contactListener = new ContactListener() {
				@Override
				public void beginContact(Contact contact) {
					final Fixture x1 = contact.getFixtureA();
					final Fixture x2 = contact.getFixtureB();
					if (x1.getBody().getUserData().equals("pipe")) {
						x1.getBody().setActive(false);
						sPlayer.setZIndex(2000);
						sPlayer.getParent().sortChildren();
						sPlayer.setRotation(90);
						if(manager.RM.hitMusic!=null&&!GAME_OVER)
						manager.RM.hitMusic.play();
					} else if (x2.getBody().getUserData().equals("pipe")) {
						sPlayer.setRotation(90);
						sPlayer.setZIndex(2000);
						x2.getBody().setActive(false);
						sPlayer.getParent().sortChildren();
						if(manager.RM.hitMusic!=null&&!GAME_OVER)
							manager.RM.hitMusic.play();
					} else if (x1.getBody().getUserData().equals("top")
							|| x2.getBody().getUserData().equals("top")){
						sPlayer.setRotation(90);
						if(manager.RM.hitMusic!=null&&!GAME_OVER)
						manager.RM.hitMusic.play();

					}
					else if (x1.getBody().getUserData().equals("bottom")
							|| x2.getBody().getUserData().equals("bottom")) {
						sPlayer.setRotation(90);
						if(manager.RM.dieMusic!=null)
							manager.RM.dieMusic.play();
						gameover = new TimerHandler(0.5f, true,
								new ITimerCallback() {
									public void onTimePassed(
											TimerHandler pTimerHandler) {
										engine.unregisterUpdateHandler(gameover);
										manager.setCurrentScene(AllScenes.MENU);
										removeAllPipes();
										removePlayer();
										manager.getMenuScene().setGameOverVisibility(true);
									}
								});
						engine.registerUpdateHandler(gameover);
					}
					if (!GAME_OVER) {
						stopPipes();
						GAME_OVER = true;
						manager.prefs.checkAndSetHscore(score-1);
						manager.getMenuScene().updateScore();
						score = 0;
						removeHandlers();
					}

				}

				@Override
				public void endContact(Contact contact) {

				}

				@Override
				public void preSolve(Contact contact, Manifold oldManifold) {

				}

				@Override
				public void postSolve(Contact contact, ContactImpulse impulse) {
				}
			};
			physicsWorld.setContactListener(contactListener);
		}


	void setHandlers() {
		GAME_OVER = false;
		createNewPipes();
		createDeleteHandler();
		createUpdateScoreHandler();
		engine.registerUpdateHandler(spriteTimerHandler);
		engine.registerUpdateHandler(del);
		engine.registerUpdateHandler(point);
	}

	private void removeHandlers() {
		engine.unregisterUpdateHandler(spriteTimerHandler);
		engine.unregisterUpdateHandler(del);
		engine.unregisterUpdateHandler(point);
	}

	private void createHUD() {
		gameHUD = new HUD();
		scoreText = new Text(20, 30, manager.RM.font, "Score: 0123456789",
				this.engine.getVertexBufferObjectManager());
		scoreText.setText("Score: " + score);
		scoreText.setColor(new Color(255,0,0));
		gameHUD.attachChild(scoreText);
		camera.setHUD(gameHUD);
	}

	private void createWalls() {
		BorderSprite land = new BorderSprite(0, camera.getHeight()
				- manager.RM.landRegion.getHeight(), manager.RM.landRegion,
				engine.getVertexBufferObjectManager(), physicsWorld, "bottom");
		BorderSprite beam = new BorderSprite(0, 0, manager.RM.landRegion,
				engine.getVertexBufferObjectManager(), physicsWorld, "top");
		attachChild(land);
		attachChild(beam);
	}

	private void createUpdateScoreHandler() {
		point = new TimerHandler(2f, true, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				updateScore();
			}
		});
	}

	private void createDeleteHandler() {
		del = new TimerHandler(2f, true, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				if (manager.getCurrentSceneID() == AllScenes.GAME) {
					Sprite pipe = null;
					for (int i = 0; i < pipelist.size(); ++i) {
						pipe = pipelist.get(i);
						if (pipe.getX() + manager.RM.pipeRegion.getWidth() <0) {
							removePipe((PipeSprite) pipe);
							pipelist.remove(i);
						}
					}
				}
			}
		});
	}

	private void createNewPipes() {
		spriteTimerHandler = new TimerHandler(1.5f, true, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				if (manager.getCurrentSceneID() == AllScenes.GAME && !GAME_OVER) {
					int shift = random.nextInt((int) ((camera.getHeight() - (MIN_HEIGHT + ESCAPE_HEIGHT)) - MIN_HEIGHT));
					createParallelWalls(shift);
					if (manager.RM.pointMusic != null)
						manager.RM.pointMusic.play();
				}
			}
		});

	}

	private void createPhysicsWorld() {
		physicsWorld = new PhysicsWorld(new Vector2(0, GRAVITY), false);
		registerUpdateHandler(physicsWorld);
	}

	public void attachPlayer() {
		sPlayer = new PlayerSprite(camera.getWidth() / 2,
				camera.getHeight() / 2, manager.RM.playerTexureRegion,
				engine.getVertexBufferObjectManager(), physicsWorld);
		attachChild(sPlayer);
	
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown() && !GAME_OVER) {
			sPlayer.getBody().setLinearVelocity(new Vector2(0, -6.3f));
			if(manager.RM.flyMusic!=null)
				manager.RM.flyMusic.play();
			return true;
		}
		return false;
	}

	private void createParallelWalls(int shift) {
		PipeSprite down = new PipeSprite(camera.getWidth(), camera.getHeight()
				- MIN_HEIGHT - shift, manager.RM.pipeRegion,
				engine.getVertexBufferObjectManager(), physicsWorld);
		float rem, inv, cam = camera.getHeight();
		rem = cam - (MIN_HEIGHT + ESCAPE_HEIGHT);
		inv = manager.RM.pipeRegion.getHeight() - rem;
		PipeSprite up = new PipeSprite(camera.getWidth(), -inv - shift,
				manager.RM.pipeRegion, engine.getVertexBufferObjectManager(),
				physicsWorld);
		attachChild(up);
		attachChild(down);
		pipelist.add(up);
		pipelist.add(down);
	}


	private void removePipe(final PipeSprite pipe) {
		final PhysicsConnector physicsConnector = physicsWorld
				.getPhysicsConnectorManager().findPhysicsConnectorByShape(pipe);

		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				if (physicsConnector != null) {
					physicsWorld.unregisterPhysicsConnector(physicsConnector);
					pipe.getBody().setActive(false);
					physicsWorld.destroyBody(pipe.getBody());
					detachChild(pipe);
				}
			}
		});
	}

	private void removePlayer() {
		final PhysicsConnector physicsConnector = physicsWorld
				.getPhysicsConnectorManager().findPhysicsConnectorByShape(
						sPlayer);
		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				if (physicsConnector != null) {
					physicsWorld.unregisterPhysicsConnector(physicsConnector);
					sPlayer.getBody().setActive(false);
					physicsWorld.destroyBody(sPlayer.getBody());
					detachChild(sPlayer);
				}
			}
		});
	}

	private void removeAllPipes() {

		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				PipeSprite pipe;
				for (Sprite list : pipelist) {
					pipe = (PipeSprite) list;
					PhysicsConnector physicsConnector = physicsWorld
							.getPhysicsConnectorManager()
							.findPhysicsConnectorByShape(pipe);

					if (physicsConnector != null) {
						physicsWorld
								.unregisterPhysicsConnector(physicsConnector);
						pipe.getBody().setActive(false);
						physicsWorld.destroyBody(pipe.getBody());
						detachChild(pipe);
					}
				}
						}
		});
	}

	private void stopPipes() {
		PipeSprite pipe;
		for (Sprite list : pipelist) {
			pipe = (PipeSprite) list;
			pipe.getBody().setLinearVelocity(0, 0);
		}
	}

	public void updateScore() {
		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				scoreText.setText("Score: " + score++);
			}
		});
	}

}

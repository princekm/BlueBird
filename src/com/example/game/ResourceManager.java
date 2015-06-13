package com.example.game;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.util.Log;

public class ResourceManager {
	public BaseGameActivity activity;
	public Engine engine;
	public Camera camera;
	//game resources
	public BitmapTextureAtlas mAutoParallaxBackgroundTexture, playerTexture,
	 landTexture,pipeTexture,gameOverTexture;
	public ITextureRegion playerTexureRegion,
	landRegion,pipeRegion, mParallaxLayerBack, mParallaxLayerMid,
	mParallaxLayerFront,gameOverRegion;
	public AutoParallaxBackground autoParallaxBackground;
	public Font font;

	//splash resources
//	public BitmapTextureAtlas splashTA;
//	public ITextureRegion splashTR;
	
	//main menu resources
	public BuildableBitmapTextureAtlas menuBtnTex;
	public ITextureRegion menuBtnPlayReg;
	public Music pointMusic,hitMusic,dieMusic,flyMusic;
	public ResourceManager(SceneManager manager){
		this.activity=manager.getActivivty();		
		this.engine=activity.getEngine();
		this.camera=manager.getCamera();
		loadSplashResources();
		loadMenuResources();
		loadGameResources();
		loadMusic();
		loadMenuFonts();
	}
	
	public void loadMusic(){
		try
		{
		    pointMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager(),activity,"music/point.ogg");
		    hitMusic=MusicFactory.createMusicFromAsset(engine.getMusicManager(),activity,"music/hit.ogg");		   
		    dieMusic=MusicFactory.createMusicFromAsset(engine.getMusicManager(),activity,"music/die.ogg");
		    flyMusic=MusicFactory.createMusicFromAsset(engine.getMusicManager(),activity,"music/fly.ogg");
		}
		catch (Exception e)
		{
		
		  Log.d( "err",""+e);
		}
		
	}
	public void loadSplashResources(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
//		splashTA = new BitmapTextureAtlas(activity.getTextureManager(),
//				512,1024);
//
//		splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
//				splashTA, activity, "splash.png", 0, 0);
//		splashTA.load();		
//
	}
	public void loadMenuFonts() {
		String mFontFile = "font/lcd.ttf";
		final ITexture font_hud = new BitmapTextureAtlas(
				activity.getTextureManager(), 2048, 2048,
				TextureOptions.BILINEAR);
		font = FontFactory.createFromAsset(activity.getFontManager(), font_hud,
				activity.getAssets(), mFontFile, 30, true,
				android.graphics.Color.WHITE);
		font.load();
	}

	public void loadMenuResources(){
		gameOverTexture=new BitmapTextureAtlas(activity.getTextureManager(),
				128,32);
		gameOverRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameOverTexture, activity, "menu/gameover.png", 0, 0);
		gameOverTexture.load();
		menuBtnTex = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(),128,32);
		menuBtnPlayReg = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuBtnTex, activity.getAssets(), "menu/playgame.png");
		try {
			menuBtnTex
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			menuBtnTex.load();
		} catch (TextureAtlasBuilderException e) {
		}
	}
	public void loadGameResources(){
		playerTexture = new BitmapTextureAtlas(engine.getTextureManager(), 64,
				64);
		playerTexureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(playerTexture, activity, "game/bluebird.png", 0, 0);
		playerTexture.load();
		pipeTexture = new BitmapTextureAtlas(engine.getTextureManager(),
				64, 512);
		pipeRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(pipeTexture, activity, "game/bluepipe.png", 0,
						0);
		pipeTexture.load();		
		landTexture = new BitmapTextureAtlas(engine.getTextureManager(), 1024,
				32);
		landRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				landTexture, activity, "game/land.png", 0, 16);
		landTexture.load();
//		mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(
//				activity.getTextureManager(), 1024, 1024,
//				TextureOptions.DEFAULT);
//		mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(this.mAutoParallaxBackgroundTexture, activity,
//						"front.png", 0, 0);
//		mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(this.mAutoParallaxBackgroundTexture, activity,
//						"back.png", 0, 0);
//		mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(this.mAutoParallaxBackgroundTexture, activity,
//						"mid.png", 0, 0);
//		autoParallaxBackground = new AutoParallaxBackground(
//				0, 0, 0, 30);
//		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f,
//				new Sprite(0, 30,
//						mParallaxLayerBack, engine
//								.getVertexBufferObjectManager())));
//		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-3.0f,
//				new Sprite(0,0, this.mParallaxLayerMid, engine
//						.getVertexBufferObjectManager())));
//		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-6.0f,
//				new Sprite(0,0,
//						mParallaxLayerFront, engine
//								.getVertexBufferObjectManager())));
//		engine.getTextureManager().loadTexture(mAutoParallaxBackgroundTexture);

	}
	

}

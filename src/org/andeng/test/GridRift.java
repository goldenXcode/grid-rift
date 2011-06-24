package org.andeng.test;

import java.util.ArrayList;
import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.collision.CollisionHandler;
import org.anddev.andengine.engine.handler.collision.ICollisionCallback;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.HorizontalAlign;

import android.graphics.Color;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Isaiah Walker
 * Updated 6/23/2011
 */
public class GridRift extends BaseGameActivity implements IOnSceneTouchListener, ICollisionCallback{
        // ===========================================================
        // Constants
        // ===========================================================

        private static final int CAMERA_WIDTH = 480;
        private static final int CAMERA_HEIGHT = 720;
        private static final float PLATFORM_HEIGHT = 550.0f;
        
        // ===========================================================
        // Fields
        // ===========================================================
        
        private Scene scene;
        private Texture mTexture;
        private Texture mBackgroundTexture;
        private Texture fontTexture;
        private ChangeableText livesText;
        private Text gameOverText;
        private Font font;
        private TextureRegion mPlatform;
        private TextureRegion mBall;
        private TextureRegion mBrick;
        private TiledTextureRegion mLaser;
        private TextureRegion mLifeUp;
        private TextureRegion mDoubleLemon;
        private TextureRegion mLaserUp;
        private TextureRegion mJewel;
        private TextureRegion mBackground;
        private Ball lemon;
        private Ball lemon2;
        private Platform platform;
        private Sprite brick;
        private Camera mCamera;
        private Jewel jewel1;
        private Jewel jewel2;
        private int ballCount;
        private int lives;
        private ArrayList<IShape> stuff;
        //private ArrayList<IShape> bricks;
        private ArrayList<IShape> powerUps;
        private Random rng;
        private int numBricks;

        // ===========================================================
        // Constructors
        // ===========================================================

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================

        @Override
        public Engine onLoadEngine() {
        	//Sets the properties of the window for the game screen
            this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
            final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
    		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
    		return new Engine(engineOptions);
        }

        @Override
        public void onLoadResources() {
        	//Loads the necessary files for the game to use
        	//Sprite
        	this.mTexture = new Texture(512,256);
        	this.mPlatform = TextureRegionFactory.createFromAsset(this.mTexture, this, "PlatformSmall.png",0,0);
        	this.mBall = TextureRegionFactory.createFromAsset(this.mTexture, this, "GoodLemon.png",0,30);
        	this.mBrick = TextureRegionFactory.createFromAsset(this.mTexture, this, "Brick.png",0,50);
        	this.mLaser = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "LaserShotAni3.png",120,0,5,1);
        	this.mLifeUp = TextureRegionFactory.createFromAsset(this.mTexture, this, "Lifeup.png",0,80);
        	this.mDoubleLemon = TextureRegionFactory.createFromAsset(this.mTexture, this, "SecondBallPowerup.png",40,80);
        	this.mLaserUp = TextureRegionFactory.createFromAsset(this.mTexture, this, "LaserPowerup.png",80,80);
        	this.mJewel = TextureRegionFactory.createFromAsset(this.mTexture, this, "JewelLaser.png",120,80);
        	this.mEngine.getTextureManager().loadTexture(this.mTexture);
        	
        	//Background
        	this.mBackgroundTexture = new Texture(512, 1024);
        	this.mBackground = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "Background Swirly.png", 0, 0);
        	this.mEngine.getTextureManager().loadTexture(this.mBackgroundTexture);
        	
        	//Fonts
        	this.fontTexture = new Texture(512,512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        	this.font = FontFactory.createFromAsset(this.fontTexture, this, "Plok.ttf", 16, true, Color.WHITE);
        	this.mEngine.getTextureManager().loadTexture(this.fontTexture);
        	this.mEngine.getFontManager().loadFont(this.font);
        	
        	//Random number Generator
        	rng = new Random();
        }

        @Override
        public Scene onLoadScene() {
        	//What gets placed the game window 
        	this.scene = new Scene(1);
        	stuff = new ArrayList<IShape>();
        	powerUps = new ArrayList<IShape>();
        	lives = 3;
        	ballCount = 1;
        	numBricks = 1;
        	
        	this.mEngine.registerUpdateHandler(new FPSLogger());

    		scene.setBackgroundEnabled(false);
    		scene.getFirstChild().attachChild(new Sprite(0, 0, this.mBackground));
    		
    		scene.setOnSceneTouchListener(this);
    		
    		//The walls of the game area
    		final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
    		ground.setUserData(new String("ground"));
    		final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
    		roof.setUserData(new String("roof"));
    		final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
    		left.setUserData(new String("left"));
    		final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);
    		right.setUserData(new String("right"));
    		
    		//Adding them to the wall collision list
    		stuff.add(ground);
    		stuff.add(roof);
    		stuff.add(left);
    		stuff.add(right);
    		
    		scene.getFirstChild().attachChild(ground);
    		scene.getFirstChild().attachChild(roof);
    		scene.getFirstChild().attachChild(left);
    		scene.getFirstChild().attachChild(right);
    		
    		//Setting up Platform
    		platform = new Platform(240, PLATFORM_HEIGHT, this.mPlatform);
    		platform.setUserData(new String("platform"));
    		scene.getFirstChild().attachChild(platform);
    		
    		//Add the sprite to the collision list
    		stuff.add(platform);
    		
    		//Setting Up Ball
    		lemon = new Ball(240, PLATFORM_HEIGHT, this.mBall, lives, platform);
    		lemon.setUserData(new String("lemon"));
    		scene.getFirstChild().attachChild(lemon);
    		
    		//Setting up bricks
    		brick = new Sprite(100, 100, this.mBrick);
    		brick.setUserData(new String("brick"));
    		scene.getFirstChild().attachChild(brick);
    		
    		stuff.add(brick);
    		
    		this.mEngine.registerUpdateHandler(new CollisionHandler(this, lemon, stuff));
    		//this.mEngine.registerUpdateHandler(new CollisionHandler(this, lemon, bricks));
    		this.mEngine.registerUpdateHandler(new CollisionHandler(this, platform, powerUps));
    		
    		livesText = new ChangeableText(10f, 690f, font, "Lives: "+lives);
    		scene.getFirstChild().attachChild(livesText);
    		//Change to spawning bricks if levels doesn't exist make random brick spawn
    		//Laser Spawner
    		scene.registerUpdateHandler(new TimerHandler(5.0f, true, new ITimerCallback(){
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {
					if(platform.hasLaser()){
						final Laser laser1 = new Laser(platform.getX()-5, platform.getY(), mLaser);
						final Laser laser2 = new Laser(platform.getX()+platform.getWidth()+5, platform.getY(), mLaser);
						laser1.animate(new long[] { 200, 200, 200 }, 1, 3, true);
						laser2.animate(new long[] { 200, 200, 200 }, 1, 3, true);
						laser1.setUserData(new String("laser"));
						laser2.setUserData(new String("laser"));
						scene.getFirstChild().attachChild(laser1);
						scene.getFirstChild().attachChild(laser2);
						GridRift.this.mEngine.registerUpdateHandler(new CollisionHandler(GridRift.this, laser1, stuff));
						GridRift.this.mEngine.registerUpdateHandler(new CollisionHandler(GridRift.this, laser2, stuff));
						platform.decreaseDuration();
					}
					if(!platform.hasLaser()){
						if(jewel1 != null){
							GridRift.this.runOnUpdateThread(new Runnable() {
			                    @Override
			                    public void run() {
			                    	scene.getFirstChild().detachChild(jewel1);
			                    	scene.getFirstChild().detachChild(jewel2);
			                    }
							});//End UpdateThread
						}
					}
				}
    			
    		}));
    		
    		scene.registerUpdateHandler(new TimerHandler(5.0f, true, new ITimerCallback(){
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {
					if (numBricks < 15){
						final Sprite brick1 = new Sprite(rng.nextInt(481), rng.nextInt(361), GridRift.this.mBrick);
			    		brick1.setUserData(new String("brick"));
			    		scene.getFirstChild().attachChild(brick1);
			    		stuff.add(brick1);
			    		numBricks++;
					}
				}
    			
    		}));
    		
    		//Game over text
    		this.gameOverText = new Text(0, 0, this.font, "Game\nOver", HorizontalAlign.CENTER);
    		this.gameOverText.setPosition((CAMERA_WIDTH - this.gameOverText.getWidth()) * 0.5f, (CAMERA_HEIGHT - this.gameOverText.getHeight()) * 0.5f);
    		this.gameOverText.registerEntityModifier(new ScaleModifier(3, 0.1f, 2.0f));
    		this.gameOverText.registerEntityModifier(new RotationModifier(3, 0, 720));
    		
            return scene;    
        }

        @Override
        public void onLoadComplete() {
        	
        }

		@Override
		public boolean onSceneTouchEvent(final Scene pScene,final TouchEvent pSceneTouchEvent) {
		
			if(pSceneTouchEvent.isActionDown()) {
				this.platform.setTargetX(pSceneTouchEvent.getX());
				return true;
			}
			else if(pSceneTouchEvent.isActionMove()){
				this.platform.setTargetX(pSceneTouchEvent.getX());
				return true;
			}
			else if(pSceneTouchEvent.isActionUp()){
				if(lemon.isOnPlatform()){
					lemon.launch();
				}
				if(lemon2 != null){
					if(lemon2.isOnPlatform()){
						lemon2.launch();
					}
				}
			}
			return false;
		}

        // ===========================================================
        // Methods
        // ===========================================================
		
		public void movePlatform(float x){
			platform.setPosition(x - platform.getWidth()/2, platform.getY());
			
		}
		
		public void spawnPowerUp(float x, float y){
			int type = rng.nextInt(3);
			switch(type){
				case 0:
					final PowerUp life = new PowerUp(x, y, mLifeUp);
					life.setUserData(new String("lifeUp"));
					powerUps.add(life);
					scene.getFirstChild().attachChild(life);
					break;
				case 1:
					final PowerUp laser = new PowerUp(x, y, mLaserUp);
					laser.setUserData(new String("laserUp"));
					powerUps.add(laser);
					scene.getFirstChild().attachChild(laser);
					break;
				case 2:
					final PowerUp twoball = new PowerUp(x, y, mDoubleLemon);
					twoball.setUserData(new String("ballUp"));
					powerUps.add(twoball);
					scene.getFirstChild().attachChild(twoball);
					break;
			}
		}

		@Override
		public boolean onCollision(final IShape pCheckShape,final IShape pTargetShape) {
			//Figure out what the ball collided with
			//If it was a wall negate the correct direction
			String check;
			String target;
			check = (String) pCheckShape.getUserData();
			target = (String) pTargetShape.getUserData();
			
			if(check.compareTo("lemon") == 0 | check.compareTo("lemon2") == 0){//Ball Block
				final Ball ball = (Ball) pCheckShape;
				if(target.compareTo("left") == 0 | target.compareTo("right") == 0){
					ball.xShift();
					return false;
				}//End of left and right block
				else if(target.compareTo("roof") == 0){
					ball.yShift();
					return false;
				}//End of roof block
				else if(target.compareTo("ground") == 0){ //Hit the ground ball stops
					if(lives < 0){ //Game over
						scene.getFirstChild().attachChild(this.gameOverText);
					}
					else if(ballCount < 2){
						ball.die();
						lives--;
						livesText.setText("Lives: "+lives);
					}
					else{
						ballCount--;
						this.runOnUpdateThread(new Runnable() {
		                    @Override
		                    public void run() {
		                    	scene.getFirstChild().detachChild(pCheckShape);
		                    	GridRift.this.mEngine.unregisterUpdateHandler(new CollisionHandler(GridRift.this, ball, stuff));
		                    }
						});//End UpdateThread
					}
					return false;
				}//End of Ground Block
				else if(target.compareTo("platform") == 0){//If it hits anything else figure out bounce based of off angles, still needs a little work in some special case...
					float targetx = (pTargetShape.getX()-pTargetShape.getWidth()/2);
					float targety = (pTargetShape.getY()-pTargetShape.getHeight()/2);
					float ballx = (ball.getX()-ball.getWidth()/2);
					float bally = (ball.getY()-ball.getHeight()/2);
					final Vector2 angleVector = new Vector2((ballx-targetx),(bally-targety));
					double angle = Math.atan(angleVector.y/angleVector.x);
					if(angle > 0.245 | angle < -0.245){
						ball.xShift();
					}
					else{
						ball.yShift();
					}
					return false;
				}//End of platform block
				else if(target.compareTo("brick") == 0){
					float targetx = (pTargetShape.getX()-pTargetShape.getWidth()/2);
					float targety = (pTargetShape.getY()-pTargetShape.getHeight()/2);
					float ballx = (ball.getX()-ball.getWidth()/2);
					float bally = (ball.getY()-ball.getHeight()/2);
					final Vector2 angleVector = new Vector2((ballx-targetx),(bally-targety));
					double angle = Math.atan(angleVector.y/angleVector.x);
					if(angle > 0.464 | angle < -0.464){
						ball.xShift();
					}
					else{
						ball.yShift();
					}
					if (rng.nextInt(101) < 10){
						spawnPowerUp(pTargetShape.getX(), pTargetShape.getY());
					}
					this.runOnUpdateThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	scene.getFirstChild().detachChild(pTargetShape);
	                    	stuff.remove(pTargetShape);
	                    	
	                    }
					});//End UpdateThread
					numBricks--;
					return false;
				}//End of Brick block
			}//End of Ball Block
			else if(check.compareTo("laser") == 0){//Laser Block
				if(target.compareTo("brick") == 0){
					if (rng.nextInt(101) < 10){
						spawnPowerUp(pTargetShape.getX(), pTargetShape.getY());
					}
					this.runOnUpdateThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	scene.getFirstChild().detachChild(pTargetShape);
	                    	stuff.remove(pTargetShape);
	                    }
					});//End UpdateThread
					this.runOnUpdateThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	scene.getFirstChild().detachChild(pCheckShape);
	                    	stuff.remove(pCheckShape);
	                    }
					});//End UpdateThread
					numBricks--;
				}//End Brick Block
				else if(target.compareTo("roof") == 0){
					this.runOnUpdateThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	scene.getFirstChild().detachChild(pCheckShape);
	                    	stuff.remove(pCheckShape);
	                    }
					});//End UpdateThread
				}//End Roof Block
				return false;
			}//End of laser Block
			else if(check.compareTo("platform")==0){//Platform Block
				if(target.compareTo("laserUp")==0){
					platform.turnOnLaser();
					jewel1 = new Jewel(240, 240, mJewel, platform, "left");
					jewel2 = new Jewel(240, 240, mJewel, platform, "right");
					scene.getFirstChild().attachChild(jewel1);
					scene.getFirstChild().attachChild(jewel2);
					this.runOnUpdateThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	scene.getFirstChild().detachChild(pTargetShape);
	                    	powerUps.remove(pTargetShape);
	                    }
					});//End UpdateThread
					return false;
				}
				else if(target.compareTo("lifeUp")==0){
					lives++;
					livesText.setText("Lives: "+lives);
					this.runOnUpdateThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	scene.getFirstChild().detachChild(pTargetShape);
	                    	powerUps.remove(pTargetShape);
	                    }
					});//End UpdateThread
					return false;
				}
				else if(target.compareTo("ballUp")==0){
					if(ballCount < 2){
						lemon2 = new Ball(240, PLATFORM_HEIGHT, this.mBall, 1, platform);
			    		lemon2.setUserData(new String("lemon2"));
			    		scene.getFirstChild().attachChild(lemon2);
			    		GridRift.this.mEngine.registerUpdateHandler(new CollisionHandler(GridRift.this, lemon2, stuff));
			    		ballCount ++;
					}
		    		this.runOnUpdateThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	scene.getFirstChild().detachChild(pTargetShape);
	                    	powerUps.remove(pTargetShape);
	                    }
					});//End UpdateThread
		    		return false;
				}
			}//End of platform block
			
			return false;
		}
		
        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================
		
}



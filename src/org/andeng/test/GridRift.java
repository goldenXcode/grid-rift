package org.andeng.test;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.collision.CollisionHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Isaiah Walker
 * Updated 6/12/2011
 */
public class GridRift extends BaseGameActivity implements IOnSceneTouchListener{
        // ===========================================================
        // Constants
        // ===========================================================

        private static final int CAMERA_WIDTH = 480;
        private static final int CAMERA_HEIGHT = 720;
        private static final float PLATFORM_HEIGHT = 550.0f;
        
        // ===========================================================
        // Fields
        // ===========================================================

        private Texture mTexture;
        private TextureRegion mPlatform;
        private TextureRegion mBall;
        private Ball lemon;
        private Sprite platform_sprite;
        private Camera mCamera;
        private int ballCount;
        private ArrayList<IShape> walls;

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
        	this.mTexture = new Texture(128,128);
        	this.mPlatform = TextureRegionFactory.createFromAsset(this.mTexture, this, "brick_tile.png",0,0);
        	this.mBall = TextureRegionFactory.createFromAsset(this.mTexture, this, "Small Lemon.png",0,32);
        	this.mEngine.getTextureManager().loadTexture(this.mTexture);
        }

        @Override
        public Scene onLoadScene() {
        	//What gets placed the game window 
        	final Scene scene = new Scene(1);
        	walls = new ArrayList<IShape>();
        	
        	this.mEngine.registerUpdateHandler(new FPSLogger());

    		scene.setBackground(new ColorBackground(0, 0, 0));
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
    		walls.add(ground);
    		walls.add(roof);
    		walls.add(left);
    		walls.add(right);
    		
    		scene.getFirstChild().attachChild(ground);
    		scene.getFirstChild().attachChild(roof);
    		scene.getFirstChild().attachChild(left);
    		scene.getFirstChild().attachChild(right);
    		
    		//Setting up Platform
    		platform_sprite = new Sprite(240, PLATFORM_HEIGHT, this.mPlatform);
    		platform_sprite.setUserData(new String("platform"));
    		scene.getFirstChild().attachChild(platform_sprite);
    		
    		//Setting Up Ball
    		lemon = new Ball(370, 250, this.mBall);
    		scene.getFirstChild().attachChild(lemon);
    		lemon.setVelocity(new Vector2(-2, 2));
    		
    		this.mEngine.registerUpdateHandler(new CollisionHandler(lemon, lemon, platform_sprite));
    		this.mEngine.registerUpdateHandler(new CollisionHandler(lemon, lemon, walls));
    		
            return scene;    
        }

        @Override
        public void onLoadComplete() {
        	
        }

		@Override
		public boolean onSceneTouchEvent(final Scene pScene,final TouchEvent pSceneTouchEvent) {
		
			if(pSceneTouchEvent.isActionDown()) {
				this.movePlatform(pSceneTouchEvent.getX());
				return true;
			}
			else if(pSceneTouchEvent.isActionMove()){
				this.movePlatform(pSceneTouchEvent.getX());
				return true;
			}
		
			return false;
		}

        // ===========================================================
        // Methods
        // ===========================================================
		
		public void movePlatform(float x){
			platform_sprite.setPosition(x - platform_sprite.getWidth()/2, platform_sprite.getY());
			
		}
		
        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================
		
}

package org.andeng.test;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class Helloworld extends BaseGameActivity implements IOnSceneTouchListener{
        // ===========================================================
        // Constants
        // ===========================================================

        private static final int CAMERA_WIDTH = 480;
        private static final int CAMERA_HEIGHT = 720;
        
        private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 1f, 0f);

        // ===========================================================
        // Fields
        // ===========================================================

        private Texture mTexture;
        private TextureRegion mPlatform;
        private TextureRegion mBall;
        private Sprite ball_sprite;
        private Sprite platform_sprite;
        private float platform_targetx;
        private Camera mCamera;
        private PhysicsWorld mPhysicsWorld;
        private int ballCount;

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
                this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
                final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
        		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
        		return new Engine(engineOptions);
        }

        @Override
        public void onLoadResources() {
        	this.mTexture = new Texture(128,128);
        	this.mPlatform = TextureRegionFactory.createFromAsset(this.mTexture, this, "brick_tile.png",0,0);
        	this.mBall = TextureRegionFactory.createFromAsset(this.mTexture, this, "Guts Man.png",0,32);
        	this.mEngine.getTextureManager().loadTexture(this.mTexture);
        }

        @Override
        public Scene onLoadScene() {
        	final Scene scene = new Scene(1);
        	
        	this.mEngine.registerUpdateHandler(new FPSLogger());

    		scene.setBackground(new ColorBackground(0, 0, 0));
    		scene.setOnSceneTouchListener(this);

    		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);

    		final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
    		final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
    		final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
    		final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

    		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
    		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
    		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
    		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
    		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

    		scene.getFirstChild().attachChild(ground);
    		scene.getFirstChild().attachChild(roof);
    		scene.getFirstChild().attachChild(left);
    		scene.getFirstChild().attachChild(right);
    		
    		//this.mPaddle.setTexturePosition(240, 680);
    		//Setting up Platform
    		platform_targetx = 240/32;
    		platform_sprite = new Sprite(240, 500, this.mPlatform);
    		final Body platform_body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, platform_sprite, BodyType.DynamicBody, FIXTURE_DEF);
    		final PhysicsConnector platform_connector = new PhysicsConnector(platform_sprite, platform_body, true, true);
    		platform_body.setFixedRotation(true);
    		MassData md = new MassData();
    		md = platform_body.getMassData();
    		md.mass = 30;
    		platform_body.setMassData(md);
    		scene.getFirstChild().attachChild(platform_sprite);
    		this.mPhysicsWorld.registerPhysicsConnector(platform_connector);
    		
    		//Setting Up Ball
    		ball_sprite = new Sprite(240, 300, this.mBall);
    		final Body ball_body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, ball_sprite, BodyType.DynamicBody, FIXTURE_DEF);
    		final PhysicsConnector ball_connector = new PhysicsConnector(ball_sprite, ball_body, true, true);
    		scene.getFirstChild().attachChild(ball_sprite);
    		this.mPhysicsWorld.registerPhysicsConnector(ball_connector);
    		ball_body.setFixedRotation(true);
    		ball_body.setLinearVelocity(new Vector2(2f, 150f));
    		
    		//this.movePlatform();
    		scene.registerUpdateHandler(this.mPhysicsWorld);
    		/*
    		scene.registerUpdateHandler(new TimerHandler(0.1f, true, new ITimerCallback() {
    			@Override
    			public void onTimePassed(final TimerHandler pTimerHandler) {
    				Helloworld.this.movePlatform();
    			}
    		}));
    		*/
            return scene;    
        }

        @Override
        public void onLoadComplete() {
        	this.movePlatform();
        }

		@Override
		public boolean onSceneTouchEvent(final Scene pScene,final TouchEvent pSceneTouchEvent) {
			if(this.mPhysicsWorld != null) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setTargetx(pSceneTouchEvent.getX());
					this.movePlatform();
					return true;
				}
				else if(pSceneTouchEvent.isActionMove()){
					this.setTargetx(pSceneTouchEvent.getX());
					this.movePlatform();
					return true;
				}
			}
			return false;
		}

        // ===========================================================
        // Methods
        // ===========================================================
		
		public void movePlatform(){
			final Body body = this.mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(platform_sprite);
			Vector2 pos = body.getWorldCenter();
			
			Debug.d("Target: " + platform_targetx + "Block: " + pos.x);
			pos.x = platform_targetx;
			body.setTransform(pos, 0);
			
			/*
			if ((int)platform_targetx == (int)pos.x){
				body.setLinearVelocity(new Vector2(0, 0));
			}
			else if(platform_targetx < pos.x){
				body.setLinearVelocity(new Vector2(-50f, 0));
			}
			else if(platform_targetx > pos.x){
				body.setLinearVelocity(new Vector2(50f, 0));
			}
			*/
			
			
			/*
			if(pos.x < pX/32.0f){
				body.setLinearVelocity(new Vector2(5f, 0));
			}
			else{
				body.setLinearVelocity(new Vector2(-5f, 0));
			}
			*/		
			
			//this.mPaddle.setTexturePosition((int)pX, (int)pY);
		}
		
		public void setTargetx(float x){
			platform_targetx = x/32f;
		}
		
        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================
}

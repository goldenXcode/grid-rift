package org.andeng.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.graphics.Color;

public class LevelEditor extends BaseGameActivity implements IOnSceneTouchListener {

	private static final int CAMERA_WIDTH = 480;
    private static final int CAMERA_HEIGHT = 720;
    private static final int BRICK_WIDTH = 60;
    private static final int BRICK_HEIGHT = 30;
    private static final int ROWS = (CAMERA_HEIGHT)/(2*BRICK_HEIGHT);
    private static final int COLS = CAMERA_WIDTH/(BRICK_WIDTH/2);
	
    private int[][] brick_grid = new int[ROWS][COLS];
	
	private String hashString = new String("");
    private Texture mTexture;
    private TextureRegion mBrick;
    private TextureRegion exportButton;
    private TextureRegion importButton;
    private Sprite exp_bu;
    private Sprite imp_bu;
    
    private HashMap<String, Sprite> brick_map = new HashMap<String, Sprite>();
    
    private Camera mCamera;
    //private ArrayList<IShape> walls;
	//private Texture fontTexture;
	//private Font font;
	private Level l = new Level("default.xml");
	
	@Override
	public Engine onLoadEngine() {
		// TODO Auto-generated method stub
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub
    	//Loads the necessary files for the game to use
    	this.mTexture = new Texture(128,128);
    	this.mEngine.getTextureManager().loadTexture(this.mTexture);
    	this.mBrick = TextureRegionFactory.createFromAsset(this.mTexture, this, "Brick.png",0,0);
    	this.exportButton = TextureRegionFactory.createFromAsset(this.mTexture, this, "export.jpg",0,0);
    	this.importButton = TextureRegionFactory.createFromAsset(this.mTexture, this, "import.jpg",0,0);
    	
    	//this.fontTexture = new Texture(512,512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	//this.font = FontFactory.createFromAsset(this.fontTexture, this, "Plok.ttf", 16, true, Color.WHITE);
    	//this.mEngine.getTextureManager().loadTexture(this.fontTexture);
    	//this.mEngine.getFontManager().loadFont(this.font);
	}

	@Override
	public Scene onLoadScene() {
		// TODO Auto-generated method stub 
    	final Scene scene = new Scene(1);
    	this.mEngine.registerUpdateHandler(new FPSLogger());

		scene.setBackground(new ColorBackground(0, 0, 0));
    	scene.setOnSceneTouchListener(this);
    	scene.setTouchAreaBindingEnabled(true);
    	
    	exp_bu = new Sprite(CAMERA_WIDTH/4, 5*CAMERA_HEIGHT/6, this.exportButton)//* 
    	{
    		@Override
    		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
    			final float pTouchAreaLocalX, final float pTouchAreaLocalY){
    				if( pTouchAreaLocalX < 180 && pTouchAreaLocalX > 120 
    						&& pTouchAreaLocalY < 660 && pTouchAreaLocalY > 600 ){
    					Debug.d("EXPORTING!");
    					return true;
    				}
    			return false;
    		}
    	};//*/
    	exp_bu.setUserData("exporter");
    	scene.registerTouchArea(exp_bu);
    	
    	scene.getFirstChild().attachChild(exp_bu);
    	
		return scene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		

		/*else if(imp_bu.onAreaTouched(pSceneTouchEvent, pSceneTouchEvent.getX(), pSceneTouchEvent.getY()))
		{
			Debug.d("Import Touched (" + pSceneTouchEvent.getX() + ", " + pSceneTouchEvent.getY() + ")");
			
		}*/
		if(pSceneTouchEvent.isActionDown()){
			hashString = "";
			Debug.d("(" + pSceneTouchEvent.getX() + ", " + pSceneTouchEvent.getY() + ")");
			
			int x_floor = (int)((pSceneTouchEvent.getX())/BRICK_WIDTH)*BRICK_WIDTH;
			int y_floor = (int)((pSceneTouchEvent.getY())/BRICK_HEIGHT)*BRICK_HEIGHT;
			int temp_y = (x_floor/(BRICK_WIDTH/2));
			int temp_x = (y_floor + 2)/BRICK_HEIGHT;
			if(y_floor + 2 > CAMERA_HEIGHT/2){
				y_floor = CAMERA_HEIGHT/2 - 2;
			}
			if(exp_bu.onAreaTouched(pSceneTouchEvent, pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
				Debug.d("EXPORTING!");
				if(export())
					return true;
			}
			
			Debug.d("(" + pSceneTouchEvent.getX() + ", " + pSceneTouchEvent.getY() + ")"
					+ "\nRounded:(" + x_floor + ", " + y_floor+ ") [" + temp_x + "][" + temp_y + "]");
			
			
			
			if(temp_x < 0 || temp_y < 0 || temp_x > ROWS-1 || temp_y > COLS-1){
				return false;
			}
			else{
				if(temp_y + 1 > COLS+1){
					return false;
				}
				//Matrix navigation and whatnot.
				if(brick_grid[temp_x][temp_y] == 0){
						if(brick_grid[temp_x][temp_y+1] == 0){
							brick_grid[temp_x][temp_y] = 1;
							brick_grid[temp_x][temp_y+1] = 2;
							hashString = x_floor + "" +  y_floor;
						}
						else if(temp_y-1 >= 0){
							brick_grid[temp_x][temp_y] = 2;
							brick_grid[temp_x][temp_y-1] = 1;
							x_floor -= BRICK_WIDTH;
							hashString = (x_floor) + "" +  y_floor;
						}
						else
							return false;
					/* Add coordinates (x_floor, y_floor) 
					 * */
					l.add(hashString, x_floor, y_floor);
					brick_map.put(hashString, new Sprite(x_floor, y_floor, BRICK_WIDTH, BRICK_HEIGHT, this.mBrick));
					this.mEngine.getScene().getFirstChild().attachChild(brick_map.get(hashString));    
				}
				else{
					if(brick_grid[temp_x][temp_y] == 1){
						brick_grid[temp_x][temp_y] = 0;
						brick_grid[temp_x][temp_y+1] = 0;
						hashString = x_floor + "" +  y_floor;
					}
					else if(temp_y-1 >= 0 && brick_grid[temp_x][temp_y] == 2){
						brick_grid[temp_x][temp_y] = 0;
						brick_grid[temp_x][temp_y-1] = 0;
						x_floor -= BRICK_WIDTH;
						hashString = (x_floor) + "" +  y_floor;
					}
					else
						return false;
					
					runOnUpdateThread(new Runnable() {
			            public void run() {
			                    
			            	pScene.getLastChild().detachChild(brick_map.remove(hashString));
			            }
			        });
					l.remove(hashString);
				}
				return true;
			}
					//*/
		}
		return false;
	}
	
	
	
	private boolean export(){
		if(l.serializePrep()){
			Serializer serializer = new Persister();
			File result = new File("/sdcard/default.xml");
			result.setWritable(true);
			try {
				serializer.write(l, result);
				Debug.d("Export complete.");
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Debug.d("Export failed due to exception");
				return false;
			}
		}
		Debug.d("Export failed, list empty.");
		return false;
	}
}

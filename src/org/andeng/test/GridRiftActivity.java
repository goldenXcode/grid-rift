package org.andeng.test;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GridRiftActivity extends BaseGameActivity{
	
	private static final int CAMERA_WIDTH = 480;
    private static final int CAMERA_HEIGHT = 720;
	private static final int MENU1 = Menu.FIRST;
	private static final int MENU2 = Menu.FIRST+1;
    
    private Scene scene;
	private Texture mBackgroundTexture;
	private TextureRegion mBackground;
	private Camera mCamera;
	
	
	
    /** Called when the activity is first created. */
    
    @Override
    public void onLoadResources(){
    	 this.mBackgroundTexture = new Texture(512, 1024);
         this.mBackground = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "MenuBackground.png", 0, 0);
         this.mEngine.getTextureManager().loadTexture(this.mBackgroundTexture);
    }

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
            engineOptions.getTouchOptions().setRunOnUpdateThread(true);
            return new Engine(engineOptions);
	}

	@Override
	public Scene onLoadScene() {
		this.scene = new Scene(1);
		scene.getFirstChild().attachChild(new Sprite(0, 0, this.mBackground));		
		return scene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0,MENU1, 0, "New Game");
		menu.add(0, MENU2, 0, "Quit");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case MENU1:
			this.startActivity(new Intent(this, GridRift.class));
			return true;
		case MENU2:
			finish();
			return true;
		}
		return false;
	}
}
package org.andeng.test;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Jewel extends Sprite{

	private String side;
	private Sprite platform;
	
	Jewel(float x, float y, TextureRegion texture, Sprite platform, String side){
		super(x, y, texture);
		this.platform = platform;
		this.side = side; 
	}

	private void move(){
		if(side.compareTo("left") == 0){
			this.setPosition(platform.getX()-5,platform.getY()-platform.getHeight()/2-2);
		}
		else{
			this.setPosition(platform.getX()+platform.getWidth()-5,platform.getY()-platform.getHeight()/2-2);
		}
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed){
		this.move();		
		super.onManagedUpdate(pSecondsElapsed);
	}
}

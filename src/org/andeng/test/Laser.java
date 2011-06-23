package org.andeng.test;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Laser extends AnimatedSprite {
	
	Laser(float x, float y, TiledTextureRegion mLaser) {
		super(x, y, mLaser);
	}
	
	private void move(){
		this.setPosition(this.mX, this.mY - 10);
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed){
		this.move();		
		super.onManagedUpdate(pSecondsElapsed);
	}
}

package org.andeng.test;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class PowerUp extends Sprite {
	
	PowerUp(float x, float y, TextureRegion texture) {
		super(x, y, texture);
	}
	
	private void move(){
		this.setPosition(this.mX, this.mY + 6);
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed){
		this.move();		
		super.onManagedUpdate(pSecondsElapsed);
	}
	
}

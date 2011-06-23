package org.andeng.test;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Platform extends Sprite {
	
	private int laserDuration;
	private boolean laser;
	private float targetX;
	
	Platform(float x, float y,final TextureRegion texture) {
		super(x, y, texture);
		this.laser = false;
		this.laserDuration = 0;
	}
	
	public void move(float x){
		this.setPosition(targetX-this.getWidth()/2, this.mY);
	}
	
	public void setTargetX(float x){
		this.targetX = x;
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed){
		this.move(targetX);
		if(laserDuration < 0){
			this.laser = false;
		}
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	public boolean hasLaser(){
		return laser;
	}
	
	public void turnOnLaser(){
		this.laserDuration = 10;
		this.laser = true;
	}
	
	public void decreaseDuration(){
		this.laserDuration -= 5;
	}

}

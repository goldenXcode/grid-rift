package org.andeng.test;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import com.badlogic.gdx.math.Vector2;

public class Ball extends Sprite {
	
	private Vector2 velocity;
	private boolean alive;
	private boolean onPlatform;
	private Sprite platform;
	private int lives;
	
	Ball(float x, float y, TextureRegion texture, int lives, Sprite platform){
		super(x, y, texture);
		this.velocity = new Vector2(0,0);
		this.alive = true;
		this.lives = lives;
		this.onPlatform = true;
		this.platform = platform;
	}

	public void move() {
		if(onPlatform){
			this.setPosition(platform.getX()+platform.getWidth()/2-10,platform.getY()-platform.getHeight()/2-2);
		}
		else if(alive){
			this.setPosition(this.getX()+velocity.x, this.getY()+velocity.y);
		}
		else{
			lives--;
			
			if(lives > 0){
				this.alive = true;
				this.reset();
			}
		}
	}
	
	public void launch(){
		this.onPlatform = false;
		this.setVelocity(8, 8);
	}
	
	public void die(){
		this.lives--;
		this.setVelocity(0, 0);
		this.onPlatform = true;
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed){
		this.move();
		super.onManagedUpdate(pSecondsElapsed);
	}
	
		public void xShift(){
		velocity.x = -1 * velocity.x;
	}
	
	public void yShift(){
		velocity.y = -1 * velocity.y;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getLives() {
		return lives;
	}
	
	public void increaseLives(){
		this.lives++;
	}
	
	public void setVelocity(float x, float y) {
		this.velocity.x = x;
		this.velocity.y = y;
	}

	public Vector2 getVelocity() {
		return velocity;
	}
	
	public boolean isOnPlatform(){
		return onPlatform;
	}
	
}

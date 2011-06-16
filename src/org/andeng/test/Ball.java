package org.andeng.test;

import org.anddev.andengine.engine.handler.collision.ICollisionCallback;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import com.badlogic.gdx.math.Vector2;

public class Ball extends Sprite implements ICollisionCallback{
	
	private Vector2 velocity;
	private String data;
	private Vector2 angleVector;
	private boolean alive;
	private boolean onPlatform;
	private int lives;
	
	Ball(float x, float y, TextureRegion texture, int lives, Sprite platform){
		super(x, y, texture);
		this.velocity = new Vector2(0,0);
		this.alive = true;
		this.lives = lives;
		this.onPlatform = true;
		this.setParent(platform);
	}

	public void move() {
		if(onPlatform){
			this.setPosition(this.getParent());
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
		this.setVelocity(new Vector2(-8, 8));
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed){
		this.move();
		super.onManagedUpdate(pSecondsElapsed);
	}

	@Override
	public boolean onCollision(IShape pCheckShape, IShape pTargetShape) {
		//Figure out what the ball collided with
		//If it was a wall negate the correct direction
		data = (String) pTargetShape.getUserData();
		if(data.compareTo("left") == 0 | data.compareTo("right") == 0){
			velocity.x = -1 * velocity.x;
			return false;
		}
		else if(data.compareTo("roof") == 0){
			velocity.y = -1 * velocity.y;
			return false;
		}
		else if(data.compareTo("ground") == 0){ //Hit the ground ball stops
			//To-DO game over stuff
			this.alive = false;
			return false;
		}
		else if(data.compareTo("platform") == 0){//If it hits anything else figure out bounce based of off angles, still needs a little work in some special case...
			float targetx = (pTargetShape.getX()-pTargetShape.getWidth()/2);
			float targety = (pTargetShape.getY()-pTargetShape.getHeight()/2);
			float ballx = (this.getX()-this.getWidth()/2);
			float bally = (this.getY()-this.getHeight()/2);
			this.angleVector = new Vector2((ballx-targetx),(bally-targety));
			double angle = Math.atan(angleVector.y/angleVector.x);
			if(angle > 0.245 | angle < -0.245){
				velocity.x = -1 * velocity.x;
			}
			else{
				velocity.y = -1 * velocity.y;
			}
			//Debug.d("Angle Vector: " + angleVector + " Angle: " + angle*(180/Math.PI));
			//Debug.d("Upper Cutoff: " + 0.245*(180/Math.PI) + " Lower Cutoff" + -0.245*(180/Math.PI));
			return true;
		}
		
		return false;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getLives() {
		return lives;
	}
	
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Vector2 getVelocity() {
		return velocity;
	}
	
}

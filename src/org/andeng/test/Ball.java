package org.andeng.test;

import org.anddev.andengine.engine.handler.collision.ICollisionCallback;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.Debug;

import com.badlogic.gdx.math.Vector2;

public class Ball extends Sprite implements ICollisionCallback{
	
	private Vector2 velocity;
	private String data;
	private Vector2 angleVector;
	
	Ball(float x, float y, TextureRegion texture){
		super(x, y, texture);
		velocity = new Vector2(0,0);
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Vector2 getVelocity() {
		return velocity;
	}
	
	public void move(){
		this.setPosition(this.getX()+velocity.x, this.getY()+velocity.y);
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
			velocity.x = 0;
			velocity.y = 0;
			return false;
		}
		else{//If it hits anything else figure out bounce based of off angles, still needs a little work in some special case...
			this.angleVector = new Vector2(pTargetShape.getX()-this.getX(), pTargetShape.getY()-this.getY());
			double angle = Math.atan(angleVector.y/angleVector.x);
			if(angle > Math.PI/4 | angle < -(Math.PI/4)){
				velocity.y = -1 * velocity.y;
			}
			else{
				velocity.x = -1 * velocity.x;
			}
			Debug.d("Angle Vector: " + angleVector + " Angle: " + angle*(180/Math.PI));
			return false;
		}
	}
}
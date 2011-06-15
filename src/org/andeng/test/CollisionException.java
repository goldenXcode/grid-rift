package org.andeng.test;

public class CollisionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private char direction;
	
	CollisionException(char dir){
		super();
		this.direction = dir;
	}

	public char getDirection() {
		return direction;
	}

}

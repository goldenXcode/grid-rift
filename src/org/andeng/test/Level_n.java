/**
 * 
 */
package org.andeng.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.anddev.andengine.util.Debug;
import org.simpleframework.xml.*;

/**
 * @author Calvin Lee
 *
 */
@Root
public class Level {

	private HashMap<String, Location> brick_locs = new HashMap<String, Location>();
	
	@Attribute (name = "lvl_name")
	private String f_name;
	
	@ElementList
	private LinkedList<Location> b_list = new LinkedList<Location>();
	
	public Level(String name){
		f_name = name;
	}
	
	public String getName(){
		return f_name;
	}
	
	public List<Location> getBricks(){
		return b_list;
	}
	
	public boolean serializePrep(){
		if(!b_list.isEmpty()){
			b_list.clear();
		}
		if(brick_locs.isEmpty())
			return false;
		else
			b_list.addAll(brick_locs.values());
		return true;
	}
	
	public void add(String hashcode, int x, int y){
		brick_locs.put(hashcode, new Location(x, y));
		Debug.d("Added: (" + x +"," + y+ ")");
	}
	
	public void remove(String hashcode){
		if(!brick_locs.isEmpty()){
			brick_locs.remove(hashcode);
			Debug.d("Removed");
		}
	}
	
	public boolean isEmpty(){
		return brick_locs.values().isEmpty();
	}
@Root (name = "brick")
public class Location {
		
		@Element (name = "x")
		private int loc_x;
		
		@Element (name = "y")
		private int loc_y;
		
		public Location(int x, int y){
			loc_x = x;
			loc_y = y;
		}
		
		public int getX(){
			return loc_x;
		}
		
		public int getY(){
			return loc_y;
		}
}
}

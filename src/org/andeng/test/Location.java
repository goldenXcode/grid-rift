package org.andeng.test;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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

package data;

import java.util.ArrayList;

public class PlayerStats {

	private int health;
	private ArrayList<String> items;
	
	public PlayerStats(){
		this.health = 92;
		this.items = new ArrayList<String>();
		init();
	}
	
	public void init(){
		// Add 8 crabby pattys
		for (int i = 0; i < 8; i++){
			items.add("crabby patty");
		}
	}
	
	public void removeItem(){
		items.remove(items.size()-1);
	}
	
	public void decrementHealth(){
		this.health -= 1;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
}

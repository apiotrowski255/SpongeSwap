package data;

import java.util.ArrayList;

public class PlayerStats {

	private final int MAXHEALTH = 92;
	private int health;
	private ArrayList<String> items;
	
	public PlayerStats(){
		this.health = MAXHEALTH;
		this.items = new ArrayList<String>();
		init();
	}
	
	public void init(){
		// Add 8 crabby pattys
		for (int i = 0; i < 4; i++){
			items.add("crabby patty");
		}
	}
	
	public ArrayList<String> getItems(){
		return this.items;
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
		this.health = Math.min(health, MAXHEALTH);
	}
	
	public void modifyItems(String string, int index){
		items.remove(index);
		items.add(index, string);
	}
}

package entities;

import renderEngine.DisplayManager;

public abstract class Entity{

	public float x, y;
	
	public Entity(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public abstract void render();

	public abstract void update();
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public boolean isOutOfBounds(){
		if(x >= DisplayManager.getWidth() || x <= 0 || y >= DisplayManager.getHeight() || y <= 0){
			return true;
		}
		return false;
	}

	public void setSpeed(float speed) {
		//needs to exist for heritence purposes
		
	}
	
}

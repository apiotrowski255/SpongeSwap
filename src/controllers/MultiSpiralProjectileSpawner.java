package controllers;

import java.util.ArrayList;

public class MultiSpiralProjectileSpawner {

	public ArrayList<SingleSpiralProjectileSpawner> SpiralProjectileSpawners;
	
	float x, y, speed, direction;
	public int projectileSize;
	public MultiSpiralProjectileSpawner(float x, float y, float speed, float direction, float projectileSpeed, float startAngle, float angleDifference, float delay, int leaves, int projectileSize){
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.direction = direction;
		
		
		this.SpiralProjectileSpawners = new ArrayList<SingleSpiralProjectileSpawner>();
		
		float angle = (float) (2*Math.PI / leaves);
		this.projectileSize = projectileSize;
		int i = 0;
		while (i < leaves){
			SpiralProjectileSpawners.add(new SingleSpiralProjectileSpawner(x, y, speed, direction, startAngle + angle*i, angleDifference, delay, projectileSize));
			i += 1;
		}
	}

	
	public void update(){
		for (SingleSpiralProjectileSpawner spawner : SpiralProjectileSpawners){
			spawner.update();
		}
		
		x += (float) (speed * Math.cos(direction));
		y += (float) (speed * Math.sin(direction));
	}
	
	public boolean outOfBounds(){
		if (x < 0 || x > 1280 || y < 0 || y > 960){
			return true;
		}
		return false;
	}
	
	public ArrayList<SingleSpiralProjectileSpawner> getSpawners(){
		return this.SpiralProjectileSpawners;
	}
	
	public int getNumberOfSingleSpawners(){
		return this.SpiralProjectileSpawners.size();
	}
}

package controllers;

import java.util.ArrayList;

import engineTester.Clock;
import entities.Projectile;

public class SingleExplosionProjectileSpawner {

	public float x, y, delay;
	public int numberOfProjectiles, projectileSize;
	public ArrayList<Projectile> projectiles;
	public boolean shoot;
	
	public SingleExplosionProjectileSpawner(float x, float y, float delay, int numberOfProjectiles, int projectileSize){
		this.x = x;
		this.y = y;
		this.delay = delay;
		this.numberOfProjectiles = numberOfProjectiles;
		this.projectileSize = projectileSize;
		this.projectiles = new ArrayList<Projectile>();
		this.shoot = false;
	}
	
	public void update(){
		
		if (delay < 0 && shoot == false){
			activate();
			this.shoot = true;
			
		} else {
			delay -= Clock.Delta();
		}
		
		for (Projectile p : projectiles){
			p.update();
			p.render();
		}
	}
	
	public void activate(){
		float angleDiff = (float) (2 * Math.PI / numberOfProjectiles);
		int i = 0;
		while (i < numberOfProjectiles){
			shootProjectile(x, y, 2, angleDiff * i, projectileSize);
			i += 1;
		}
	}
	
	public void shootProjectile(float x, float y, float speed, float angle, int size){
		Projectile p = new Projectile(x, y, speed, angle, size, (float) 0.0);			// last argument is gravity
		projectiles.add(p);
	}
	
	public ArrayList<Projectile> getProjectiles(){
		return this.projectiles;
	}
	
}

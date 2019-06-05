package entities;

public class Camera {

	/*
	 * Camera has static variable,
	 *  within the program there should be only one camera
	 */
	
	private static float x;
	private static float y;
	
	public Camera(float x, float y){
		this.x = x;
		this.y = y;
	}

	public static float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public static float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	
}

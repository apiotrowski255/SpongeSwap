package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import data.PlayerStats;
import engineTester.Clock;
import renderEngine.DisplayManager;
import shapes.Shapes;

public class Player extends Entity {

	private final int DOWN = 0;
	private final int LEFT = 1;
	private final int UP = 2;
	private final int RIGHT = 3;

	public final int PLAYMODE = 0;
	public final int MENUMODE = 1;
	public final int GAMEOVER = 2;

	public int speed, size, gravityDirection;
	public float direction;
	public Texture texture;
	// bounds the player to an area of movement
	public float xMinBound, xMaxBound, yMinBound, yMaxBound;
	
	public Vector3f color;
	public float gravity;
	public boolean inAir, falling;
	public int mode;
	public float timeDelay;
	
	public PlayerStats stats;
	private int extention;
	
	private boolean hide;

	public Player(int x, int y, int size) {
		super(x, y);
		this.size = size;
		this.speed = 2;
		this.texture = Shapes.LoadTexture("res/heart.png", "PNG");
		resetBounds();
		this.color = new Vector3f(0, 0, 1);
		this.gravity = 0;
		this.inAir = false;
		this.falling = false;
		this.gravityDirection = DOWN;
		this.mode = MENUMODE;
		this.stats = new PlayerStats();
		this.timeDelay = 0;
		this.extention = 0;
		this.hide = false;
	}
	
	public void init(){
		this.timeDelay = 0;
	}

	public void render() {
		if (hide){
			return;
		}
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(color.x, color.y, color.z);

		Shapes.DrawQuadTexRot(texture, super.getX(), super.getY(), size + extention, size, gravityDirection * 90);
	}

	
	public void update() {

		// Clamp the player within the bounds of the window
		super.setX(clamp(super.getX(), xMinBound, xMaxBound));
		super.setY(clamp(super.getY(), yMinBound, yMaxBound));

		if (this.mode == PLAYMODE) {

			if (this.getColor().equals(new Vector3f(1, 0, 0))) {			// When player color is red
				redMovement();
			} else if (this.getColor().equals(new Vector3f(0, 0, 1))) {		// When player color is blue
				blueMovement(gravityDirection);
				
				applyGravity(gravityDirection);								// Apply gravity

				if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
					setGravityDirection(UP);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
					setGravityDirection(DOWN);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
					setGravityDirection(LEFT);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
					setGravityDirection(RIGHT);
				}
			}
		} else if (this.mode == MENUMODE){
			return;
			// handled by the menu class, (in the menu class it sets the position of the player)
		} else if (this.mode == GAMEOVER){
			System.out.println(this.size);
			if (timeDelay > 20){
				timeDelay = 0;
				setTexture("broken_heart");
				this.extention = 25;
				System.out.println("Insert death animation here");
			}
			
			timeDelay += Clock.Delta();
			
			return;
			// run the death animation
			// Heart -> broken heart -> 5 random pieces
		}

	}

	public void applyGravity(int gravityDirection) {
		if (gravityDirection == DOWN) {
			if (super.getY() <= yMaxBound) {
				super.setY(super.getY() + gravity);
				gravity += 0.02;
				inAir = true;
				if (super.getY() >= yMaxBound) {
					gravity = 0;
					inAir = false;
					falling = false;
				}
			}
		} else if (gravityDirection == LEFT) {
			if (super.getX() >= xMinBound) {
				super.setX(super.getX() - gravity);
				gravity += 0.02;
				inAir = true;
				if (super.getX() <= xMinBound) {
					gravity = 0;
					inAir = false;
					falling = false;
				}
			}
		} else if (gravityDirection == UP) {
			if (super.getY() >= yMinBound) {
				super.setY(super.getY() - gravity);
				gravity += 0.02;
				inAir = true;
				if (super.getY() <= yMinBound) {
					gravity = 0;
					inAir = false;
					falling = false;
				}
			}
		} else if (gravityDirection == RIGHT) {
			if (super.getX() <= xMaxBound) {
				super.setX(super.getX() + gravity);
				gravity += 0.02;
				inAir = true;
				if (super.getX() >= xMaxBound) {
					gravity = 0;
					inAir = false;
					falling = false;
				}
			}
		}
	}

	public void redMovement() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveUp();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveDown();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			moveLeft();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			moveRight();
		}
	}

	public void blueMovement(int gravityDirection) {
		if (gravityDirection == DOWN) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W) && falling == false) {
				super.setY(super.getY() - speed);
				inAir = true;
				falling = false;
			} else if (falling == false && inAir == true) {
				falling = true;
				if (gravity > speed) {
					gravity = gravity - speed;
				} else {
					gravity = 0;
				}
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				moveLeft();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				moveRight();
			}
		} else if (gravityDirection == LEFT) {
			if (Keyboard.isKeyDown(Keyboard.KEY_D) && falling == false) {
				super.setX(super.getX() + speed);
				inAir = true;
				falling = false;
			} else if (falling == false && inAir == true) {
				falling = true;
				if (gravity > speed) {
					gravity = gravity - speed;
				} else {
					gravity = 0;
				}
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				moveUp();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				moveDown();
			}
		} else if (gravityDirection == UP) {
			if (Keyboard.isKeyDown(Keyboard.KEY_S) && falling == false) {
				super.setY(super.getY() + speed);
				inAir = true;
				falling = false;
			} else if (falling == false && inAir == true) {
				falling = true;
				if (gravity > speed) {
					gravity = gravity - speed;
				} else {
					gravity = 0;
				}
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				moveLeft();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				moveRight();
			}
		} else if (gravityDirection == RIGHT) {
			if (Keyboard.isKeyDown(Keyboard.KEY_A) && falling == false) {
				super.setX(super.getX() - speed);
				inAir = true;
				falling = false;
			} else if (falling == false && inAir == true) {
				falling = true;
				if (gravity > speed) {
					gravity = gravity - speed;
				} else {
					gravity = 0;
				}
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				moveUp();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				moveDown();
			}
		}
	}

	public float clamp(float variable, float min, float max) {
		if (variable < min)
			return min;
		else if (variable > max)
			return max;
		else
			return variable;
	}

	public void setGravityDirection(int gravity_direction) {
		if (gravity_direction >= 0 && gravity_direction <= 3) {
			this.gravityDirection = gravity_direction;
		} else {
			System.out.println("gravity direction needs to be a int between 0 and 3 inclusive");
		}
	}
	
	public void resetBounds(){
		this.xMinBound = 0;
		this.xMaxBound = DisplayManager.getWidth() - size;
		this.yMinBound = 0;
		this.yMaxBound = DisplayManager.getHeight() - size;
	}

	public void moveLeft() {
		if (super.getX() > xMinBound) {
			super.setX(super.getX() - speed);
		}
	}

	public void moveRight() {
		if (super.getX() < xMaxBound) {
			super.setX(super.getX() + speed);
		}
	}

	public void moveUp() {
		if (super.getY() > yMinBound) {
			super.setY(super.getY() - speed);
		}
	}

	public void moveDown() {
		if (super.getY() < yMaxBound) {
			super.setY(super.getY() + speed);
		}
	}

	public void setTexture(String texture){
		this.texture = Shapes.LoadTexture("res/" + texture + ".png", "PNG");
	}
	
	public float getxMinBound() {
		return xMinBound;
	}

	public void setxMinBound(float xMinBound) {
		this.xMinBound = xMinBound;
	}

	public float getxMaxBound() {
		return xMaxBound;
	}

	public void setxMaxBound(float xMaxBound) {
		this.xMaxBound = xMaxBound;
	}

	public float getyMinBound() {
		return yMinBound;
	}

	public void setyMinBound(float yMinBound) {
		this.yMinBound = yMinBound;
	}

	public float getyMaxBound() {
		return yMaxBound;
	}

	public void setyMaxBound(float yMaxBound) {
		this.yMaxBound = yMaxBound;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public int getGravityDirection() {
		return gravityDirection;
	}

	public void setPlayMode() {
		this.mode = PLAYMODE;
	}

	public void setMenuMode() {
		this.mode = MENUMODE;
	}
	
	public void decrementHealth(){
		this.stats.decrementHealth();
	}
	
	public int getHealth(){
		return this.stats.getHealth();
	}
	
	public void setHealth(int health){
		this.stats.setHealth(health);
	}

	public void setGameOverMode() {
		this.mode = GAMEOVER;
	}
	
	public boolean getHideStatus(){
		return this.hide;
	}
	
	public void setHide(boolean value){
		this.hide = value;
	}
	
	public void hide(){
		this.hide = true;
	}
	
	public void show(){
		this.hide = false;
	}
	
}

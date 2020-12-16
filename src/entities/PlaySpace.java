package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import renderEngine.DisplayManager;
import shapes.Shapes;

public class PlaySpace extends Entity{

	// player is contained in the playSpace
	// In the playSpace the player can dodge the bullets
	
	public int height, width, strokeWidth;
	public Player player;
	
	// New values for variables if a transition is invoked.
	public float Nx, Ny, Nheight, Nwidth;	
	public float xSpeed, ySpeed, heightSpeed, widthSpeed;
	public float delta;
	
	public int counter;
	
	public PlaySpace(float x, float y, int height, int width, Player player){
		super(x, y);
		this.height = height;
		this.width = width;
		this.player = player;
		this.Nx = 0;
		this.Ny = 0;
		this.Nheight = 0;
		this.Nwidth = 0;
		this.xSpeed = 1;
		this.ySpeed = 1;
		this.heightSpeed = 1;
		this.widthSpeed = 1;
		this.delta = 0.01f;
		this.strokeWidth = 10;
		this.counter = 0;
	}
	
	public void render(){
		GL11.glDisable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		Shapes.drawBox(x, y, width, height, strokeWidth);
	}
	
	public void update(){
		transition();
		
		if (player.mode == player.MENUMODE){
			return;
		}
		
		setPlayerBounds();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
			invokeTransition(DisplayManager.getWidth()/2 - 200/2, 550, 200, 200, 10);
			System.out.println("Pressed");
		}

	}
	
	public void centerPlayerPosition(){
		player.setX(x + width/2 - player.size/2);
		player.setY(y + height/2 - player.size/2);
	}
	
	
	public void setPlayerBounds(){
		player.setxMaxBound(super.getX() + width - player.size - 10);
		player.setxMinBound(super.getX() + 10);
		player.setyMaxBound(super.getY() + height - player.size - 10 - 10/2);
		player.setyMinBound(super.getY() + 10/2);
	}
	
	
	public void invokeTransition(float x, float y, int height, int width, int time){
		
		/*
		// Instantly set the new boundaries
		super.setX(x);
		super.setY(y);
		this.height = height;
		this.width = width;
		*/
		
		float oldx = this.x;
		float oldy = this.y;
		float oldheight = this.height;
		float oldwidth = this.width;
		Nx = x;
		Ny = y;
		Nheight = height;
		Nwidth = width;
		xSpeed = (oldx - x)/time;
		ySpeed = (oldy - y)/time;
		heightSpeed = (oldheight - height)/time;
		widthSpeed = (oldwidth - width)/time;
		counter = 4*time; 						// needs to be multiplied by 4 since there are 4 factors - x, y, width and height that need to be changed
	}
	
	public void TextMode(){
		invokeTransition(32, 500, 250, 1216, 50);
	}
	
	// changes the shape of the PlaySpace over time
	public void transition(){
		xTransition();
		yTransition();
		heightTransition();
		widthTransition();
	}
	
	public void xTransition(){
		if (counter > 0){
			super.setX((float) (super.getX() - xSpeed));
			counter -= 1;
		}
	}
	
	public void yTransition(){
		if (counter > 0){
			super.setY((float) (super.getY() - ySpeed));
			counter -= 1;
		}
	}
	
	public void heightTransition(){
		if (counter > 0){
			height -= heightSpeed;
			counter -= 1;
		}
	}
	
	public void widthTransition(){
		if (counter > 0){
			width -= widthSpeed;
			counter -= 1;
		}
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	
}

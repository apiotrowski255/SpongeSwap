package engineTester;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import audio.AudioMaster;
import audio.Source;
import entities.Background;
import entities.Camera;
import entities.Entity;
import entities.PlaySpace;
import entities.Player;
import entities.Projectile;
import renderEngine.DisplayManager;
import shapes.Shapes;
import turnController.MasterTurnController;


public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
			
		Camera camera = new Camera(0,0);
		
		//Sound setup
		AudioMaster.init();
		
		int buffer = AudioMaster.loadSound("audio/soundtrack.wav");
		Source source = new Source();
		// Play music
		// source.play(buffer);
		
		
		MasterTurnController turnController = new MasterTurnController();
		
		int x = 1280;
		int y = 960; 
		
		
		
		while (!Display.isCloseRequested()) {
			
			GL11.glClearColor(0,0,0,1);				// It actually makes the images a bit sharper
			
			DisplayManager.updateDisplay();
			
			
			//GL11.glLoadIdentity();
			//GL11.glTranslatef(camera.getX(), camera.getY(), 0);
			
			
			/*
			for (Entity entity:entities){
				entity.update();
				entity.render();
			}*/
			Clock.update();
			turnController.update();
			
			
			
			//entity_controller(entities);
			//draw_lines(entities);
			
			
			
			
			
			// Sort of Debugging
			//Shapes.draw_grid();
			//System.out.println(entities.size());
			
			//Game Controllers
			//menu.update();
		}

		//Sound cleanup
		source.delete();
		AudioMaster.cleanUp();
		
		DisplayManager.closeDisplay();

	}
	

	

	
	public static void draw_lines(ArrayList<Entity> entities){
		Player player = (Player) entities.get(1);
		
		for (int i = 2; i < entities.size(); i++){
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(1, 1, 1);
			Shapes.draw_line(player.getX() + 16, player.getY()+16, entities.get(i).getX()+8, entities.get(i).getY()+8);
		}
	}
	
}

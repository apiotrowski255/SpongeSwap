package engineTester;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import UI.GameMenu;
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
		
		MasterTurnController turnController = new MasterTurnController();
		
		int x = 1280;
		int y = 960; 
		
		GameMenu gameMenu = new GameMenu(0,0); 
		
		while (!Display.isCloseRequested()) {
			
			GL11.glClearColor(0,0,0,1);				// It actually makes the images a bit sharper
			
			DisplayManager.updateDisplay();
			
			
			//GL11.glLoadIdentity();
			//GL11.glTranslatef(camera.getX(), camera.getY(), 0);
			
			Clock.update();

			if (gameMenu.playerRequestToPlay()){
				turnController.update();
			} else if (gameMenu.playerRequestToClose()){
				return;
			} else {
				gameMenu.update();
				gameMenu.render();
			}

		}

		//Sound cleanup
		//source.delete();
		AudioMaster.cleanUp();
		
		DisplayManager.closeDisplay();

	}
	
}

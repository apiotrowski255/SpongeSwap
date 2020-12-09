package UI;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import audio.AudioMaster;
import audio.Source;
import engineTester.Clock;
import entities.Entity;
import shapes.Shapes;

public class GameMenu extends Entity{

	ArrayList<Button> menu_options;
	public int selected;
	
	public float delay, timeSinceLastPress;
	
	// Audio
	int Attackbuffer, genericMenuSelectBuffer;
	public Source source;
	private mode mode;
	
	enum mode {
		MENU,
		PLAY
	}
	
	public GameMenu(float x, float y) {
		super(x, y);
		this.selected = 0;
		this.menu_options = new ArrayList<Button>();
		this.menu_options.add(new Button(100, 100, "play bar"));
		this.menu_options.add(new Button(100, 400, "exit bar"));
		
		this.timeSinceLastPress = 0;
		this.delay = 2f;
		
		this.genericMenuSelectBuffer = AudioMaster.loadSound("audio/generic menu selection.wav");
		this.source = new Source();
		this.mode = mode.MENU;
	}

	@Override
	public void render() {
		for (Entity button : menu_options) {
			button.render();
		}
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			changeSelect(-1, menu_options.size());
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			changeSelect(1, menu_options.size());
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
			this.mode = mode.PLAY;
		}
		
		timeSinceLastPress += Clock.Delta();
	}

	public void changeSelect(int direction, int size) {
		if (timeSinceLastPress > delay) {
			selected += direction;
			if (selected > size - 1) {
				selected -= size;
			}
			if (selected < 0) {
				selected += size;
			}
			timeSinceLastPress = 0;
			source.play(genericMenuSelectBuffer);
		}
		
	}
	
	public Boolean playerRequestToPlay(){
		return this.mode == mode.PLAY;
	}
}

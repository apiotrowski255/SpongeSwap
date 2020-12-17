package UI;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
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
	int Attackbuffer, genericMenuSelectBuffer, MenuMusicBuffer;
	public Source source, MenuMusicSource;
	private mode mode;
	
	private Texture texture;
	
	enum mode {
		MENU,
		PLAY
	}
	
	public GameMenu(float x, float y) {
		super(x, y);
		this.selected = 0;
		this.menu_options = new ArrayList<Button>();
		this.menu_options.add(new Button(512, 300, "play bar"));
		this.menu_options.add(new Button(512, 500, "exit bar"));
		
		this.timeSinceLastPress = 0;
		this.delay = 2f;
		
		this.genericMenuSelectBuffer = AudioMaster.loadSound("audio/generic menu selection.wav");
		this.source = new Source();
		
		this.MenuMusicBuffer = AudioMaster.loadSound("audio/Back Once Again.wav");
		this.MenuMusicSource = new Source();
		this.mode = mode.MENU;
		
		MenuMusicSource.play(MenuMusicBuffer);
		
		
		this.texture = Shapes.LoadTexture("res/spongespin title.png", "PNG");
	}

	@Override
	public void render() {
		for (Entity button : menu_options) {
			button.render();
		}
		
		GL11.glEnable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		Shapes.DrawQuadTex(texture, 100, 100, 500, 100);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			changeSelect(-1, menu_options.size());
			MenuMusicSource.unmute();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			changeSelect(1, menu_options.size());
			MenuMusicSource.mute();
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

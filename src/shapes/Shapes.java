package shapes;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.io.InputStream;

public class Shapes {

	// Draws shapes
	// Color must be specified beforehand
	
	public static void draw_line(float x1, float y1, float x2, float y2){
		glBegin(GL_LINES);
		glVertex2d(x1, y1);
		glVertex2d(x2, y2);
		glEnd();
	}
	
	
	// draws a quad that has fill
	public static void draw_quad(float x, float y, int width, int height){
		glBegin(GL_QUADS);
		glVertex2d(x, y);
		glVertex2d(x + width, y);
		glVertex2d(x + width, y + height);
		glVertex2d(x, y + height);
		glEnd();
	}
	
	public static void draw_square(float x, float y, int size){
		draw_quad(x, y, size, size);
	}
	
	public static void draw_grid(){
		GL11.glColor3f(1, 1, 1);
		int limit = 640; // DisplayManager.getWidth()
		for (int i = 0; i <= limit; i += 32){
			draw_line(0, i, limit, i);
			draw_line(i, 0, i, limit);
		}
	}
	
	public static void drawBox(float x, float y, int width, int height, int strokeWidth){
		GL11.glLineWidth(strokeWidth);
		draw_line(x, y, x+width, y);
		draw_line(x+width-strokeWidth/2, y-strokeWidth/2, x+width-strokeWidth/2, y+height-strokeWidth/2);
		draw_line(x+width-strokeWidth, y+height-strokeWidth, x, y+height-strokeWidth);
		draw_line(x+strokeWidth/2, y+height-strokeWidth, x+strokeWidth/2, y);
		GL11.glLineWidth(1);									// Reset back to default
	}
	
	public static void DrawQuadTex(Texture tex, float x, float y, float width, float height){
		tex.bind();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glLoadIdentity();
		glEnd();
		glLoadIdentity();
	}
	
	public static Texture LoadTexture(String path, String fileType){
		Texture tex = null;
		InputStream in = ResourceLoader.getResourceAsStream(path);
		try {
			tex = TextureLoader.getTexture(fileType, in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tex;
	}
	
	public static void DrawQuadTexRot(Texture tex, float x, float y, float width, float height, float angle){
		tex.bind();
		glTranslatef(x + width/2, y + height/2, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(-width/2, -height/2, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glEnd();
		glLoadIdentity();
	}
	
}

package game;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Block {
	
	public final static int RIGHT = 0, TOP = 1, FRONT = 2, LEFT = 3, BOTTOM = 4, BACK = 5; 
	
	private Vector3f position;
	private static int texture;
	private Vector4f[] uvs;
	private boolean[] drawFace;
	private Vector3f[] faceColor;
	private float spritesInRow = 16;
	
	public static void LoadResources() {
		texture = PBUtils.loadTexture("/terrain.png");
	}
	
	public Block(int x, int y, int z, int t) {
		this.position = new Vector3f(x, y, z);
		this.uvs = new Vector4f[6];
		this.drawFace = new boolean[6];
		this.faceColor = new Vector3f[6];
		
		Arrays.fill(drawFace, true);
		Arrays.fill(faceColor, new Vector3f(1f, 1f, 1f));
		
		SetSprite(t, t/(int)spritesInRow);
	}
	
	public void SetSprite(int x, int y) {
		Arrays.fill(uvs, new Vector4f(x / spritesInRow, y / spritesInRow, (x / spritesInRow) + (1 / spritesInRow), (y / spritesInRow) + (1 / spritesInRow)));
	}
	
	public void SetSpriteForFace(int side, int x, int y) {
		uvs[side] = new Vector4f(x / spritesInRow, y / spritesInRow, (x / spritesInRow) + (1 / spritesInRow), (y / spritesInRow) + (1 / spritesInRow));
	}

	public void SetColor(float r, float g, float b) {
		Arrays.fill(faceColor, new Vector3f(r, g, b));
	}
	
	public void SetColorForFace(int face, float r, float g, float b) {
		faceColor[face] = new Vector3f(r, g, b);
	}
	
	public void SetFaceVisable(int x, int y, int z, boolean value) {
		if (x > 0) drawFace[RIGHT]	= value;
		if (x < 0) drawFace[LEFT]	= value;
		if (y > 0) drawFace[TOP]	= value;
		if (y < 0) drawFace[BOTTOM]	= value;
		if (z < 0) drawFace[FRONT]	= value;
		if (z > 0) drawFace[BACK]	= value;
	}
	
	public void ShowFace(int x, int y, int z) {
		SetFaceVisable(x, y, z, true);
	}
	
	public void HideFace(int x, int y, int z) {
		SetFaceVisable(x, y, z, false);
	}
	
	public void Draw() {
		glPushMatrix();
		{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, Block.texture);
			glTranslatef(position.x, position.y, -position.z);
			glBegin(GL_QUADS);
			{
				// FrontFace
				if (drawFace[FRONT]) {
					glColor3f(faceColor[FRONT].x, faceColor[FRONT].y, faceColor[FRONT].z);
					GL11.glTexCoord2f(uvs[FRONT].x, uvs[FRONT].y); glVertex3f(-.5f, 0.5f, 0.5f);
					GL11.glTexCoord2f(uvs[FRONT].z, uvs[FRONT].y); glVertex3f(0.5f, 0.5f, 0.5f);
					GL11.glTexCoord2f(uvs[FRONT].z, uvs[FRONT].w); glVertex3f(0.5f, -.5f, 0.5f);
					GL11.glTexCoord2f(uvs[FRONT].x, uvs[FRONT].w); glVertex3f(-.5f, -.5f, 0.5f);
				}

				// BackFace
				if (drawFace[BACK]) {
					glColor3f(faceColor[BACK].x, faceColor[BACK].y, faceColor[BACK].z);
					GL11.glTexCoord2f(uvs[BACK].x, uvs[BACK].y); glVertex3f(0.5f, 0.5f, -.5f);
					GL11.glTexCoord2f(uvs[BACK].z, uvs[BACK].y); glVertex3f(-.5f, 0.5f, -.5f);
					GL11.glTexCoord2f(uvs[BACK].z, uvs[BACK].w); glVertex3f(-.5f, -.5f, -.5f);
					GL11.glTexCoord2f(uvs[BACK].x, uvs[BACK].w); glVertex3f(0.5f, -.5f, -.5f);
				}

				// BottomFace
				if (drawFace[BOTTOM]) {
					glColor3f(faceColor[BOTTOM].x, faceColor[BOTTOM].y, faceColor[BOTTOM].z);
					GL11.glTexCoord2f(uvs[BOTTOM].x, uvs[BOTTOM].y); glVertex3f(-.5f, -.5f, 0.5f);
					GL11.glTexCoord2f(uvs[BOTTOM].z, uvs[BOTTOM].y); glVertex3f(0.5f, -.5f, 0.5f);
					GL11.glTexCoord2f(uvs[BOTTOM].z, uvs[BOTTOM].w); glVertex3f(0.5f, -.5f, -.5f);
					GL11.glTexCoord2f(uvs[BOTTOM].x, uvs[BOTTOM].w); glVertex3f(-.5f, -.5f, -.5f);
				}

				// TopFace
				if (drawFace[TOP]) {
					glColor3f(faceColor[TOP].x, faceColor[TOP].y, faceColor[TOP].z);
					GL11.glTexCoord2f(uvs[TOP].x, uvs[TOP].y); glVertex3f(-.5f, 0.5f, -.5f);
					GL11.glTexCoord2f(uvs[TOP].z, uvs[TOP].y); glVertex3f(0.5f, 0.5f, -.5f);
					GL11.glTexCoord2f(uvs[TOP].z, uvs[TOP].w); glVertex3f(0.5f, 0.5f, 0.5f);
					GL11.glTexCoord2f(uvs[TOP].x, uvs[TOP].w); glVertex3f(-.5f, 0.5f, 0.5f);
				}

				// LeftFace
				if (drawFace[LEFT]) {
					glColor3f(faceColor[LEFT].x, faceColor[LEFT].y, faceColor[LEFT].z);
					GL11.glTexCoord2f(uvs[LEFT].x, uvs[LEFT].y); glVertex3f(-.5f, 0.5f, -.5f);
					GL11.glTexCoord2f(uvs[LEFT].z, uvs[LEFT].y); glVertex3f(-.5f, 0.5f, 0.5f);
					GL11.glTexCoord2f(uvs[LEFT].z, uvs[LEFT].w); glVertex3f(-.5f, -.5f, 0.5f);
					GL11.glTexCoord2f(uvs[LEFT].x, uvs[LEFT].w); glVertex3f(-.5f, -.5f, -.5f);
				}

				// RightFace
				if (drawFace[RIGHT]) {
					glColor3f(faceColor[RIGHT].x, faceColor[RIGHT].y, faceColor[RIGHT].z);
					GL11.glTexCoord2f(uvs[RIGHT].x, uvs[RIGHT].y); glVertex3f(0.5f, 0.5f, 0.5f);
					GL11.glTexCoord2f(uvs[RIGHT].z, uvs[RIGHT].y); glVertex3f(0.5f, 0.5f, -.5f);
					GL11.glTexCoord2f(uvs[RIGHT].z, uvs[RIGHT].w); glVertex3f(0.5f, -.5f, -.5f);
					GL11.glTexCoord2f(uvs[RIGHT].x, uvs[RIGHT].w); glVertex3f(0.5f, -.5f, 0.5f);
				}
			}
			glEnd();
		}
		glPopMatrix();
	}
}

package game;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class Blocks {
	
	static int world_width = 11;
	static int world_height = 11;
	static int world_length = 11;
	static int[][][] world = new int[world_width][world_height][world_length];
	static Block[] cubes = new Block[world_width*world_height*world_length];
	
	public static void main(String[] args) {
		initDisplay();
		createWorld();
		buildWorld();
		
		Block.LoadResources();
		
		gameLoop();
		
		cleanUp();
	}
	
	public static void createWorld() {
		for (int x = 0; x < world_width; x++) {
			for (int y = 0; y < world_height; y++) {
				for (int z = 0; z < world_length; z++) {
					world[x][y][z] = -1; // Fill the world with air
					
					world[x][0][z] = 199;
					world[x][4][z] = 198;
	
					world[x][1][0] = 4;
					world[x][3][0] = 4;
					world[x][1][world_length-1] = 4;
					world[x][3][world_length-1] = 4;
					
					world[0][1][z] = 4;
					world[0][3][z] = 4;
					world[world_width-1][1][z] = 4;
					world[world_width-1][3][z] = 4;
				}
			}
		}

		world[4][3][0] = 199; world[5][3][0] = 199; world[6][3][0] = 199;
		world[4][2][0] = 199; world[5][2][0] = -1; world[6][2][0] = 199;
		world[4][1][0] = 199; world[5][1][0] = -1; world[6][1][0] = 199;

		world[3][2][0] = 4;
		world[7][2][0] = 4;
		
		world[0][3][0] = 199;
		world[0][2][0] = 199;
		world[0][1][0] = 199;
		world[0][2][1] = 4;
		world[1][2][0] = 4;

		world[world_width-1][3][0] = 199;
		world[world_width-1][2][0] = 199;
		world[world_width-1][1][0] = 199;
		world[world_width-1][2][1] = 4;
		world[world_width-2][2][0] = 4;

		world[0][3][world_length-1] = 199;
		world[0][2][world_length-1] = 199;
		world[0][1][world_length-1] = 199;
		world[1][2][world_length-1] = 4;
		world[0][2][world_length-2] = 4;

		world[world_width-1][3][world_length-1] = 199;
		world[world_width-1][2][world_length-1] = 199;
		world[world_width-1][1][world_length-1] = 199;
		world[world_width-2][2][world_length-1] = 4;
		world[world_width-1][2][world_length-2] = 4;
	}
	
	public static void buildWorld() {
		int index = 0;
		for (int x = 0; x < world_width; x++) {
			for (int y = 0; y < world_height; y++) {
				for (int z = 0; z < world_length; z++) {
					if (CubeExistsAt(x, y, z)) {
						Block cube = new Block(x, y, z, world[x][y][z]);
						if (CubeExistsAt(x, y, z + 1))
							cube.HideFace(0, 0, 1);
						if (CubeExistsAt(x, y, z - 1))
							cube.HideFace(0, 0, -1);
						if (CubeExistsAt(x, y + 1, z))
							cube.HideFace(0, 1, 0);
						if (CubeExistsAt(x, y - 1, z))
							cube.HideFace(0, -1, 0);
						if (CubeExistsAt(x + 1, y, z))
							cube.HideFace(1, 0, 0);
						if (CubeExistsAt(x - 1, y, z))
							cube.HideFace(-1, 0, 0);
						cubes[index++] = cube;
					}
				}
			}
		}
	}
	
	public static void drawWorld() {
		for (Block cube : cubes) {
			if (cube != null)
				cube.Draw();
		}
	}

	public static void gameLoop() {
		Camera.Init(70, (float) Display.getWidth() / (float) Display.getHeight(), 0.3f, 1000);

		while (!Display.isCloseRequested()) {
			
			Camera.AcceptInput(PBUtils.getDelta());
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glLoadIdentity();
			Camera.UseView();
			
			drawWorld();
			
			Display.update();

		}
	}

	public static boolean CubeExistsAt(int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x >= world_width || y >= world_height || z >= world_length)
			return false;
		
		if (world[x][y][z] > -1)
			return true;
		
		return false;
	}
	
	public static void cleanUp() {
		Display.destroy();
	}

	public static void initDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.sync(60);
			Display.create();
			Display.setTitle("Blocks");
		} catch (LWJGLException ex) {
			Logger.getLogger(Blocks.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
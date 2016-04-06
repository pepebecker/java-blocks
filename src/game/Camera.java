package game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.nio.FloatBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private static Vector3f position = new Vector3f(5f, 1.7f, 5f);
	private static Vector3f rotation = new Vector3f(0f, 0f, 0f);

	private static float mouseSensitivity = 0.05f;
	private static float maxLook = 85f;
	private static float moveSpeed = 0.01f;

	private static float fov;
	private static float aspect;
	private static float near;
	private static float far;

	public static void Init(float fov, float aspect, float near, float far) {
		Camera.fov = fov;
		Camera.aspect = aspect;
		Camera.near = near;
		Camera.far = far;
		initProjection();
	}

	private static void initProjection() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, aspect, near, far);
		glMatrixMode(GL_MODELVIEW);

		glEnable(GL_DEPTH_TEST);
		glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void UseView() {
		if (rotation.y / 360 > 1) {
			rotation.y -= 360;
		} else if (rotation.y / 360 < -1) {
			rotation.y += 360;
		}
		glLoadIdentity();
		glRotatef(rotation.x, 1, 0, 0);
		glRotatef(rotation.y, 0, 1, 0);
		glRotatef(rotation.z, 0, 0, 1);
		glTranslatef(-position.x, -position.y, -position.z);

		// glShadeModel(GL_SMOOTH);
		// glEnable(GL_LIGHTING);
		// glEnable(GL_LIGHT0);
		// glLightModel(GL_LIGHT_MODEL_AMBIENT, PBUtils.asFloatBuffer(new
		// float[] { 0.05f, 0.05f, 0.05f, 1f }));
		// glLight(GL_LIGHT0, GL_DIFFUSE, PBUtils.asFloatBuffer(new float[] {
		// 1.5f, 1.5f, 1.5f, 1f }));
		glEnable(GL_CULL_FACE);
		glCullFace(GL_FRONT);
		// glEnable(GL_COLOR_MATERIAL);
		// glColorMaterial(GL_FRONT, GL_DIFFUSE);

		float lightAmbient[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float lightDiffuse[] = { 0.5f, 0.5f, 0.5f, 1.0f };
		float lightPosition[] = { 5.0f, 1.0f, 5.0f, 0.0f };

		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer) PBUtils.asFloatBuffer(lightAmbient)); // Setup
																													// The
																													// Ambient
																													// Light
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer) PBUtils.asFloatBuffer(lightDiffuse)); // Setup
																													// The
																													// Diffuse
																													// Light
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, (FloatBuffer) PBUtils.asFloatBuffer(lightPosition));

		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	public static Vector3f GetPosition() {
		return position;
	}

	public static Vector3f GetRotation() {
		return rotation;
	}

	public static void AcceptInput(float delta) {
		AcceptInputRotate(delta);
		AcceptInputGrab();
		AcceptInputMove(delta);
	}

	public static void AcceptInputRotate(float delta) {
		if (Mouse.isGrabbed()) {
			float mouseDX = Mouse.getDX();
			float mouseDY = -Mouse.getDY();
			rotation.y += mouseDX * mouseSensitivity * delta;
			rotation.x += mouseDY * mouseSensitivity * delta;
			rotation.x = Math.max(-maxLook, Math.min(maxLook, rotation.x));
		}
	}

	public static void AcceptInputGrab() {
		if (Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
			Mouse.setGrabbed(true);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Mouse.setGrabbed(false);
		}
	}

	public static void AcceptInputMove(float delta) {
		boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP);
		boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		boolean keyFlyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		boolean keyFlyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

		float speed = moveSpeed;

		speed *= delta;

		if (keyFlyUp) {
			position.y += speed;
		}
		if (keyFlyDown) {
			position.y -= speed;
		}

		if (keyDown) {
			position.x -= Math.sin(Math.toRadians(rotation.y)) * speed;
			position.z += Math.cos(Math.toRadians(rotation.y)) * speed;
		}
		if (keyUp) {
			position.x += Math.sin(Math.toRadians(rotation.y)) * speed;
			position.z -= Math.cos(Math.toRadians(rotation.y)) * speed;
		}
		if (keyLeft) {
			position.x += Math.sin(Math.toRadians(rotation.y - 90)) * speed;
			position.z -= Math.cos(Math.toRadians(rotation.y - 90)) * speed;
		}
		if (keyRight) {
			position.x += Math.sin(Math.toRadians(rotation.y + 90)) * speed;
			position.z -= Math.cos(Math.toRadians(rotation.y + 90)) * speed;
		}
	}
}
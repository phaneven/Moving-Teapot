package ass2.spec;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Camera{
	private double[] eye = new double[3];
	private double[] centre = new double[3];
	private double[] up = {0,1,0}; //default setting
	
	private double[] face = new double[3]; // camera face direction
	private double cameraAngle = Math.PI/2;  // assume x is original direction
	
	private double viewAngle = 45;
	private double aspectRatio;
	private double nearDist = 0.1f;
	private double farDist = 100f; 
	
	private Terrain terrain;
	private Avatar avatar;
	private double[] avatarPos = new double[3]; // record the avatar's position
	double avatar_distance = 2; // set the camera backward
	private boolean avatarOpen = false;
	private Torch torch = null;
	private boolean torchState = false;
	
	/* constructor */
	Camera(Terrain terrain, double x, double y, double z) {
		this.terrain = terrain;
		eye = new double[] {x, y, z};
		face = new double[] {(double) Math.cos(cameraAngle) , y, (double) Math.sin(cameraAngle)};

		torch = new Torch(face, eye, terrain);
	}
	
	public void setCamera(GL2 gl){
		if (torchState) { // torch
			torch.torchSetting(gl);
		} else {
			gl.glDisable(GL2.GL_LIGHT1);
		} 
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		
		if (!avatarOpen) { // set the position of camera in first-person view
			if (eye[0] >= 0 && eye[0] < terrain.width()-1 && eye[2] >=0 && eye[2] < terrain.height()-1) {
				eye[1] = (double)terrain.altitude(eye[0], eye[2]) + 0.5f;
			} else {
				eye[1] = 0.5f;
			}
			centre = new double[]{eye[0] + face[0], eye[1], eye[2] + face[2]};
			GLU glu = new GLU();
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			glu.gluLookAt(eye[0], eye[1], eye[2], centre[0], centre[1], centre[2], up[0], up[1], up[2]);
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(viewAngle, aspectRatio, nearDist, farDist);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			
			
		} else { // set the position of camera in third-person view mode
			if (eye[0] >= 0 && eye[0] < terrain.width()-1 && eye[2] >=0 && eye[2] < terrain.height()-1) {
				avatar = new Avatar(eye[0], terrain.altitude(eye[0], eye[2]) + 0.5f, eye[2], face);
			} else {
				avatar = new Avatar(eye[0], 0.5f, eye[2], face);
			}
			avatarPos = avatar.getAvatarPos();
			double newEye[] = {avatarPos[0]-avatar_distance*face[0], avatarPos[1]+2, avatarPos[2]-avatar_distance*face[2]};
			
			GLU glu = new GLU();
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			glu.gluLookAt(newEye[0], newEye[1], newEye[2], avatarPos[0], avatarPos[1], avatarPos[2], up[0], up[1], up[2]);
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(viewAngle, aspectRatio, nearDist, farDist);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			avatar.draw(gl); // draw the avatar
		}	
		
		
	}
	
	public void reshape(GL2 gl, int x, int y, int width, int height){
		this.aspectRatio = 1.0 * width/height;
	}
	
	public void setAspectRatio(double ar) {
		this.aspectRatio = ar;
	}
	
	// camera behavior
	public void turnLeft(double i) {
		this.cameraAngle -= i;
		face[0] = (double) Math.cos(this.cameraAngle);
		face[2] = (double) Math.sin(this.cameraAngle);
		torch = new Torch(face, eye, terrain);
	}
	
	public void turnRight(double i) {
		this.cameraAngle += i;
		face[0] = (double) Math.cos(this.cameraAngle);
		face[2] = (double) Math.sin(this.cameraAngle);
		torch = new Torch(face, eye, terrain);
	}
	
	public void moveForward(double d) {
		this.eye[0] += d * face[0];
		this.eye[2] += d * face[2];
		torch = new Torch(face, eye, terrain);
//		if (!(eye[0] >= 0 && eye[0] < terrain.width()-1 && eye[2] >=0 && eye[2] < terrain.height()-1)) {
//			eye[0] -= d*face[0];
//			eye[2] -= d*face[2];
//		}
	}
	
	public void moveBackward(double d) {
		this.eye[0] -= d * face[0];
		this.eye[2] -= d * face[2];
		torch = new Torch(face, eye, terrain);
//		if (!(eye[0] >= 0 && eye[0] < terrain.width()-1 && eye[2] >=0 && eye[2] < terrain.height()-1)) {
//			eye[0] += d*face[0];
//			eye[2] += d*face[2];
//		}
	}
	
	// first-person mode
	public void switch_to_first_person() {
//		System.out.println("first-person view");
		this.avatarOpen = false;
	}
	
	// third-person mode
	public void switch_to_third_person(){
//		System.out.println("third-person view");
		this.avatarOpen = true;
	}
	
	// torch control 
	public void torchControl() {
		System.out.println(torchState);
		this.torchState = !torchState;
	}
}

package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Light {
	GLUT glut = new GLUT();
	private Terrain myTerrain;
	private float sunPos[] = new float[4]; 
	private boolean sunOn = true;
	
	float[] SunAmb = {0f,0f,0f,1f};
	float[] SunDif = {0.8f, 0.8f, 0.8f, 1.0f};
	float[] SunSpec = {0.8f, 0.8f, 0.8f, 1.0f};

	float[] gloAmb = {0f, 0f, 0f, 1f};
	
	private boolean nightMode = false;
	private boolean MoveMode = false;
	
	private float r1 = 0;
	private float R = 0; // the distance from sun to the mid of the terrain
	
	private long start = 0;
	private float a = 0;
	private float b = 0;
	// constructor
	Light(Terrain terrain) {
		myTerrain = terrain;
	}
	
	public void setLight(GL2 gl) {
		
		gl.glPushMatrix();
		drawAndPositionSun(gl);
		if (sunOn) {
			if (nightMode) {
				SunAmb = new float[] {0f,0f,0f,1f};
				SunDif = new float[] {0.3f, 0.3f, 0.3f, 1.0f};
				SunSpec = new float[]{0.8f, 0.8f, 0.8f, 1.0f};	
				gl.glEnable(GL2.GL_LIGHT0);
			} else if (MoveMode) {
				long current = System.currentTimeMillis();
				long par = (current - start)/1000;
				System.out.println(par);
				sunPos[2] = (float)(r1 * Math.sin(par));
				sunPos[1] = (float)(r1 * Math.cos(par));
				if (Math.sin(par) <= 0.3) // change light color
					SunAmb[0] = (float)Math.abs(Math.sin(par));
					SunAmb[1] = (float)Math.abs(Math.sin(par));
					SunAmb[2] = (float)Math.abs(Math.sin(par));
				draw(gl);
			} else if (!nightMode) {
				SunAmb = new float[] {0f,0f,0f,1f};
				SunDif = new float[] {0.8f, 0.8f, 0.8f, 1.0f};
				SunSpec = new float[]{0.8f, 0.8f, 0.8f, 1.0f};
				gl.glEnable(GL2.GL_LIGHT0);
			}
			
		} else {
			gl.glDisable(GL2.GL_LIGHT0);
		}
		// light properties
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, sunPos, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, SunDif, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, SunSpec, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, SunAmb, 0);
		
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, gloAmb, 0);
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_AMBIENT, GL2.GL_TRUE);
		
		gl.glPopMatrix();
	}
	
	public void drawAndPositionSun(GL2 gl) {
		sunPos[0] = myTerrain.getSunlight()[0]; // sun is directional light
		sunPos[1] = myTerrain.getSunlight()[1];
		sunPos[2] = myTerrain.getSunlight()[2];
		sunPos[3] = 0;
//		gl.glPushMatrix(); {
//			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, SunDif, 0);
//		}gl.glPopMatrix();
	}
	
	public void draw(GL2 gl) {
		GLUT glut = new GLUT();
		gl.glPushMatrix();
		gl.glTranslated(sunPos[0], sunPos[1], sunPos[2]);
//		gl.glRotated(360, 0, 1, 0);
//		float emml[] = {1f,1f, 1f,1.0f};
//		float matAmbAndDifL[] = {0.8f, 0.8f, 0.8f, 1.0f};
//		float matSpecL[] = {0.8f, 0.8f, 0.8f, 1.0f};
//		float matShineL[] = {50.0f};
//		
//		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDifL, 0);
//		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecL, 0);
//		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShineL, 0);
//		
//		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emml, 0);
		if (sunPos[1] > 0) {
			gl.glFrontFace(GL2.GL_CW);
			glut.glutSolidSphere(0.1, 50, 50);
			gl.glFrontFace(GL2.GL_CCW);
			gl.glEnable(GL2.GL_LIGHT0);
		} else { 
			gl.glDisable(GL2.GL_LIGHT0);
		}
		gl.glPopMatrix();
	}
	
	public void nightModeControl() {
		System.out.println("test");
		this.nightMode = !nightMode;
	}
	
	public void startSunMove() { // SunMove Initialize
		System.out.println("start move");
		MoveMode = !MoveMode;
		r1 = (float) Math.sqrt(sunPos[1]*sunPos[1] + sunPos[2]*sunPos[2]);
		R = (float) Math.sqrt(r1*r1 + sunPos[0]*sunPos[0]);
		a = (float) Math.asin(r1/R);
		b = (float) Math.atan(Math.abs(sunPos[1]) / Math.abs(sunPos[2]));
		start = System.currentTimeMillis();
		
	}
}

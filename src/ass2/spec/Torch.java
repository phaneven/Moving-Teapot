package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

public class Torch {
	
	double[] Pos = null;
	private double[] face = new double[3];
	
	private boolean torch = false;
	private float torchPos[] = null;
	private float torchDir[] = null;
	
	private float lightAmb[] = {0.5f,0.5f,0.5f,1f};
	private float lightDiff[] = {1f,1f,1f,1f};
	private float lightSpec[] = {1f,1f,1f,1f};
	private float gloAmb[] = {0.5f,0.5f,0.5f,1f};
	double avatar_distance = 2;
	
	Terrain terrain = null;
	Torch (double[] face, double[] Pos, Terrain terrain) {
		this.face = face;
		this.Pos = Pos;
		this.terrain = terrain;
		if (Pos[0] >= 0 && Pos[0] < terrain.width()-1 && Pos[2] >=0 && Pos[2] < terrain.height()-1) {
			Pos[1] = (float)terrain.altitude(Pos[0], Pos[2]) + 0.2f;
		} else {
			Pos[1] = 0.2f;
		}
	}
	
	
	
	public void torchSetting(GL2 gl) {
		
		// light properties
		gl.glLightfv(GL2.GL_LIGHT1,GL2.GL_AMBIENT, lightAmb, 0);
		gl.glLightfv(GL2.GL_LIGHT1,GL2.GL_DIFFUSE, lightDiff, 0);
		gl.glLightfv(GL2.GL_LIGHT1,GL2.GL_SPECULAR, lightSpec, 0);
		gl.glEnable(GL2.GL_LIGHT1);
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, gloAmb, 0); // global ambient light
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE); // enable local viewpoint
		gl.glPushMatrix();{
			torchPos = new float[] {(float)(Pos[0]), (float)Pos[1]+0.5f, (float)(Pos[2]), 1};
			torchDir = new float[] {(float)face[0], (float)face[1], (float)face[2], 0};
			System.out.println(torchPos[0]);

			gl.glLightfv(GL2.GL_LIGHT1,GL2.GL_POSITION, torchPos,0);
			gl.glLightf(GL2.GL_LIGHT1,GL2.GL_SPOT_CUTOFF, 45.0f);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, torchDir, 0);
			gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, 1.0f);
		}
		gl.glPopMatrix();	
	}
}

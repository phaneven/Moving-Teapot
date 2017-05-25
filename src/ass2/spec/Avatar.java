package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {
	private double AvatarPos[] = null;
	double AvatarAngle = 0;
	private double AvatarSize = 0.1f;
	private double[] face = new double[3];
	
	
	public Avatar(double x, double y, double z, double[] face){
		AvatarPos = new double[] {x , y, z};
		this.face = face;
	}

	public void draw(GL2 gl){
		gl.glPushMatrix();
		GLUT glut = new GLUT();
		gl.glTranslated(AvatarPos[0], AvatarPos[1], AvatarPos[2]);
		gl.glRotated(Math.toDegrees(AvatarAngle), 0, 1, 0);
		
		MyTexture texture = new MyTexture(gl, "src/ass2/spec/bark.jpg", "jpg", true);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	
		gl.glFrontFace(GL2.GL_CW);
		glut.glutSolidTeapot(AvatarSize);
		gl.glFrontFace(GL2.GL_CCW);
		
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glPopMatrix();
	}
	
	public double[] getAvatarPos(){
		return this.AvatarPos;
	}
	
	
	public double getAngle(){
		return this.AvatarAngle;
	}
	
	public double getModleSize(){
		return this.AvatarSize;
	}
	
	
}

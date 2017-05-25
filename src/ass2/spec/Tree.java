package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {
	 
	private double treeRadius = 0.1;
	private double treeHeight = 1.0;
	private double sphereRadius = 0.5;
    private double[] myPos;
    MyTexture barkTexture;
    MyTexture leafTexture;
    
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
        
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    public void drawTree(GL2 gl) {

    	// add texture
    	barkTexture = new MyTexture(gl, "src/ass2/spec/bark.jpg", "jpg", true);
    	leafTexture = new MyTexture(gl, "src/ass2/spec/leaves.jpg", "jpg", true);
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	
    	gl.glPushMatrix();
    	gl.glTranslated(this.getPosition()[0], this.getPosition()[1], this.getPosition()[2]);
    	gl.glRotatef(-90, 1.0f, 0.0f, 0.0f);
    	
    	GLU glu = new GLU();
    	GLUquadric cylinder = glu.gluNewQuadric();
    	glu.gluQuadricTexture(cylinder, true);
    	glu.gluQuadricNormals(cylinder, GLU.GLU_SMOOTH);
    	
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, barkTexture.getTextureId());
    	gl.glFrontFace(GL2.GL_CW);
    	glu.gluCylinder(cylinder, treeRadius, treeRadius, treeHeight, 5, 5);
    	gl.glFrontFace(GL2.GL_CCW);
    	barkTexture.release(gl); // release bark texture buffer
    	gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
    	gl.glTranslated(0, treeHeight, 0.0f);
    
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, leafTexture.getTextureId());
    	gl.glFrontFace(GL2.GL_CW);
    	glu.gluSphere(cylinder, sphereRadius, 32, 32);
    	gl.glFrontFace(GL2.GL_CCW);
    	leafTexture.release(gl); // release leaf texture buffer
    	gl.glDisable(GL2.GL_TEXTURE_2D);
    	gl.glPopMatrix();
 
    }
}


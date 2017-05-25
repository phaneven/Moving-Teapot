package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;
    
    private int MeshNum;
    private double VertexList[];
    private double NormalList[];
    private double textureCoordinateList[];
    private Other other = null;
    private MyTexture grassTexture;
    public boolean otherOpen = false;
    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
       
        MeshNum = 2*(width-1)*(depth-1);
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }
    
    public double width() {
    	return mySize.width;
    }
    
    public double height() {
    	return mySize.height;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
//    	double altitude = 0;
//      int x_ceil = (int) Math.ceil(x);
//    	int x_floor = (int) Math.floor(x);
//    	int z_ceil = (int) Math.ceil(z);
//    	int z_floor = (int) Math.floor(z);
//    	
//    	// 4 points
//    	// p1 p3
//    	// p2 p4
//    	double p1 = myAltitude[x_floor][z_floor];
//    	double p2 = myAltitude[x_floor][z_ceil];
//    	double p3 = myAltitude[x_ceil][z_floor];
//    	double p4 = myAltitude[x_ceil][z_ceil];
//    	double t = (z - z_floor) / (x - x_floor);
//    	if (t == 1) { // the point is on the line between p2 and p3
//    		altitude = p2 * (z - z_floor) / (z_ceil - z_floor) + p3 * (z_ceil - z) / (z_ceil - z_floor);
//    		return altitude; 
//    	}
//    	if(z_ceil!=z && x!=x_ceil){
//	    	double r1 = (x_ceil-x)*p1 + (x-x_floor)*p3;
//	    	double r2 = (x_ceil-x)*p2 +  (x-x_floor)*p4;
//	        altitude = (z_ceil-z)*r1+(z-z_floor)*r2;
//    	} else if(z_ceil == z && x_ceil != x){ 
//    		double r1 = (x_ceil-x)*p1 + (x-x_floor)*p3;
//    		return r1;
//    	} else if(x_ceil == x && z_ceil != z){ 
//    		double r1 = (z_ceil-z)*p1 + (z-z_floor)*p2;
//    		return r1;
//    	} else if(x_ceil == x && z_ceil == z){ 
//    		return getGridAltitude((int)x,(int)z);
//    	}
//        return altitude;
    	double altitude = 0;
    	int x_ceil = (int) Math.ceil(x);
    	int x_floor = (int) Math.floor(x);
    	int z_ceil = (int) Math.ceil(z);
    	int z_floor = (int) Math.floor(z);
    	
    	// 4 points
    	// p1 p3
    	// p2 p4
    	double p1 = myAltitude[x_floor][z_floor];
    	double p2 = myAltitude[x_floor][z_ceil];
    	double p3 = myAltitude[x_ceil][z_floor];
    	double p4 = myAltitude[x_ceil][z_ceil];
    	
    	if(z_ceil!=z && x!=x_ceil){
    		double t = (z_ceil-z) / (x - x_floor);
        	if (t == 1) { // the point is on the line between p2 and p3
        		altitude = p2 * (z - z_floor) / (z_ceil - z_floor) + p3 * (z_ceil - z) / (z_ceil - z_floor);
        		return altitude; 
        	} 
        	else if (t > 1) { // left triangle p1 p2 p3
        		double r1 = p2 * (z - z_floor) / (z_ceil - z_floor) + p1 *(z_ceil - z) / (z_ceil - z_floor);
        		double r2 = p2 * (z - z_floor) / (z_ceil - z_floor) + p3 * (z_ceil - z) / (z_ceil - z_floor);
        		// (x_floor,z) (x_floor+z_ceil-z, z)
        		altitude = (x-x_floor)/(z_ceil - z)*r2 + (x_floor+z_ceil-z - x)/(z_ceil-z)*r1;
        		return altitude;
        	} else { // right triangle p2 p3 p4
        		double r1 = p2 * (z - z_floor) / (z_ceil - z_floor) + p3 * (z_ceil - z) / (z_ceil - z_floor);
        		double r2 = p4 * (z - z_floor) / (z_ceil - z_floor) + p3 * (z_ceil - z) / (z_ceil - z_floor);
        		// (x_floor+z_ceil-z, z) (x_ceil,z)
        		altitude = (x - (x_floor+z_ceil-z))/(x_ceil - (x_floor+z_ceil-z))*r2 + (x_ceil - x)/(x_ceil - (x_floor+z_ceil-z))*r1;
        		altitude = (x - (x_floor+z_ceil-z))/(x_ceil - (x_floor+z_ceil-z))*r2 + (x_ceil - x)/(x_ceil - (x_floor+z_ceil-z))*r1;

        		return altitude;
        	}
        	
    	} else if(z_ceil == z && x_ceil != x){ 
    		double r1 = (x_ceil-x)*p1 + (x-x_floor)*p3;
    		return r1;
    	} else if(x_ceil == x && z_ceil != z){ 
    		double r1 = (z_ceil-z)*p1 + (z-z_floor)*p2;
    		return r1;
    	} else if(x_ceil == x && z_ceil == z && x_floor == x && z_floor == z){ 
    		return getGridAltitude((int)x,(int)z);
    	}
    	return altitude;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        road.init(this);
        myRoads.add(road);        
    }
    
    public void meshGenerator(GL2 gl) {
    	VertexList = new double[MeshNum*3*3];
    	NormalList = new double[MeshNum*3*3];
    	textureCoordinateList = new double[MeshNum*3*2];
    	
    	for (int i = 0; i < MeshNum*3*2; i+=6) {
    		textureCoordinateList[i] = 0.0f;
    		textureCoordinateList[i+1] = 0.0f;
    		textureCoordinateList[i+2] = 0.0f;
    		textureCoordinateList[i+3] = 1.0f;
    		textureCoordinateList[i+4] = 1.0f;
    		textureCoordinateList[i+5] = 1.0f;
    	}
    	
    	int vptr = 0;
    	int nptr = 0;
    	for (int i = 0; i < (int)mySize.getWidth()-1; ++i) {
    		for (int j=0; j < (int)mySize.getHeight()-1; ++j) {
    			// top left triangle
    			VertexList[vptr++] = i;
    			VertexList[vptr++] = myAltitude[i][j];
    			VertexList[vptr++] = j;
    			
    			VertexList[vptr++] = i;
    			VertexList[vptr++] = myAltitude[i][j+1];
    			VertexList[vptr++] = j + 1;
    		
    			VertexList[vptr++] = i + 1;
    			VertexList[vptr++] = myAltitude[i+1][j];
    			VertexList[vptr++] = j;
    			
    			double[] lside1 = {0, (myAltitude[i][j+1] - myAltitude[i][j]), 1};
    			double[] lside2 = {1, (myAltitude[i+1][j] - myAltitude[i][j]), 0};
    			double[] n1 = MathUtil.crossProduct(lside1, lside2);
    			n1 = MathUtil.Normalise(n1);
    			NormalList[nptr++] = n1[0]; NormalList[nptr++] = n1[1]; NormalList[nptr++] = n1[2];
    			
    			//bottom right triangle
    			VertexList[vptr++] = i + 1;
    			VertexList[vptr++] = myAltitude[i+1][j];
    			VertexList[vptr++] = j;
    			
    			
    			VertexList[vptr++] = i;
    			VertexList[vptr++] = myAltitude[i][j+1];
    			VertexList[vptr++] = j + 1;
    			
    			VertexList[vptr++] = i + 1;	
    			VertexList[vptr++] = myAltitude[i+1][j+1];	
    			VertexList[vptr++] = j + 1;
    			
  
    			double[] rside1 = {-1, (myAltitude[i][j+1] - myAltitude[i+1][j]), 1};
    			double[] rside2 = {0, (myAltitude[i+1][j+1] - myAltitude[i+1][j]), 1};
    			double[] n2 = MathUtil.crossProduct(rside1, rside2);
    			n2 = MathUtil.Normalise(n2);
    			NormalList[nptr++] = n2[0]; NormalList[nptr++] = n2[1]; NormalList[nptr++] = n2[2];
    		}
    	}
    }
    
    public void draw(GL2 gl) {
    	int nptr = 0;
    	int tptr = 0;
    	// texture
    	grassTexture = new MyTexture(gl, "src/ass2/spec/grass.jpg", "jpg", true);
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, grassTexture.getTextureId());
    	for (int i = 0; i < VertexList.length; i = i+9) {
    		gl.glBegin(GL2.GL_TRIANGLES);
            {
            	gl.glNormal3d(NormalList[nptr++], NormalList[nptr++], NormalList[nptr++]);
            	gl.glColor3d(0, 1, 0);
            	gl.glTexCoord2d(textureCoordinateList[tptr++], textureCoordinateList[tptr++]);
            	gl.glVertex3d(VertexList[i], VertexList[i+1],VertexList[i+2]);
            	gl.glTexCoord2d(textureCoordinateList[tptr++], textureCoordinateList[tptr++]);
                gl.glVertex3d(VertexList[i+3], VertexList[i+4],VertexList[i+5]);
                gl.glTexCoord2d(textureCoordinateList[tptr++], textureCoordinateList[tptr++]);
                gl.glVertex3d(VertexList[i+6], VertexList[i+7],VertexList[i+8]);
            }
            gl.glEnd();
            gl.glColor3d(0, 0, 0);
            gl.glBegin(GL2.GL_LINE_LOOP); {
            	gl.glVertex3d(VertexList[i], VertexList[i+1],VertexList[i+2]);
                gl.glVertex3d(VertexList[i+3], VertexList[i+4],VertexList[i+5]);
                gl.glVertex3d(VertexList[i+6], VertexList[i+7],VertexList[i+8]);
            };
            gl.glEnd();
    	}
    	grassTexture.release(gl); // release grass texture buffer
    	gl.glDisable(GL2.GL_TEXTURE_2D);
    	// draw trees
    	for (Tree tree : myTrees) {
    		tree.drawTree(gl);
    	}
    	// draw roads
    	for (Road road : myRoads) {
    		road.draw(gl);
    	}
    	// draw others
    	if (otherOpen) {
    		float[] otherPos = {0,(float)this.altitude(0, 0),0};
        	other = new Other(otherPos);
        	other.draw(gl);
    	}
    	
    	
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }
}

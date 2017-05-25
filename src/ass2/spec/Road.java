package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;


/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;
    
    MyTexture roadTexture;
    
    private Terrain terrain;
    private ArrayList<double[]> spine = null;
    private double[][] crossSec = null;
    ArrayList<Point> Points = null;
    int SLICES = 100;
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        myPoints.add(x0);
        myPoints.add(y0);
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
    }

    
    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(y1);
        myPoints.add(x2);
        myPoints.add(y2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        double[] p = new double[2];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;        
        
        return p;
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

    public void init(Terrain terrain) {
    	this.terrain = terrain;
    	this.Points = new ArrayList<Point>();
    	this.spine = new ArrayList<double[]>();
    	this.crossSec = new double[][] {{-myWidth/2, 0, 0, 1}, {myWidth/2, 0, 0, 1}};
    	this.initSpines();
    	this.addPoint();
    }
    
    public void initSpines() {
    	double a = ((double)this.size()) / SLICES;
    	for (int i = 0; i < SLICES; i++) {
    		double[] p = point(i * a);
    		spine.add(p);
    	}
    }
    
    public void addPoint() {
    	double pCurr[] = new double[] {spine.get(0)[0], terrain.altitude(spine.get(0)[0], spine.get(0)[1]), spine.get(0)[1]};
    	double pNext[] = new double[] {spine.get(1)[0], terrain.altitude(spine.get(1)[0], spine.get(1)[1]), spine.get(1)[1]};
    	double pPrev[] = pCurr;
    	
    	this.Points.add(getPoint(pCurr, pNext, pPrev));
    	
    	for (int i = 1; i < spine.size()-1; i++) {
    		pPrev = pCurr;
    		pCurr = pNext;
    		pNext = new double[] {spine.get(i+1)[0], terrain.altitude(spine.get(i+1)[0], spine.get(i+1)[1]), spine.get(i+1)[1]};
    		this.Points.add(getPoint(pCurr, pNext, pPrev));
    	}
    	pPrev = pCurr;
    	pCurr = pNext;
    	this.Points.add(getPoint(pCurr, pNext, pPrev));
    }
    
    public Point getPoint(double[] pCurr, double[] pNext, double[] pPrev) {
    	double m[][] = new double[4][4];
    	// phi = pCurr
    	m[0][3] = pCurr[0];
    	m[1][3] = pCurr[1];
    	m[2][3] = pCurr[2];
    	m[3][3] = 1;
    	
    	// k = pNext - pPrev
    	m[0][2] = pNext[0] - pPrev[0];
    	m[1][2] = pNext[1] - pPrev[1];
    	m[2][2] = pNext[2] - pPrev[2];
    	m[3][2] = 0;
    	
    	// normalise k
    	double n1 = Math.sqrt(m[0][2] * m[0][2] + m[1][2] * m[1][2] + m[2][2] * m[2][2]);
    	m[0][2] /= n1;
    	m[1][2] /= n1;
    	m[2][2] /= n1;
    	
    	
      	// i = simple perpendicular to k
    	m[0][0] = -m[2][2];
    	m[1][0] = 0;
    	m[2][0] = m[0][2];
    	m[3][0] = 0;
    	
//    	double[] k = new double[] {m[0][2], m[1][2], m[2][2]};
//    	
//    	// get the normal of terrain face
//    	double[] p1 = new double[3];
//    	double[] p2 = new double[3];
//    	double[] p3 = new double[3];
//    	if (Math.ceil(pCurr[2]) - pCurr[2] >= pCurr[0] - Math.floor(pCurr[0])) {
//    		p1 = new double[] {Math.floor(pCurr[0]), terrain.altitude(Math.floor(pCurr[0]), Math.floor(pCurr[2])) ,Math.floor(pCurr[2])};
//        	p2 = new double[] {Math.floor(pCurr[0]), terrain.altitude(Math.floor(pCurr[0]), Math.ceil(pCurr[2])) ,Math.ceil(pCurr[2])};
//        	p3 = new double[] {Math.ceil(pCurr[0]), terrain.altitude(Math.ceil(pCurr[0]), Math.floor(pCurr[2])) ,Math.floor(pCurr[2])};
//    	} else {
//    		p3 = new double[] {Math.ceil(pCurr[0]), terrain.altitude(Math.ceil(pCurr[0]), Math.floor(pCurr[2])) ,Math.floor(pCurr[2])};
//        	p2 = new double[] {Math.ceil(pCurr[0]), terrain.altitude(Math.ceil(pCurr[0]), Math.ceil(pCurr[2])) ,Math.ceil(pCurr[2])};
//        	p1 = new double[] {Math.floor(pCurr[0]), terrain.altitude(Math.floor(pCurr[0]), Math.ceil(pCurr[2])) ,Math.ceil(pCurr[2])};
//    	} 
//     			
//    	m[0][0] = MathUtil.Normalise(MathUtil.crossProduct(k, MathUtil.Normalise(MathUtil.getNormal(p1, p2, p3))))[0];
//    	m[1][0] = MathUtil.Normalise(MathUtil.crossProduct(k, MathUtil.Normalise(MathUtil.getNormal(p1, p2, p3))))[1];
//    	m[2][0] = MathUtil.Normalise(MathUtil.crossProduct(k, MathUtil.Normalise(MathUtil.getNormal(p1, p2, p3))))[2];
//    	m[3][0] = 0;
    
    	
    	
    	// j = k * i
    	m[0][1] = m[1][2] * m[2][0] - m[1][0] * m[2][2];
    	m[1][1] = m[2][2] * m[0][0] - m[2][0] * m[0][2];
    	m[2][1] = m[0][2] * m[1][0] - m[0][0] * m[1][2];
    	m[3][1] = 0;
    	
    	Point pointList = new Point();
    	
    	for (int i = 0; i < this.crossSec.length; i++) {
    		double[] p = multiply(m, this.crossSec[i]);
    		pointList.add(p);
    	}
    	
    	return pointList;
    }
    
    public double[] multiply(double[][] matrix, double[] vertex) {
    	double m[] = new double[vertex.length];
    	for (int i = 0; i < matrix.length; i++) {
    		for (int j = 0; j < matrix[i].length; j++) {
    			m[i] += matrix[i][j] * vertex[j];
    		}
    	}
    	return m;
    }
    
    public void draw(GL2 gl) {

    	gl.glPushMatrix();
    	gl.glPolygonOffset(-1, -1);
    	// add texture
    	roadTexture = new MyTexture(gl, "src/ass2/spec/road.jpg", "jpg", true);
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, roadTexture.getTextureId());
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		
    	for (int i = 0; i < this.Points.size()-1; i++) {
   
        	double p00[] = this.Points.get(i).get(0);
        	double p01[] = this.Points.get(i).get(1);
        	double p10[] = this.Points.get(i+1).get(0);
        	double p11[] = this.Points.get(i+1).get(1);
        	
        	double N1[] = MathUtil.getNormal(p00, p01, p11);
        	N1 = MathUtil.Normalise(N1);
        	double N2[] = MathUtil.getNormal(p11, p10, p00);
        	N2 = MathUtil.Normalise(N2);
        	// draw triangles
        	gl.glBegin(GL2.GL_TRIANGLES); 
        	{
        		gl.glNormal3d(N1[0], N1[1], N1[2]);
        		gl.glTexCoord2d(1, 1);
        		gl.glVertex3d(p00[0], p00[1], p00[2]);
        		gl.glTexCoord2d(1, 0);
        		gl.glVertex3d(p01[0], p01[1], p01[2]);
        		gl.glTexCoord2d(0, 1);
        		gl.glVertex3d(p11[0], p11[1], p11[2]);
        		
        		gl.glNormal3d(N2[0], N2[1], N2[2]);
        		gl.glTexCoord2d(1, 1);
        		gl.glVertex3d(p11[0], p11[1], p11[2]);
        		gl.glTexCoord2d(0, 1);
        		gl.glVertex3d(p10[0], p10[1], p10[2]);
        		gl.glTexCoord2d(1, 0);
        		gl.glVertex3d(p00[0], p00[1], p00[2]);
        	}
        	gl.glEnd();
    	}
    	this.roadTexture.release(gl);
    	gl.glDisable(GL2.GL_TEXTURE_2D);

    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	gl.glPolygonOffset(0, 0);
    	gl.glPopMatrix();
    }
}

class Point {
	ArrayList<double[]> vertices = null;
	public Point() {
		vertices = new ArrayList<double[]>();
	}
	public void add(double[] p){
		vertices.add(p);
	}
	public double[] get(int index) {
		return vertices.get(index);
	}
	public int size() {
		return vertices.size();
	}
}
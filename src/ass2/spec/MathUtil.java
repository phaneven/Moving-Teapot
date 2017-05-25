package ass2.spec;


/**
 * A collection of useful math methods 
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 */
public class MathUtil {

   public static double[] crossProduct(double[] vector1, double[] vector2) {
	   double[] result = new double[3];
	   result[0] = vector1[1] * vector2[2] - vector1[2] * vector2[1];
	   result[1] = vector1[2] * vector2[0] - vector1[0] * vector2[2];
	   result[2] = vector1[0] * vector2[1] - vector1[1] * vector2[0];
	   return result;
   }
   
   public static double[] subtraction(double[] vector1, double[] vector2) {
	   double[] result = new double[3];
	   result[0] = vector1[0] - vector2[0];
	   result[1] = vector1[1] - vector2[1];
	   result[2] = vector1[2] - vector2[2];
	   return result;
   }
   
   public static double getMagnitude(double [] n){
	   double mag = n[0]*n[0] + n[1]*n[1] + n[2]*n[2];
	   mag = (double) Math.sqrt(mag);
	   return mag;
   }
    
   public static double [] Normalise(double [] n){
	   double  mag = getMagnitude(n);
	   double norm[] = {n[0]/mag,n[1]/mag,n[2]/mag};
	   return norm;
   }
   
 
   
   public static double [] getNormal(double[] p0, double[] p1, double[] p2){
	   double u[] = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
	   double v[] = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};
   	
	   return crossProduct(u,v);

   }
   
   
   
}


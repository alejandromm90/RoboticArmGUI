package math;

/**
 * 
 * @author Philippe Heer
 *
 */
public abstract class Calculate {

	/**
	 * 
	 * @param point
	 * @return angles
	 */
	public static Angles calculateAngles(Point point, double lengthShoulder, double lengthElbow) {
		float x = point.getX();
		float y = point.getY();
		float z = point.getZ();

//		double thi = (Math.atan2(y,x);
		double length = Math.sqrt(x * x + y * y);
		
		double kappa = 2 *  Math.atan(-Math.sqrt(((lengthElbow + lengthShoulder) * (lengthElbow + lengthShoulder) - (length * length + z * z)) / ((length * length + z * z) - (lengthElbow - lengthShoulder) * (lengthElbow - lengthShoulder)))); 
//		double theta = Math.atan2(z, length) - Math.atan2(lengthElbow * Math.sin(kappa), lengthShoulder + lengthElbow * Math.cos(kappa));
		double theta = (2 * Math.atan(-Math.sqrt(((lengthElbow + lengthShoulder) * (lengthElbow + lengthShoulder) - (x*x + y*y))
				/ ((x*x + y*y) - (lengthElbow - lengthShoulder) * (lengthElbow - lengthShoulder))))); 
		// elbow always parallel to shoulder
		kappa += theta;
		double thi = (Math.atan2(y,x) - Math.atan2(lengthElbow * Math.sin(theta), lengthShoulder + lengthElbow*Math.cos(theta)));
		
		return new Angles(Math.toDegrees(thi), Math.toDegrees(theta), Math.toDegrees(kappa));
	}
	
	public static float calculatePhiAngle(float x, float y, float lengthElbow, float lengthShoulder, float thetaAngle) {
		float phi;
		phi = (float) (Math.atan2(y,x) - Math.atan2(lengthElbow * Math.sin(thetaAngle), lengthShoulder + lengthElbow*Math.cos(thetaAngle)));
		return phi;
	}
	
	public static float calculateThetaAngle(float x, float y, float lengthElbow, float lengthShoulder) {
		float theta;
		theta = (float) (2 * Math.atan(-Math.sqrt(((lengthElbow + lengthShoulder) * (lengthElbow + lengthShoulder) - (x*x + y*y))
				/ ((x*x + y*y) - (lengthElbow - lengthShoulder) * (lengthElbow - lengthShoulder)))));
		return theta;
	}
}

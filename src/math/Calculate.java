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

		double length = Math.sqrt(x * x + y * y);
		
		double kappa = 2 *  Math.atan(-Math.sqrt(((lengthElbow + lengthShoulder) * (lengthElbow + lengthShoulder) - (length * length + z * z)) / ((length * length + z * z) - (lengthElbow - lengthShoulder) * (lengthElbow - lengthShoulder)))); 
		double theta = (2 * Math.atan(-Math.sqrt(((lengthElbow + lengthShoulder) * (lengthElbow + lengthShoulder) - (x*x + y*y))
				/ ((x*x + y*y) - (lengthElbow - lengthShoulder) * (lengthElbow - lengthShoulder))))); 
		double thi = (Math.atan2(y,x) - Math.atan2(lengthElbow * Math.sin(theta), lengthShoulder + lengthElbow*Math.cos(theta)));

		// elbow always parallel to shoulder
		kappa += theta;		
		
		return new Angles(thi, theta, kappa);
	}
}

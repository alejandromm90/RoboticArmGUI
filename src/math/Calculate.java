package math;

/**
 * 
 * @author Philippe Heer
 *
 */
public abstract class Calculate {
	public static final double ARM_LENGTH_SHOULDER = 10.0;
	public static final double ARM_LENGTH_ELBOW = 10.0;

	/**
	 * 
	 * @param point
	 * @return angles
	 */
	public static Angles calculateAngles(Point point) {
		float x = point.getX();
		float y = point.getY();
		float z = point.getZ();

		double thi = Math.atan2(y, x);
		double length = Math.sqrt(x * x + y * y);
		
		double kappa = 2 *  Math.atan(-Math.sqrt(((ARM_LENGTH_ELBOW + ARM_LENGTH_SHOULDER) * (ARM_LENGTH_ELBOW + ARM_LENGTH_SHOULDER) - (length * length + z * z)) / ((length * length + z * z) - (ARM_LENGTH_ELBOW - ARM_LENGTH_SHOULDER) * (ARM_LENGTH_ELBOW - ARM_LENGTH_SHOULDER)))); 
		double theta = Math.atan2(z, length) - Math.atan2(ARM_LENGTH_ELBOW * Math.sin(kappa), ARM_LENGTH_SHOULDER + ARM_LENGTH_ELBOW * Math.cos(kappa));
	
		// elbow always parallel to shoulder
		kappa += theta;
		
		return new Angles(Math.toDegrees(thi), Math.toDegrees(theta), Math.toDegrees(kappa));
	}
}

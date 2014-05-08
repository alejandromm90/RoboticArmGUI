package math;

import geometric.Angles;
import geometric.RelativePoint;

import constants.Constants;

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
	public static Angles calculateAngles(RelativePoint point) {
		double x = point.getX();
		double y = point.getY();
		double z = point.getZ();

		double thi = Math.atan2(y, x);
		double length = Math.sqrt(x * x + y * y);
		
		double kappa = 2 *  Math.atan(-Math.sqrt(((Constants.ARM_LENGTH_ELBOW + Constants.ARM_LENGTH_SHOULDER) * (Constants.ARM_LENGTH_ELBOW + Constants.ARM_LENGTH_SHOULDER) - (length * length + z * z)) / ((length * length + z * z) - (Constants.ARM_LENGTH_ELBOW - Constants.ARM_LENGTH_SHOULDER) * (Constants.ARM_LENGTH_ELBOW - Constants.ARM_LENGTH_SHOULDER)))); 
		double theta = Math.atan2(z, length) - Math.atan2(Constants.ARM_LENGTH_ELBOW * Math.sin(kappa), Constants.ARM_LENGTH_SHOULDER + Constants.ARM_LENGTH_ELBOW * Math.cos(kappa));
	
		// elbow always parallel to shoulder
		kappa += theta;
		
		return new Angles(thi, theta, kappa);
	}
	
	/**
	 * 
	 * @param point
	 * @return
	 */
	public static Angles calculateAngles2(RelativePoint point) {
		double x = point.getX();
		double y = point.getY();
		double z = point.getZ();

		double thi = Math.atan2(y, x);

		double length = Math.sqrt(x * x + y * y);
		double theta = Math.atan2(z, length);
		
		return new Angles(thi, theta, 0);
	}
}

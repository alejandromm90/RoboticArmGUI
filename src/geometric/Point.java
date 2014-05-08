package geometric;

import constants.Constants;

/**
 * 
 * @author Philippe Heer
 * 
 */
public class Point extends RelativePoint {

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point(double x, double y, double z) {
		super(x - Constants.BASE_X, y - Constants.BASE_Y, z - Constants.BASE_Z);
	}
}

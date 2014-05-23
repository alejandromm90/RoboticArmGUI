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
	 * @param flow1
	 * @param flow2
	 * @param flow3
	 */
	public Point(double x, double y, double z, long flow1, long flow2,
			long flow3) {
		super(x - Constants.DRAWING_APPLET_RELATIVE_X,
				Constants.DRAWING_APPLET_SIZE_HEIGHT - y, z, flow1, flow2,
				flow3);
	}
}
package geometric;

import constants.Constants;
import constants.Flavour;

/**
 * The Point class represents a point in a 3d space relative to the drawing
 * applet. It also has attribute w, the flow of the flavour and f, the flavour
 * itself.
 * 
 * @author Philippe Heer
 * 
 */
public class Point extends RelativePoint {

	/**
	 * 
	 * @param x
	 *            - coordinate x
	 * @param y
	 *            - coordinate y
	 * @param z
	 *            - coordinate z
	 * @param flow
	 *            - flow
	 * @param flavour
	 *            - flavour
	 */
	public Point(double x, double y, double z, int flow, Flavour flavour) {
		super(x - Constants.BASE_X, y - Constants.BASE_Y, z - Constants.BASE_Z,
				flow, flavour);
	}
}

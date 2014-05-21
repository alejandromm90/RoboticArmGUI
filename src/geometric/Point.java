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
	 *            - flow, between -120 and 120
	 * @param flavour
	 *            - flavour
	 */
	public Point(double x, double y, double z, int flow, Flavour flavour) {
		super(x - Constants.DRAWING_APPLET_RELATIVE_X,
				Constants.DRAWING_APPLET_SIZE_HEIGHT - y, z, flow, flavour);
	}
}
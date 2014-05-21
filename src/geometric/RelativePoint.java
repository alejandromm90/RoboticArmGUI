package geometric;

import constants.Flavour;

/**
 * The RelativePoint class represents a point in a 3d space relative to the
 * robotic arm. It also has attribute w, the flow of the flavour and f, the
 * flavour itself.
 * 
 * @author Philippe Heer
 * 
 */
public class RelativePoint {
	private double x;
	private double y;
	private double z;
	private int flow;
	private Flavour flavour;

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
	public RelativePoint(double x, double y, double z, int flow, Flavour flavour) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.flow = flow;
		this.flavour = flavour;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "x : " + (int) x + " y : " + (int) y + " z : " + (int) z
				+ " flow : " + flow + " flavour : " + flavour + "\n";
	}

	/**
	 * 
	 * @return x
	 */
	public double getX() {
		return x;
	}

	/**
	 * 
	 * @param x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * 
	 * @return y
	 */
	public double getY() {
		return y;
	}

	/**
	 * 
	 * @param y
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * 
	 * @return z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * 
	 * @param z
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * 
	 * @return flow
	 */
	public int getFlow() {
		return flow;
	}

	/**
	 * 
	 * @param flow
	 */
	public void setFlow(int flow) {
		this.flow = flow;
	}

	/**
	 * 
	 * @return flavour
	 */
	public Flavour getFlavour() {
		return flavour;
	}

	/**
	 * 
	 * @param flavour
	 */
	public void setF(Flavour flavour) {
		this.flavour = flavour;
	}
}

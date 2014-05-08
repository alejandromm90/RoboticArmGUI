package geometric;


/**
 * 
 * @author Philippe Heer
 *
 */
public class RelativePoint {
	private double x;
	private double y;
	private double z;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public RelativePoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "x : " + (int) x + "\ny : " + (int) y + "\nz : " + (int) z;
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
}

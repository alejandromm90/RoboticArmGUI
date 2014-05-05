package math;

/**
 * 
 * @author Philippe Heer
 *
 */
public class Point {
	private float x;
	private float y;
	private float z;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * 
	 * @return x
	 */
	public float getX() {
		return x;
	}

	/**
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * 
	 * @return y
	 */
	public float getY() {
		return y;
	}

	/**
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 
	 * @return z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * 
	 * @param z
	 */
	public void setZ(int z) {
		this.z = z;
	}
}

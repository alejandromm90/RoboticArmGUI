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
	private long flow1;
	private long flow2;
	private long flow3;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param flow1
	 * @param flow2
	 * @param flow3
	 */
	public RelativePoint(double x, double y, double z, long flow1, long flow2,
			long flow3) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.flow1 = flow1;
		this.flow2 = flow2;
		this.flow3 = flow3;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "x : " + (int) x + ", y : " + (int) y + ", z : " + (int) z
				+ ", flow1 : " + flow1 + ", flow2 : " + flow2 + ", flow3 : "
				+ flow3 + "\n";
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
	 * @return flow1
	 */
	public long getFlow1() {
		return flow1;
	}

	/**
	 * 
	 * @param flow1
	 */
	public void setFlow1(long flow1) {
		this.flow1 = flow1;
	}

	/**
	 * 
	 * @return flow2
	 */
	public long getFlow2() {
		return flow2;
	}

	/**
	 * 
	 * @param flow2
	 */
	public void setFlow2(long flow2) {
		this.flow2 = flow2;
	}

	/**
	 * 
	 * @return flow3
	 */
	public long getFlow3() {
		return flow3;
	}

	/**
	 * 
	 * @param flow3
	 */
	public void setFlow3(long flow3) {
		this.flow3 = flow3;
	}
}

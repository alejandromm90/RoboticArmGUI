package geometric;

/**
 * 
 * @author Philippe Heer
 * 
 */
public class Angles {
	private double thi;
	private double theta;
	private double kappa;

	/**
	 * 
	 * @param thi
	 * @param theta
	 * @param kappa
	 */
	public Angles(double thi, double theta, double kappa) {
		this.thi = thi;
		this.theta = theta;
		this.kappa = kappa;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "thi : " + (int) Math.toDegrees(thi) + "\ntheta : "
				+ (int) Math.toDegrees(theta) + "\nkappa : "
				+ (int) Math.toDegrees(kappa);
	}

	/**
	 * 
	 * @return thi
	 */
	public double getThi() {
		return thi;
	}

	/**
	 * 
	 * @param thi
	 */
	public void setThi(double thi) {
		this.thi = thi;
	}

	/**
	 * 
	 * @return theta
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * 
	 * @param theta
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}

	/**
	 * 
	 * @return kappa
	 */
	public double getKappa() {
		return kappa;
	}

	/**
	 * 
	 * @param kappa
	 */
	public void setKappa(double kappa) {
		this.kappa = kappa;
	}
}

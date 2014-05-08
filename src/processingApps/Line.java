package processingApps;

/**
 * @author Alejandro Moran
 * 
 * */

public class Line {
	
	private double x, y, xE, yE;
	
	public Line (double x, double y, double xE, double yE) {
		this.x = x;
		this.y = y;
		this.xE = xE;
		this.yE = yE;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getxE() {
		return xE;
	}

	public double getyE() {
		return yE;
	}
	
}

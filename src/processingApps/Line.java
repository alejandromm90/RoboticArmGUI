package processingApps;

public class Line {
	
	private float x, y, xE, yE;
	
	public Line (float x, float y, float xE, float yE) {
		this.x = x;
		this.y = y;
		this.xE = xE;
		this.yE = yE;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getxE() {
		return xE;
	}

	public float getyE() {
		return yE;
	}
	
}

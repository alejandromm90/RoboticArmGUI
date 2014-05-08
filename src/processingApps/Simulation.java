package processingApps;

import geometric.Angles;
import geometric.RelativePoint;

import java.util.ArrayList;

import math.Calculate;

import processing.core.PApplet;


import constants.Constants;

/**
 * 
 * @author Philippe Heer
 * 
 */
@SuppressWarnings("serial")
public class Simulation extends PApplet {
	private ArrayList<RelativePoint> points;
	private RelativePoint point;
	private Angles angles;

	/**
	 * 
	 * @param points
	 */
	public Simulation(ArrayList<RelativePoint> points) {
		this.points = points;
	}

	/**
	 * 
	 */
	public void setup() {
		size(Constants.SIZE_WIDTH, Constants.SIZE_HEIGHT);
		background(255);

		stroke(173, 112, 51); // define color
		strokeWeight(17); // define line thickness of drawings, even for a point
		fill(0); // fill circle
		
		point = null;
		angles = null;
	}

	/**
	 * 
	 */
	public void closeApplication() {
		System.exit(0);
	}

	/**
	 *
	 */
	public void draw() {
		background(255);
		
		if (points.size() > 0) {
			RelativePoint point1 = points.remove(0);

			point = new RelativePoint(point1.getX(),
					point1.getY(), point1.getZ());

			angles = Calculate.calculateAngles(point);
		}

			text(angles.toString(), 20, 20);
			text(point.toString(), 20, 80);

			drawArmTop((float) angles.getThi(), (float) angles.getTheta(),
				(float) angles.getKappa());
		drawArmSide((float) angles.getTheta(), (float) angles.getKappa());

		waits();
	}

	/**
	 * 
	 * @param thi
	 * @param theta
	 * @param kappa
	 */
	private void drawArmTop(float thi, float theta, float kappa) {
		float length1 = (float) Constants.ARM_LENGTH_SHOULDER * cos(theta);
		float length2 = (float) Constants.ARM_LENGTH_ELBOW * cos(kappa);

		float x1 = (float) Constants.BASE_X + length1 * cos(thi);
		float y1 = (float) Constants.BASE_Y + length1 * sin(thi);

		float x2 = x1 + length2 * cos(thi);
		float y2 = y1 + length2 * sin(thi);

		// drawPoint((float) Constants.BASE_X, (float) Constants.BASE_Y, length1
		// + length2);
		// drawPoint((float) Constants.BASE_X, (float) Constants.BASE_Y,
		// length1);

		drawLine((float) Constants.BASE_X, (float) Constants.BASE_Y, x1, y1);
		drawLine(x1, y1, x2, y2);
		drawPoint((float) Constants.BASE_X, (float) Constants.BASE_Y, 5);
		drawPoint(x1, y1, 5);
		drawPoint(x2, y2, 5);
	}

	/**
	 * 
	 * @param theta
	 * @param kappa
	 */
	private void drawArmSide(float theta, float kappa) {
		float x1 = (float) Constants.BASE_X
				+ (float) Constants.ARM_LENGTH_SHOULDER * cos(theta);
		float z1 = (float) Constants.BASE_Z
				+ (float) Constants.ARM_LENGTH_SHOULDER * sin(theta);

		float x2 = x1 + (float) Constants.ARM_LENGTH_ELBOW * cos(kappa);
		float z2 = z1 + (float) Constants.ARM_LENGTH_ELBOW * sin(kappa);

		drawLine((float) Constants.BASE_X - 100, (float) Constants.BASE_Z,
				(float) Constants.BASE_X, (float) Constants.BASE_Z);
		drawLine(x2, z2, x2 + 50, z2);

		drawLine((float) Constants.BASE_X, (float) Constants.BASE_Z, x1, z1);
		drawLine(x1, z1, x2, z2);
		drawPoint((float) Constants.BASE_X, (float) Constants.BASE_Z, 5);
		drawPoint(x1, z1, 5);
		drawPoint(x2, z2, 5);
	}

	/**
	 * 
	 * @param x
	 * @param z
	 * @param x2
	 * @param z2
	 */
	private void drawLine(float x, float z, float x2, float z2) {
		line(x, Constants.SIZE_HEIGHT - z, x2, Constants.SIZE_HEIGHT - z2);
	}

	/**
	 * 
	 * @param x
	 * @param z
	 * @param radius
	 */
	private void drawPoint(float x, float z, float radius) {
		float diameter = 2 * radius;
		ellipse(x, Constants.SIZE_HEIGHT - z, diameter, diameter);
	}

	/**
	 * 
 	 */
	private void waits() {
		long i = 0;
		while (i <= Constants.SIMULATION_SPEED) {
			i++;
		}
	}
}

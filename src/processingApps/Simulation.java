package processingApps;

import java.util.ArrayList;

import processing.core.PApplet;
//import processing.serial.Serial; TODO

import constants.Constants;
import constants.Flavour;
import geometric.Angles;
import geometric.RelativePoint;
import math.Calculate;
import math.Move;

/**
 * 
 * @author Philippe Heer
 * 
 */
@SuppressWarnings("serial")
public class Simulation extends PApplet {
	private ArrayList<RelativePoint> points;
	private ArrayList<RelativePoint> pointsDraw;
	private RelativePoint point;
	private Angles angles;

	// private Serial port; TODO

	/**
	 * 
	 * @param points
	 */
	public Simulation(ArrayList<RelativePoint> points) {
		RelativePoint point1 = points.get(points.size() - 1);

		points.add(new RelativePoint(point1.getX(), point1.getY(), point1
				.getZ(), 0, Flavour.CHOCOLATE));

		points.add(new RelativePoint(Constants.START_X, Constants.START_Y,
				Constants.START_Z, 0, Flavour.CHOCOLATE));

		this.points = Move.smoothMovement(Calculate
				.transformRelativePoint(points));
		pointsDraw = new ArrayList<RelativePoint>();
	}

	/**
	 * 
	 */
	public void setup() {
		size(Constants.SIZE_WIDTH, Constants.SIZE_HEIGHT);
		background(255);

		// port = new Serial(this, Serial.list()[0], 19200); TODO

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

			point = new RelativePoint(point1.getX(), point1.getY(),
					point1.getZ(), point1.getFlow(), point1.getFlavour());
		}

		angles = Calculate.calculateAngles(point);

		if (point.getFlow() != 0) {
			pointsDraw.add(point);

			// TODO Here you should let the flavour flow with intesity
			// point.getFlow()
		}

		sendToArduino(angles);

		drawBorder();
		drawParameters();
		drawHistoryPoints();

		drawArmTop((float) angles.getThi(), (float) angles.getTheta(),
				(float) angles.getKappa());
		drawArmSide((float) angles.getTheta(), (float) angles.getKappa());

		delay(Constants.SIMULATION_SPEED);
	}

	/**
	 * 
	 */
	private void drawBorder() {
		noFill();
		rect((float) Constants.BASE_X + Constants.DRAWING_APPLET_RELATIVE_X,
				(float) (Constants.SIZE_HEIGHT - (Constants.BASE_Y + Constants.DRAWING_APPLET_RELATIVE_Y)),
				Constants.DRAWING_APPLET_SIZE_WIDTH,
				(float) -Constants.DRAWING_APPLET_SIZE_HEIGHT);
	}

	/**
	 * 
	 */
	private void drawParameters() {
		fill(0);
		text(angles.toString(), 20, 20);
		text(point.toString(), 20, 80);
	}

	/**
	 * 
	 */
	private void drawHistoryPoints() {
		for (int i = 0; i < pointsDraw.size(); i++) {
			drawFlavourPoint(
					(float) (pointsDraw.get(i).getX() + Constants.BASE_X),
					(float) (pointsDraw.get(i).getY() + Constants.BASE_Y),
					(float) pointsDraw.get(i).getFlow(), pointsDraw.get(i)
							.getFlavour());
		}
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
		stroke(173, 112, 51); // define color
		strokeWeight(17); // define line thickness

		line(x, Constants.SIZE_HEIGHT - z, x2, Constants.SIZE_HEIGHT - z2);
	}

	/**
	 * 
	 * @param x
	 * @param z
	 * @param radius
	 * @param flavour
	 */
	private void drawFlavourPoint(float x, float z, float radius,
			Flavour flavour) {
		switch (flavour) {
		case CHOCOLATE:
			stroke(153, 76, 0);
			fill(153, 76, 0);
			break;
		case VANILLE:
			stroke(255, 255, 0);
			fill(255, 255, 0);
			break;
		case STRAWBERRY:
			stroke(255, 0, 0);
			fill(255, 0, 0);
			break;
		default:
			break;
		}

		strokeWeight(1); // define line thickness

		float diameter = 2 * radius;
		ellipse(x, Constants.SIZE_HEIGHT - z, diameter, diameter);
	}

	/**
	 * 
	 * @param x
	 * @param z
	 * @param radius
	 */
	private void drawPoint(float x, float z, float radius) {
		stroke(0); // define color black
		strokeWeight(1); // define line thickness
		fill(0); // fill circle

		float diameter = 2 * radius;
		ellipse(x, Constants.SIZE_HEIGHT - z, diameter, diameter);
	}

	/**
	 * 
	 * @param angles
	 */
	void sendToArduino(Angles angles) {
		int thi = (int) Math.toDegrees(angles.getThi())
				+ Constants.CORRECT_ANGLE_THI;
		int theta = (int) Math.toDegrees(angles.getTheta())
				+ Constants.CORRECT_ANGLE_THETA;
		int kappa = (int) -Math.toDegrees(angles.getKappa())
				+ Constants.CORRECT_ANGLE_KAPPA;

		// port.write(thi + "a"); TODO
		// port.write(theta + "b");
		// port.write(kappa + "c");
	}
}

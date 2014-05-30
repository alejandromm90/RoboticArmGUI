package processingApps;

import geometric.Angles;
import geometric.RelativePoint;

import java.util.ArrayList;

import ArduinoComm.TalkWithArduino;
import appInterface.MainInterface;
import math.Calculate;
import math.Move;
import processing.core.PApplet;
import processing.serial.Serial;
import constants.Constants;

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
	private long[] flowBefore;
	public static boolean finishPrint = true;
	public static boolean liveMode = false;

	/**
	 * 
	 * @param points
	 */
	public Simulation(ArrayList<RelativePoint> points) {
		if (!points.isEmpty()) {
			
			RelativePoint point1 = points.get(points.size() - 1);
	
			points.add(new RelativePoint(point1.getX(), point1.getY(), point1
					.getZ(), 0, 0, 0));
	
			points.add(new RelativePoint(Constants.START_X, Constants.START_Y,
					Constants.START_Z, 0, 0, 0));
	
			this.points = Move.smoothMovement(Calculate
					.transformRelativePoint(points));
			pointsDraw = new ArrayList<RelativePoint>();
		} else {
			this.points = new ArrayList<RelativePoint>();
			pointsDraw = new ArrayList<RelativePoint>();
		}
	}

	/**
	 * 
	 */
	public void setup() {
		if (liveMode == false) {
			size(Constants.SIZE_WIDTH, Constants.SIZE_HEIGHT);
			background(255);	
		}
		
		try {
			Thread.sleep(100);//TODO maybe there is something better to do than learn to program as indian... 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		flowBefore = new long[Constants.NUMBER_OF_FLAVOURS];
		for (int i = 0; i < flowBefore.length; i++) {
			flowBefore[i] = 0;
		}
		
		point = null;
		angles = null;
		
	}

	/**
	 *
	 */
	public void draw() {
		if (!liveMode) {
			background(255);
	
			if (points.size() > 0) {
				finishPrint = false;
				RelativePoint point1 = points.remove(0);
	
				point = new RelativePoint(point1.getX(), point1.getY(),
						point1.getZ(), point1.getFlow1(), point1.getFlow2(),
						point1.getFlow3());
			} else if (points.size() == 0){
				finishPrint = true;
			}
	
			angles = Calculate.calculateAngles(point);
	
			controlFlow();
			TalkWithArduino.sendToArduino(angles, point.getFlow1(), point.getFlow2(),
					point.getFlow3()); // TODO
	
			if ((point.getFlow1() + point.getFlow2() + point.getFlow3()) > 0) {
				pointsDraw.add(point);
			}
	
			drawBorders();
			drawParameters();
			drawHistoryPoints();
	
			drawArmTop((float) angles.getThi(), (float) angles.getTheta(),
					(float) angles.getKappa());
			drawArmSide((float) angles.getTheta(), (float) angles.getKappa());
	
			delay(Constants.SIMULATION_SPEED);
		} else {
			// Live Mode
			if (points.size() > 0) {
				RelativePoint point1 = points.remove(0);
	
				point = new RelativePoint(point1.getX(), point1.getY(),
						point1.getZ(), point1.getFlow1(), point1.getFlow2(),
						point1.getFlow3());
			
				angles = Calculate.calculateAngles(point);
				
				controlFlow();
				TalkWithArduino.sendToArduino(angles, point.getFlow1(), point.getFlow2(),
						point.getFlow3()); // TODO
				delay(Constants.SIMULATION_SPEED);
			} 
		}
	}

	/**
	 * 
	 */
	private void controlFlow() {
		long flow[] = new long[Constants.NUMBER_OF_FLAVOURS];

		flow[0] = point.getFlow1();
		flow[1] = point.getFlow2();
		flow[2] = point.getFlow3();

		boolean wait = false;

		for (int j = 0; j < flow.length; j++) {
			if ((flowBefore[j] == 0) && (flow[j] != 0)) {
				flow[j] = Constants.FLOW_WAIT_SPEED_START[j];

				wait = true;
			} else if ((flowBefore[j] != 0) && (flow[j] == 0)) {
				flow[j] = -Constants.FLOW_WAIT_SPEED_END[j];

				wait = true;
			}
		}

		if (wait) {
			for (int i = 0; i < flow.length; i++) {
				if ((flow[i] > 0)
						&& (flow[i] < Constants.MAX_ALLOWED_FLOW_BY_USER)) {
					flow[i] = 0;
				}
			}

			TalkWithArduino.sendFlowToArduino(flow[0], flow[1], flow[2]); // TODO

			delay(Constants.FLOW_WAIT);
		}

		flowBefore[0] = point.getFlow1();
		flowBefore[1] = point.getFlow2();
		flowBefore[2] = point.getFlow3();
	}

	/**
	 * 
	 */
	private void drawBorders() {
		noFill();
		rect((float) Constants.BASE_X + Constants.DRAWING_APPLET_RELATIVE_X,
				(float) (Constants.SIZE_HEIGHT - (Constants.BASE_Y + Constants.DRAWING_APPLET_RELATIVE_Y)),
				Constants.DRAWING_APPLET_SIZE_WIDTH,
				(float) -Constants.DRAWING_APPLET_SIZE_HEIGHT);
		rect((float) Constants.BASE_X + Constants.DRAWING_APPLET_RELATIVE_Y,
				(float) (Constants.SIZE_HEIGHT - 10), 300, -300);
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
			drawFlavourPoint((float) (Constants.BASE_X + pointsDraw.get(i)
					.getX()), (float) (Constants.BASE_Y + pointsDraw.get(i)
					.getY()), (float) pointsDraw.get(i).getFlow1(),
					(float) pointsDraw.get(i).getFlow2(), (float) pointsDraw
							.get(i).getFlow3());
			drawFlavourPoint((float) (Constants.BASE_X + Math.sqrt(pointsDraw
					.get(i).getX()
					* pointsDraw.get(i).getX()
					+ pointsDraw.get(i).getY() * pointsDraw.get(i).getY())),
					(float) (Constants.BASE_Z + pointsDraw.get(i).getZ()),
					(float) pointsDraw.get(i).getFlow1(), (float) pointsDraw
							.get(i).getFlow2(), (float) pointsDraw.get(i)
							.getFlow3());
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

		float x1 = (float) (Constants.BASE_X + Constants.BASE_BOTTOM_LENGTH
				* cos(thi));
		float y1 = (float) (Constants.BASE_Y + Constants.BASE_BOTTOM_LENGTH
				* sin(thi));

		float x2 = (float) x1 + length1 * cos(thi);
		float y2 = (float) y1 + length1 * sin(thi);

		float x3 = x2 + length2 * cos(thi);
		float y3 = y2 + length2 * sin(thi);

		float x4 = x3 + (float) Constants.BASE_TOP_LENGTH * cos(thi);
		float y4 = y3 + (float) Constants.BASE_TOP_LENGTH * sin(thi);

		drawLine((float) Constants.BASE_X, (float) Constants.BASE_Y, x1, y1);
		drawLine(x1, y1, x2, y2);
		drawLine(x2, y2, x3, y3);
		drawLine(x3, y3, x4, y4);
		drawPoint((float) Constants.BASE_X, (float) Constants.BASE_Y, 8);
		drawPoint(x1, y1, 5);
		drawPoint(x2, y2, 8);
		drawPoint(x3, y3, 5);
		drawPoint(x4, y4, 8);
	}

	/**
	 * 
	 * @param theta
	 * @param kappa
	 */
	private void drawArmSide(float theta, float kappa) {
		float x1 = (float) Constants.BASE_X
				+ (float) Constants.BASE_BOTTOM_LENGTH;
		float z1 = (float) Constants.BASE_Z;

		float x2 = x1 + (float) Constants.ARM_LENGTH_SHOULDER * cos(theta);
		float z2 = z1 + (float) Constants.ARM_LENGTH_SHOULDER * sin(theta);

		float x3 = x2 + (float) Constants.ARM_LENGTH_ELBOW * cos(kappa);
		float z3 = z2 + (float) Constants.ARM_LENGTH_ELBOW * sin(kappa);

		float x4 = x3 + (float) Constants.BASE_TOP_LENGTH;
		float z4 = z3;

		// Bottom and end paddle
		drawLine((float) Constants.BASE_X - 80, (float) Constants.BASE_Z,
				(float) Constants.BASE_X, (float) Constants.BASE_Z);
		drawLine(x4, z4, x4 + 15, z4);

		drawLine((float) Constants.BASE_X, (float) Constants.BASE_Z, x1, z1);
		drawLine(x1, z1, x2, z2);
		drawLine(x2, z2, x3, z3);
		drawLine(x3, z3, x4, z4);
		drawPoint((float) Constants.BASE_X, (float) Constants.BASE_Z, 8);
		drawPoint(x1, z1, 5);
		drawPoint(x2, z2, 8);
		drawPoint(x3, z3, 5);
		drawPoint(x4, z4, 8);
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
	 * @param flow1
	 * @param flow2
	 * @param flow3
	 */
	private void drawFlavourPoint(float x, float z, float flow1, float flow2,
			float flow3) {
		strokeWeight(1); // define line thickness

		float diameter;

		if (flow1 > 0) {
			stroke(153, 76, 0);
			fill(153, 76, 0);

			diameter = 2 * flow1;
			ellipse(x + Constants.FLOW_SEPARATION_SIZE, Constants.SIZE_HEIGHT
					- z, diameter, diameter);
		}
		if (flow2 > 0) {
			stroke(255, 0, 0);
			fill(255, 0, 0);

			diameter = 2 * flow2;
			ellipse(x - Constants.FLOW_SEPARATION_SIZE, Constants.SIZE_HEIGHT
					- z, diameter, diameter);
		}
		if (flow3 > 0) {
			stroke(255, 255, 0);
			fill(255, 255, 0);

			diameter = 2 * flow3;
			ellipse(x, Constants.SIZE_HEIGHT - z, diameter, diameter);
		}
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
	
	public void addPoint(RelativePoint point) {
		ArrayList<RelativePoint> pts = new ArrayList<RelativePoint>();
		pts.add(point);
//		points = Calculate.transformRelativePoint(pts);
		System.out.println(point);
		points.addAll(Calculate.transformRelativePoint(pts));	
		System.out.println(points+" ddddd");
	}
	
}

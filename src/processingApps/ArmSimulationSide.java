package processingApps;

/**
 * @author Alejandro Moran
 *  
 * */

import java.util.ArrayList;

import math.Calculate;
import math.Point;
import processing.core.PApplet;


public class ArmSimulationSide extends PApplet{
	private float shoulderX, shoulderY, elbowX, elbowY;
	private final int SIZE_WIDTH = 640;
	private final int SIZE_HEIGHT = 320;
	private final int lengthShoulder = SIZE_WIDTH / 2; // By having the length as the half of the window width, we will reach all the points
	private final int lengthElbow = SIZE_WIDTH / 2;
	private float phiAngle, thetaAngle;
	private int pointCounter;
	private ArrayList<Point> points;
	
	public void setup() {
	  size(SIZE_WIDTH, SIZE_HEIGHT);
	  background(255);
	  shoulderX = 640;
	  shoulderY = 300;
	  pointCounter = 0;
	  
		Point p3 = new Point(30, shoulderY, 0);
		Point p4 = new Point(40, shoulderY, 0);
		Point p5 = new Point(50, shoulderY, 0);
		Point p6 = new Point(60, shoulderY, 0);
		Point p7 = new Point(100, shoulderY, 0);
		Point p8 = new Point(120, shoulderY, 0);
		Point p9 = new Point(140, shoulderY, 0);
		points = new ArrayList<Point>();

		points.add(p3);
		points.add(p4);
		points.add(p5);
		points.add(p6);
		points.add(p7);
		points.add(p8);
		points.add(p9);
	}

	public void draw() {
	  background(255);
	  Point newP = points.get(pointCounter);
	  
	  float testXaux = newP.getX() + shoulderX/2;
	  float testYaux = 0;

	  ellipse(testXaux, shoulderY, 10, 10); // We use shoulderY to stay in the Y coordinates
	  testXaux -= shoulderX;

	  Point newPoint = new Point(testXaux, testYaux, 0);
	  thetaAngle = (float) Calculate.calculateAngles(newPoint, lengthShoulder, lengthElbow).getTheta(); //ThetaAngle(testXaux, testYaux);
	  phiAngle = (float) Calculate.calculateAngles(newPoint, lengthShoulder, lengthElbow).getThi(); //calculatePhiAngle(testXaux, testYaux);
//	  thetaAngle = Calculate.calculateThetaAngle(newPoint.getX(), newPoint.getY(), lengthShoulder, lengthElbow);
//	  phiAngle = Calculate.calculatePhiAngle(newPoint.getX(), newPoint.getY(), lengthShoulder, lengthElbow, thetaAngle);
	  
//	  System.out.println("(" + testXaux + ", "+testYaux+")");	  
//	  System.out.println("Phi : " + Math.toDegrees(phiAngle));
//	  System.out.println("Theta 1: " + Math.toDegrees(thetaAngle));// + phiAngle));
//	  float ttt = (float) (360 + Math.toDegrees(thetaAngle));// + phiAngle));
//	  System.out.println("Theta 2: " + ttt);

	  lineAngle(shoulderX, shoulderY,(-1) * phiAngle, lengthShoulder);
	  lineAngle(elbowX, elbowY, (-1) * (thetaAngle + phiAngle), lengthElbow); // + phiAngle)), lengthElbow);
	  
	  waitUntil(1000000000); // wait loop
	  
	  if (pointCounter == points.size() - 1) {
		  pointCounter = 0;
	  } else {
		  pointCounter++;
	  }
	}

	/** Wait method **/
	private void waitUntil(long x) {
		long i = 0;
		while (i <= x) {
			  i++;
		  }
	}
	
	/** Draws a line with angle and length given. Angle in radiant**/
	private void lineAngle(float x, float y, float angle, int length)
	{
	  final int ellipseRadius = 17;
	  stroke(173, 112, 51);
	  strokeWeight(17);

	  line(x, y, x+cos(angle)*length, y-sin(angle)*length);

	  strokeWeight(0);
	  fill(0);
	  ellipse(x, y, ellipseRadius, ellipseRadius);  
	  elbowX = x+cos(angle)*length;
	  elbowY = y-sin(angle)*length;
	  ellipse(elbowX, elbowY, ellipseRadius, ellipseRadius);
	}

}

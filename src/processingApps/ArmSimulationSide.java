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
	private int pointCounter, pointSplitted;
	private ArrayList<Point> points;
	private ArrayList<Point> splittedPoints;
	private boolean allSplitted;
	
	public void setup() {
	  size(SIZE_WIDTH, SIZE_HEIGHT);
	  background(255);
	  shoulderX = 640;
	  shoulderY = 300;
	  pointCounter = 0;
	  pointSplitted = 0;
	  allSplitted = false;
//		Point p1 = new Point(10, shoulderY, 0);
//		Point p3 = new Point(30, shoulderY, 0);
//		Point p4 = new Point(40, shoulderY, 0);
//		Point p5 = new Point(50, shoulderY, 0);
//		Point p6 = new Point(60, shoulderY, 0);
//		Point p7 = new Point(100, shoulderY, 0);
//		Point p8 = new Point(120, shoulderY, 0);
//		Point p9 = new Point(140, shoulderY, 0);
//		points = new ArrayList<Point>();
//		points.add(p1);
//		points.add(p3);
//		points.add(p4);
//		points.add(p5);
//		points.add(p6);
//		points.add(p7);
//		points.add(p8);
//		points.add(p9);
		
		splittedPoints = new ArrayList<Point>();
	}

	
	public void draw() {
	  background(255);
	  drawPoints();
	  splitPoints();
	  Point newP = new Point(SIZE_WIDTH / 2, SIZE_HEIGHT,0);
	  if (!splittedPoints.isEmpty()) {
		  newP = splittedPoints.get(pointCounter);
	  }
	  float testXaux = newP.getX(); 
	  float testYaux = 0;

	  testXaux -= shoulderX;

	  Point newPoint = new Point(testXaux, testYaux, 0);
	  thetaAngle = (float) Calculate.calculateAngles(newPoint, lengthShoulder, lengthElbow).getTheta(); //ThetaAngle(testXaux, testYaux);
	  phiAngle = (float) Calculate.calculateAngles(newPoint, lengthShoulder, lengthElbow).getThi(); //calculatePhiAngle(testXaux, testYaux);

	  lineAngle(shoulderX, shoulderY,(-1) * phiAngle, lengthShoulder);
	  lineAngle(elbowX, elbowY, (-1) * (thetaAngle + phiAngle), lengthElbow); // + phiAngle)), lengthElbow);
	  
	  /* TODO When the printing with the arm will be available, 
	   * we will have to print by lines (by pairs of points) and 
	   * stop printing between each pair.
	   * */ 
	  
	  waitUntil(1000000000); // wait loop
	  
	  if (pointCounter == splittedPoints.size() - 1) {
		  pointCounter = 0;
	  } else {
		  pointCounter++;
	  }
	}

	private void drawPoints() {
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			ellipse(p.getX(), shoulderY, 10, 10);
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

	/** Splits in more points the line between two points **/
	private void splitPoints() { 
		if (!allSplitted && !points.isEmpty()) {
			Point p1 = points.get(pointSplitted);
			Point p2 = null;
			if ((points.size() % 2) != 0) { // Points are not by pairs
				if (pointSplitted == points.size() - 1 && points.size() > 1) { // Means that P1 is the last point and P2 should be P1
					p1 = points.get(points.size() - 2);
					p2 = points.get(points.size() - 1); 
				} else if (points.size() > 1){
					p2 = points.get(pointSplitted + 1);
				} else {
					p2 = p1;
				}
			} else {
				p2 = points.get(pointSplitted + 1);
			}
			// TODO take care with those draws where there are odd points
			splittedPoints.add(p1);
			float p1x = p1.getX(); 
			float p2x = p2.getX();
			if (abs(p1x - p2x) > 10){
				boolean end = false;
				if (p1x - p2x > 0) { // positive means p1x is bigger, its on the right side of the canvas
					// We have to go from right to left by decreasing p1x
					while (!end) {
						p1x -= 10;
						if (p1x > p2x){
							Point p = new Point(p1x, 0, 0);
							splittedPoints.add(p);
						} else {
							end = true;
						}
					}
				} else {
					// We have to go from left to right by increasing p1x
					while (!end) {
						p1x += 10;
						if (p1x < p2x){
							Point p = new Point(p1x, 0, 0);
							splittedPoints.add(p);
						} else {
							end = true;
						}
					}
				}		
			}
			splittedPoints.add(p2);
			pointSplitted += 2;

			if ((points.size() % 2) != 0) {
				if (pointSplitted > points.size()) {
					allSplitted = true;
				} 
			} else {
				if (pointSplitted >= points.size()-1) {
					allSplitted = true;
				} 
			}
		}
	}
	
	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
}

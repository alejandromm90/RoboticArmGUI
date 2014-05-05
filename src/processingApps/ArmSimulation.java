package processingApps;
import processing.core.PApplet;


public class ArmSimulation extends PApplet{
	private float shoulderX, shoulderY, elbowX, elbowY;
	private final int lengthShoulder = 150;
	private final int lengthElbow = 150;
	private float phiAngle, thetaAngle;

	public void setup() {
	  size(640, 360);
	  background(255);
	  shoulderX = 320;
	  shoulderY = 300;
	}

	public void draw() {
	  background(255);
	  float testXaux = 200;
	  float testYaux = 200;
	  if (mousePressed == true) {
	   testXaux = mouseX;
	   testYaux = mouseY;
	  }
	  ellipse(testXaux, testYaux, 10, 10); 
	  testXaux -= shoulderX;
	  testYaux -= shoulderY;
	  
	  thetaAngle = calculateThetaAngle(testXaux, testYaux);
	  phiAngle = calculatePhiAngle(testXaux, testYaux);
//	  System.out.println("(" + testXaux + ", "+testYaux+")");	  
//	  System.out.println("Phi : " + Math.toDegrees(phiAngle));
//	  System.out.println("Theta 1: " + Math.toDegrees(thetaAngle));// + phiAngle));
//	  float ttt = (float) (360 + Math.toDegrees(thetaAngle));// + phiAngle));
//	  System.out.println("Theta 2: " + ttt);
	  

	  lineAngle(shoulderX, shoulderY, (-1) * phiAngle, lengthShoulder);
	  lineAngle(elbowX, elbowY, (-1) * (thetaAngle + phiAngle), lengthElbow); // + phiAngle)), lengthElbow);
	  
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

	private float calculatePhiAngle(float x, float y) {
		float phi;
		phi = atan2(y,x) - atan2(lengthElbow * sin(thetaAngle), lengthShoulder + lengthElbow*cos(thetaAngle));
		return phi;
	}
	
	private float calculateThetaAngle(float x, float y) {
		float theta;
		theta = 2 * atan(-sqrt(((lengthElbow + lengthShoulder) * (lengthElbow + lengthShoulder) - (x*x + y*y))
				/ ((x*x + y*y) - (lengthElbow - lengthShoulder) * (lengthElbow - lengthShoulder))));
		return theta;
	}

	private void moveArm() {
	    
	}
}

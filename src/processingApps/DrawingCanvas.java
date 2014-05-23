package processingApps;

/**
 * @author Alejandro Moran
 * 
 * */

import geometric.RelativePoint;

import java.util.ArrayList;

import leapMotion.LeapMotionListener;
import processing.core.PApplet;
import appInterface.Flavor;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.Vector;

import constants.Constants;

public class DrawingCanvas extends PApplet{
	private double lastX, lastY;
	private int width = 682;
	private int height = 270;
	private boolean firstClick, leapMotionMode;
	private ArrayList<RelativePoint> lastEllipse, ellipsesRedo, ellipsesLeapMotion;
	private ArrayList<Line> lastLine, linesRedo;
	private Controller leapController;
	private LeapMotionListener listener;
	private ArrayList<Flavor> flavors;
	private Controller controller;
	private int actualFlow1, actualFlow2, actualFlow3;
	
	public void setup() {
	  frameRate(30);
	  size(width, height);

	  firstClick = true;
	  lastEllipse = new ArrayList<RelativePoint>();
	  lastLine = new ArrayList<Line>();
	  ellipsesRedo = new ArrayList<RelativePoint>();
	  linesRedo = new ArrayList<Line>();
  	  ellipsesLeapMotion = new ArrayList<RelativePoint>();

	  leapController = new Controller();
	  flavors = new ArrayList<Flavor>();
	  flavors.add(new Flavor("chocolate"));	  
	  flavors.add(new Flavor("strawberry"));
	  flavors.add(new Flavor("chocStraw"));

	  initializeLeapMotionListener();
	  
	  actualFlow1 = Constants.DEFAULT_FLOW;
	  actualFlow2 = 0;
	  actualFlow3 = 0;

//	  leapMotionMode = true;
	  drawBorder();
	  strokeWeight(Constants.DEFAULT_FLOW - 5);

	}
	
	
	public void draw() { 

	  if (leapMotionMode) { 
		  drawWithLeapMotion();	  
	  } else {
		  drawWithMouse();
	  }
	}

	
	private void drawWithMouse() {
		if (mousePressed == true) {
			if (insideCanvas(mouseX, mouseY)) {
				if (actualFlow1 > 0 || actualFlow2 > 0 || actualFlow3 > 0) {
				    if (firstClick) {
				      lastX = mouseX;  
				      lastY = mouseY;
				      
				    } else {
				    	if ((mouseX != lastX) && (mouseY != lastY)) {
				    		addLineToArray(mouseX, mouseY, lastX, lastY);
						}
				      // draw line between points
				      drawLine(mouseX, mouseY,(float)lastX, (float)lastY, actualFlow1, actualFlow2, actualFlow3);
					  
					  lastX = mouseX;
					  lastY = mouseY;
				  }
				    
				    firstClick = false;
				  
				drawFlavourPoint(mouseX, mouseY, actualFlow1, actualFlow2, actualFlow3);
			    addEllipseToArray(mouseX, mouseY, actualFlow1, actualFlow2, actualFlow3);    
				}
			}
		}
	}


	/** Checks if the mouse point is inside the arm range **/
	private boolean insideCanvas(int mouseX, int mouseY) {
		// Converts coordinates to the same that are in the arm canvas
		int x = mouseX + Constants.DRAWING_APPLET_RELATIVE_X;// - 80; 
		int y = Constants.DRAWING_APPLET_SIZE_HEIGHT - mouseY + 80;
		
		double ratio = Constants.DRAWING_APPLET_SIZE_WIDTH/2; // 341
		double xCenter = 0;
		double yCenter = 0;

		if (Math.sqrt (((xCenter-x)*(xCenter-x)) + ((yCenter-y)*(yCenter-y))) <= ratio) { 
			return true;
		} else { 
			return false;
		}
	}


	private void drawWithLeapMotion() {
		  Frame frame = leapController.frame();
	        
//          if (!frame.hands().isEmpty()) {
//            // Get the first hand
//            Hand hand = frame.hands().get(0);
//
//            // Check if the hand has any fingers
//            FingerList fingers = hand.fingers();
//            
//            if(fingers.count() == 1) {
		  
			  Pointable pointer = frame.pointables().frontmost();
			  if( pointer.isValid()) {
			    int frontColor = color( 255, 0, 0, 0 );
	
			    InteractionBox iBox = frame.interactionBox();
			    Vector tip = iBox.normalizePoint(pointer.tipPosition());
			    fingerPaint(tip, frontColor);
			    waits(8000000);
			  }
//            }
//          }
	}
	
	private boolean addLineToArray(double mX, double mY, double lX, double lY) {
		if (lastLine.isEmpty()) {
			  lastLine.add(new Line(mX, mY, lX, lY));
		  } else if ((lastLine.get(lastLine.size()-1).getX() != mX) && (lastLine.get(lastLine.size()-1).getY() != mY)
				  && (lastLine.get(lastLine.size()-1).getxE() != lX) && (lastLine.get(lastLine.size()-1).getyE() != lY)) {
			  lastLine.add(new Line(mX, mY, lX, lY));
		  }
		return true;
	}
	
	private void addEllipseToArray(double mX, double mY, int flow1, int flow2, int flow3) {
//		if (!lastEllipse.isEmpty()) {
//	    	if ((lastEllipse.get(lastEllipse.size() - 1).getX() != mX) && (lastEllipse.get(lastEllipse.size() - 1).getY() != mY)) {
//	    		lastEllipse.add(new Point(mX, mY, 0));
//	    	}
//	    } else {
//	    	lastEllipse.add(new Point(mouseX, mouseY, 0));
//	    }
		lastEllipse.add(new RelativePoint(mX, mY, -70, flow1, flow2, flow3));	// Z = -70

	}

	/**
	 * Draws with Leap Motion tracking the finger
	**/
	private void fingerPaint(Vector tip, int paintColor) {
		final int MAXBRUSHSIZE = 240;
	    float x = tip.getX() * width;
	    float y = height - tip.getY() * height;
	    float brushSize = MAXBRUSHSIZE - MAXBRUSHSIZE * tip.getZ();
	    int flow = Constants.DEFAULT_FLOW;
	    
	    boolean drawing = false;
    	int numberFlavor = listener.getNumberFlavor();
    	if (insideCanvas((int)x, (int)y)) {
		    System.out.println(brushSize);
		    if (brushSize > 210 && brushSize <= MAXBRUSHSIZE) {
		    	flow = 20;
		    } else if (brushSize > 170 && brushSize <= 210) {
		    	flow = 15;
		    } else if (brushSize > 140 && brushSize <= 170) {
		    	flow = 10;
		    } 
		    
		    if (brushSize > 140) {
		    	// We are drawing
		        controller.removeListener(listener);	// Remove the Leap Motion listener to avoid gestures mistakes
		        RelativePoint newPoint = null;
		        switch (numberFlavor) {
		        case 0:	// Chocolate
		        	newPoint = new RelativePoint(x, y, 0, flow, 0, 0);
			    	drawFlavourPoint(x, y, flow, 0, 0);
		        	break;
		        case 1:	// Strawberry
		        	newPoint = new RelativePoint(x, y, 0, 0, flow, 0);
			    	drawFlavourPoint(x, y, 0, flow, 0);
		        	break;
		        case 2:	// Chocolate + Strawberry
		        	newPoint = new RelativePoint(x, y, 0, flow, flow, 0);
			    	drawFlavourPoint(x, y, flow, flow, 0);
		        	break;
		        }
		    	
		    	ellipsesLeapMotion.add(newPoint);
		    	drawing = true;
		    	
		    } else {
		    	// We just draw the brush (mouse)
		        controller.addListener(listener);	// Add the Leap Motion listener to enable gestures recognition
		    	drawBorder();
		    	switch(numberFlavor) {
		    	case 0:	// Chocolate
			    	drawFlavourPoint(x, y, Constants.DEFAULT_FLOW, 0, 0);
		        	break;
		        case 1:	// Strawberry
			    	drawFlavourPoint(x, y, 0, Constants.DEFAULT_FLOW, 0);
		        	break;
		        case 2:	// Chocolate + Strawberry
			    	drawFlavourPoint(x, y, Constants.DEFAULT_FLOW, Constants.DEFAULT_FLOW, 0);
		        	break;
		    	}
		    	repaintCanvas(ellipsesLeapMotion, new ArrayList<Line>(), false);
		    }
	    
		    if(!drawing) {
		    	addLastPoint();
		    }
    	}
	}
	
	/** Initialize the Leap Motion Listener and its Controller when it is called **/
	private void initializeLeapMotionListener() {
		// Create a sample listener and controller
		listener = new LeapMotionListener(flavors);
		controller = new Controller();
		
        // Have the listener receive events from the controller
        controller.addListener(listener);
	}

	/** It adds a Flow 0 point to the array **/
	private void addLastPoint() {
    	if (!ellipsesLeapMotion.isEmpty()) {
    		RelativePoint lastPoint = ellipsesLeapMotion.get(ellipsesLeapMotion.size()-1);
	    	if (lastPoint.getFlow1() > 0 || lastPoint.getFlow2() > 0 || lastPoint.getFlow3() > 0) {
		    	RelativePoint newPoint = new RelativePoint(lastPoint.getX(), lastPoint.getY(), lastPoint.getZ(), 0, 0, 0);
		    	ellipsesLeapMotion.add(newPoint);
	    	}
    	}
	}
	
	/** Method to wait and control better the leap drawing 
	 * @args waitTime
	 **/
	private void waits(long waitTime) {
		long i = 0;
		while (i <= waitTime) {
			i++;
		}
	}
	
	public void setLeapMotionMode(boolean activate) {
		cleanCanvas();
		leapMotionMode = activate;
	}
	
	
	public void cleanCanvas() {
		drawBorder();
		setActualFlow(actualFlow1, 0, 0); // actualFlow1, only chocolate by defaults flow and flavour
		firstClick = true;
		lastEllipse = new ArrayList<RelativePoint>();
		lastLine = new ArrayList<Line>();
		ellipsesRedo = new ArrayList<RelativePoint>();
		linesRedo = new ArrayList<Line>();
		ellipsesLeapMotion = new ArrayList<RelativePoint>();
	}
	
	public void undoCanvas() {
		drawBorder();
		if (!lastEllipse.isEmpty()) {
			
			RelativePoint undoEllipse = lastEllipse.remove(lastEllipse.size() - 1);
			if (!ellipsesRedo.contains(undoEllipse)) {
				ellipsesRedo.add(undoEllipse);
			}
			if (!lastLine.isEmpty()) {
				Line undoLine = lastLine.remove(lastLine.size() - 1); 
				if (!linesRedo.contains(undoLine)) {
					linesRedo.add(undoLine);
				}
			}
			if (lastEllipse.isEmpty()) {
				firstClick = true;
			} else {
				repaintCanvas(lastEllipse, lastLine, false);
			}	
		}	
	}
	
	public void redoCanvas() {
		drawBorder();
		firstClick = false;
		
		if (lastEllipse.isEmpty()) {
			repaintCanvas(ellipsesRedo, new ArrayList<Line>(), true);
			if (!ellipsesRedo.isEmpty()) {
				lastEllipse.add(ellipsesRedo.remove(ellipsesRedo.size() - 1));
			}
		} else {
			repaintCanvas(lastEllipse, lastLine, false);

			repaintCanvas(ellipsesRedo, linesRedo, true);
		
			if (!ellipsesRedo.isEmpty()) {
				lastEllipse.add(ellipsesRedo.remove(ellipsesRedo.size() - 1));
			}
			if (!linesRedo.isEmpty()) {
				lastLine.add(linesRedo.remove(linesRedo.size() - 1));
			}
		}
	}
	
	private void repaintCanvas(ArrayList<RelativePoint> ellipses, ArrayList<Line> lines, boolean redo) {
		int i = 0;
		if (redo && !ellipses.isEmpty()) {
			i = ellipses.size() - 1;
		}
		while (i < ellipses.size()) {
			RelativePoint lastE = ellipses.get(i);
			if (lastE.getFlow1() > 0 || lastE.getFlow2() > 0 || lastE.getFlow3() > 0) {
				
				Line lastL = null;
				if (i < lines.size()) {
					lastL = lines.get(i);
				}
				
				drawFlavourPoint((float)lastE.getX(), (float)lastE.getY(), lastE.getFlow1(), lastE.getFlow2(), lastE.getFlow3());
//				ellipse((float)lastE.getX(), (float)lastE.getY(), 5, 5);
				if (lastL != null) {
					line((float)lastL.getX(), (float)lastL.getY(), (float)lastL.getxE(), (float)lastL.getyE());
				}
	
				lastX = lastE.getX();
				lastY = lastE.getY();
				
			}
			i++;

		}
	}
	
	private void drawBorder() {
	  background(162); // gray color
	  stroke(0); // black color line
	  strokeWeight(2);  // Increases the weight of the line
	  fill(255);	// fill the ellipse with white
	  ellipseMode(CENTER);
	  ellipse(width/2, height + 80, width, width);
	  line(0, 0, width, 0);
	  line(0, 0, 0, height-1);
	  line(0, height-1, width-1, height-1);
	  line(width-1, 0, width-1, height-1);
	}
	
	/**
	 * Draws a point between two coordinates with the color of the flavours that are selected.  
	 * @param x
	 * @param y
	 * @param flow1
	 * @param flow2
	 * @param flow3
	 */
	private void drawFlavourPoint(float x, float y, long flow1, long flow2, long flow3) {
		
		if (flow1 > 0) { // Chocolate
			stroke(153, 76, 0);
			fill(153, 76, 0);
			float diameter = flow1; 
			ellipse(x, y, diameter, diameter);
			
		} if (flow2 > 0) { // Strawberry
			stroke(255, 0, 0);
			fill(255, 0, 0);
			float diameter = flow2;
			if (flow1 > 0 || flow3 > 0) { // if we have other flavours, we modify a bit to see better both points
				x += 5;
				y += 5;
			}
			ellipse(x, y, diameter, diameter);
			
		}
	}
	
	/** Draws a line between two points. Uses as many flavours as positive flows there are **/
	private void drawLine(int mouseX, int mouseY, float lastX, float lastY,
			int flow1, int flow2, int flow3) {
		
		if (flow1 > 0) { // Chocolate
			stroke(153, 76, 0);
			fill(153, 76, 0);
			line(mouseX, mouseY,(float)lastX, (float)lastY);
			
		} if (flow2 > 0) { // Strawberry
			stroke(255, 0, 0);
			fill(255, 0, 0);
			if (flow1 > 0 || flow3 > 0) { // if we have other flavours, we modify a bit to see better both lines
				mouseX += 5;
				mouseY += 5;
				lastX += 5;
				lastY += 5;
			}
			line(mouseX, mouseY,(float)lastX, (float)lastY);
		}
	}

	/** Sets the actual flavour's flow
	 * @params flavour
	 * @params flow
	 * **/
	public void setActualFlavour(int flavour, int flow) {
		
		switch(flavour) {
		case 1:	// Chocolate
			actualFlow1 = flow;
			break;
		case 2:	// Strawberry
			actualFlow2 = flow;
			break;
		case 4:	// Chocolate + Strawberry
			actualFlow1 = flow;
			actualFlow2 = flow;
			break;
		}	 
		
		if (!firstClick) {	// We add a last point to the array with the actual flavour's flow
			addEllipseToArray(lastEllipse.get(lastEllipse.size()-1).getX(), lastEllipse.get(lastEllipse.size()-1).getY(),
    				actualFlow1, actualFlow2, actualFlow3); 
		}
	}
	
	/** Splits the drawn figure to start a new one in the same canvas **/
	public void splitDraw() {
		if (!firstClick) {	// We add a last point to the array with the flow 0 value
		    addEllipseToArray(lastEllipse.get(lastEllipse.size()-1).getX(), lastEllipse.get(lastEllipse.size()-1).getY(),
		    				0, 0, 0); 
		    firstClick = true;
		}
	}
	
	/** Changes the flow 
	 * @param flow the quantity of flow to split
	 * @param flavour to select which flavour flow we want to set
	 * **/
	public void setActualFlow(int flow1, int flow2, int flow3) {
		
		if (!lastEllipse.isEmpty()) {	// if is not the first point, we add a new one with the new flow value
			RelativePoint lastPoint = lastEllipse.get(lastEllipse.size()-1);
			if (flow1 > 0) {	// Chocolate
				actualFlow1 = flow1;
				lastPoint.setFlow1(flow1);
				strokeWeight(flow1 - 5);

			} if (flow2 > 0) {	// Strawberry
				actualFlow2 = flow2;
			 	lastPoint.setFlow2(flow2);
				strokeWeight(flow2 - 5);

			} if (flow3 > 0) {	// Other flavour
				actualFlow3 = flow3;
				lastPoint.setFlow3(flow3);
				strokeWeight(flow3 - 5);

			}
		}
	}
	
	/** Returns an ArrayList of points **/
	public ArrayList<RelativePoint> getPoints() {	
		if (!lastEllipse.isEmpty()) {
			return lastEllipse;
		} else {
			return ellipsesLeapMotion;
		}
	}
	
}

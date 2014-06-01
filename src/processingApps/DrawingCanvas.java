package processingApps;

/**
 * @author Alejandro Moran
 * 
 * */

import geometric.RelativePoint;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import leapMotion.LeapMotionListener;
import math.Calculate;
import processing.core.PApplet;
import appInterface.Flavor;
import appInterface.SetZvalue;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.Vector;

import constants.Constants;

public class DrawingCanvas extends PApplet{
	private static final long serialVersionUID = 1L;
	private static final int MAX_BRUSH_SIZE = 260;
	private static final int MIN_BRUSH_SIZE = 160;
	private double lastX, lastY;
	private int width = Constants.DRAWING_APPLET_SIZE_WIDTH * 2;
	private int height = Constants.DRAWING_APPLET_SIZE_HEIGHT * 2;
	private boolean firstClick, leapMotionMode, liveMode;
	private ArrayList<RelativePoint> lastEllipse, ellipsesRedo, ellipsesLeapMotion;
	private ArrayList<Line> lastLine, linesRedo;
	private Controller leapController;
	private LeapMotionListener listener;
	private ArrayList<Flavor> flavors;
	private Controller controller;
	private int actualFlow1, actualFlow2, actualFlow3;
	private processing.core.PApplet simulationPApplet;
	private int mode;
	private float diameter;
	private JFrame simJFrame;
	
	public DrawingCanvas(int mode, float diameter) {	// Mode for drawing the background shape
		this.mode = mode;
		this.diameter = Calculate.transformToPixels(diameter);
		firstClick = true;
		lastEllipse = new ArrayList<RelativePoint>();
		lastLine = new ArrayList<Line>();
		ellipsesRedo = new ArrayList<RelativePoint>();
		linesRedo = new ArrayList<Line>();
	  	ellipsesLeapMotion = new ArrayList<RelativePoint>();
	  	  
		// Chocolate by default
		actualFlow1 = Constants.DEFAULT_FLOW;
		actualFlow2 = 0;
		actualFlow3 = 0;
	}
	
	public void setup() {
	  frameRate(30);
	  size(width, height);

//	  firstClick = true;
//	  lastEllipse = new ArrayList<RelativePoint>();
//	  lastLine = new ArrayList<Line>();
//	  ellipsesRedo = new ArrayList<RelativePoint>();
//	  linesRedo = new ArrayList<Line>();
//  	  ellipsesLeapMotion = new ArrayList<RelativePoint>();

	  leapController = new Controller();
	  flavors = new ArrayList<Flavor>();
	  flavors.add(new Flavor("chocolate"));	  
	  flavors.add(new Flavor("strawberry"));
	  flavors.add(new Flavor("chocStraw"));

	  initializeLeapMotionListener();
	  
//	  actualFlow1 = Constants.DEFAULT_FLOW;
//	  actualFlow2 = 0;
//	  actualFlow3 = 0;

	  liveMode = false;
	  
//	  leapMotionMode = true;
	  drawBorder();
	  strokeWeight(Constants.DEFAULT_FLOW - 5);

	}
	
	
	public void draw() { 
		line(0, this.size().height/2, this.size().width, this.size().height/2);
		line(this.size().width/2, 0, this.size().width/2, this.size().height);

	  if (leapMotionMode) { 
		  drawWithLeapMotion();	  
	  } else {
		  drawWithMouse();
	  }
	}

	/********************************************************************************
	 * 								Mouse Drawing Mode								*
	 * 																				*
	 * ******************************************************************************/
	
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
				
				} else if (liveMode) {
				    addEllipseToArray(mouseX, mouseY, actualFlow1, actualFlow2, actualFlow3);    
				}
			}
		}
	}

	
	/********************************************************************************
	 * 								Leap Motion Drawing Mode						*
	 * 																				*
	 * ******************************************************************************/
	
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
	
	/**
	 * Draws with Leap Motion tracking the finger
	**/
	private void fingerPaint(Vector tip, int paintColor) {
		
	    float x = tip.getX() * width;
	    float y = height - tip.getY() * height;
	    float brushSize = MAX_BRUSH_SIZE - MAX_BRUSH_SIZE * tip.getZ();
	    int flow = Constants.DEFAULT_FLOW;
	    
	    boolean drawing = false;
    	int numberFlavor = listener.getNumberFlavor();
    	if (insideCanvas((int)x, (int)y)) {
		    System.out.println(brushSize);
		   
		    
		    if (brushSize > MIN_BRUSH_SIZE) {
		    	// We are drawing
		    	// We set the flow value depending on the distance to the screen
		    	if (brushSize > 240 && brushSize <= MAX_BRUSH_SIZE) {
			    	flow = 20;
			    } else if (brushSize > 180 && brushSize <= 240) {
			    	flow = 15;
			    } else if (brushSize > MIN_BRUSH_SIZE && brushSize <= 180) {
			    	flow = 10;
			    } 
		    	 
		        controller.removeListener(listener);	// Remove the Leap Motion listener to avoid gestures mistakes
		        RelativePoint newPoint = null;
		        if (!newPointEqualToLast(ellipsesLeapMotion, x, y)) {	// We check if the point is the same to the last one
			        switch (numberFlavor) {
			        case 0:	// Chocolate
			        	newPoint = new RelativePoint(x, y, SetZvalue.getZValue(), flow, 0, 0);
				    	drawFlavourPoint(x, y, flow, 0, 0);
			        	break;
			        case 1:	// Strawberry
			        	newPoint = new RelativePoint(x, y, SetZvalue.getZValue(), 0, flow, 0);
				    	drawFlavourPoint(x, y, 0, flow, 0);
			        	break;
			        case 2:	// Chocolate + Strawberry
			        	newPoint = new RelativePoint(x, y, SetZvalue.getZValue(), flow, flow, 0);
				    	drawFlavourPoint(x, y, flow, flow, 0);
			        	break;
			        }
			    	newPoint.setX(x/2);
			    	newPoint.setY(y/2);
			    	
			    	if (liveMode) {	// We check if we are in Live Mode
				    	ArrayList<RelativePoint> pts = new ArrayList<RelativePoint>();
						pts.add(newPoint);				
						((Simulation) simulationPApplet).addPoint(pts);
					}

			    	ellipsesLeapMotion.add(newPoint);	
		        } 
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
		    	repaintCanvas(ellipsesLeapMotion, new ArrayList<Line>(), false, true);	// Leap mode true
		    }
	    
		    if(!drawing) {
		    	addLastPoint();
		    	if (liveMode) {
			    	ArrayList<RelativePoint> pts = new ArrayList<RelativePoint>();
					((Simulation) simulationPApplet).addPoint(pts);
		    	}
		    }
    	}
	}
	
	/** Returns true if the last point added is equal to the new one
	 *  @params x
	 *  @params y
	**/
	private boolean newPointEqualToLast(ArrayList<RelativePoint> pointList, float x, float y) { 
		if (!pointList.isEmpty()) {
			RelativePoint lastPoint = pointList.get(pointList.size()-1);
	        if (((int)lastPoint.getX() == (int)x) && ((int)lastPoint.getY() == (int)y)) {
	        	return true;
	        	
	        } else return false;
		} else return false;
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
	
	/** Initialize the Leap Motion Listener and its Controller when it is called **/
	private void initializeLeapMotionListener() {
		// Create a sample listener and controller
		listener = new LeapMotionListener(flavors);
		controller = new Controller();
		
        // Have the listener receive events from the controller
        controller.addListener(listener);
	}
	
	
	/********************************************************************************
	 * 								Auxiliary functions 							*
	 * 																				*
	 * ******************************************************************************/
	
	/** Checks if two points are inside the arm range **/
	private boolean insideCanvas(int mouseX, int mouseY) {
		// Converts coordinates to the same that are in the arm canvas
		int x = mouseX + Constants.DRAWING_APPLET_RELATIVE_X * 2;// - 80; 
		int y = Constants.DRAWING_APPLET_SIZE_HEIGHT * 2 - mouseY + (80 * 2);
		double ratio = 0;
		double xCenter = 0;
		double yCenter = 0;

		switch(mode) {
		case 0:
			ratio = Constants.DRAWING_APPLET_SIZE_WIDTH; // 642
			break;
		case 1:
			ratio = diameter;
			xCenter = 0;
			yCenter = Constants.DRAWING_APPLET_SIZE_HEIGHT + (80 * 2);	
			break;
		}
		if (Math.sqrt (((xCenter-x)*(xCenter-x)) + ((yCenter-y)*(yCenter-y))) <= ratio) { 
			return true;
		} else { 
			return false;
		}
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
		RelativePoint newPoint = new RelativePoint(mX/2, mY/2, SetZvalue.getZValue(), flow1, flow2, flow3);	// Z = -50
		System.out.println(newPoint);

		if (liveMode) {	// We check if we are in Live Mode
			if (!newPointEqualToLast(lastEllipse, (float)newPoint.getX(), (float)newPoint.getY())) {
		    	ArrayList<RelativePoint> pts = new ArrayList<RelativePoint>();
				pts.add(newPoint);				
				((Simulation) simulationPApplet).addPoint(pts);
			}
		} 
		if (newPoint.getFlow1() > 0 || newPoint.getFlow2() > 0 || newPoint.getFlow3() > 0)	// To not add points when we are in Live Mode without flavour
			lastEllipse.add(newPoint);	
		
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
		strokeWeight(Constants.DEFAULT_FLOW - 5);
		setActualFlow(actualFlow1, 0, 0); // actualFlow1, only chocolate by defaults flow and flavour
		firstClick = true;
		lastEllipse = new ArrayList<RelativePoint>();
		lastLine = new ArrayList<Line>();
		ellipsesRedo = new ArrayList<RelativePoint>();
		linesRedo = new ArrayList<Line>();
		ellipsesLeapMotion = new ArrayList<RelativePoint>();
	}
	
	
	/** UNDO function. Not used in the end **/
//	public void undoCanvas() {
//		drawBorder();
//		if (!lastEllipse.isEmpty()) {
//			
//			RelativePoint undoEllipse = lastEllipse.remove(lastEllipse.size() - 1);
//			if (!ellipsesRedo.contains(undoEllipse)) {
//				ellipsesRedo.add(undoEllipse);
//			}
//			if (!lastLine.isEmpty()) {
//				Line undoLine = lastLine.remove(lastLine.size() - 1); 
//				if (!linesRedo.contains(undoLine)) {
//					linesRedo.add(undoLine);
//				}
//			}
//			if (lastEllipse.isEmpty()) {
//				firstClick = true;
//			} else {
//				repaintCanvas(lastEllipse, lastLine, false, false);
//			}	
//		}	
//	}
	
	/** REDO function. Not used in the end **/
//	public void redoCanvas() {
//		drawBorder();
//		firstClick = false;
//		
//		if (lastEllipse.isEmpty()) {
//			repaintCanvas(ellipsesRedo, new ArrayList<Line>(), true, false);
//			if (!ellipsesRedo.isEmpty()) {
//				lastEllipse.add(ellipsesRedo.remove(ellipsesRedo.size() - 1));
//			}
//		} else {
//			repaintCanvas(lastEllipse, lastLine, false, false);
//
//			repaintCanvas(ellipsesRedo, linesRedo, true, false);
//		
//			if (!ellipsesRedo.isEmpty()) {
//				lastEllipse.add(ellipsesRedo.remove(ellipsesRedo.size() - 1));
//			}
//			if (!linesRedo.isEmpty()) {
//				lastLine.add(linesRedo.remove(linesRedo.size() - 1));
//			}
//		}
//	}
	
	private void repaintCanvas(ArrayList<RelativePoint> ellipses, ArrayList<Line> lines, boolean redo, boolean leapMode) {
		int i = 0;
		double x = 0;
		double y = 0;
		if (redo && !ellipses.isEmpty()) {
			i = ellipses.size() - 1;
		}
		while (i < ellipses.size()) {
			RelativePoint lastE = ellipses.get(i);
			if (leapMode) {
				x = lastE.getX() * 2;
				y = lastE.getY() * 2;	// We multiply by 2 because we added them divided by 2
			} else {
				x = lastE.getX();
				y = lastE.getY();
			}
				
			if (lastE.getFlow1() > 0 || lastE.getFlow2() > 0 || lastE.getFlow3() > 0) {
				
				Line lastL = null;
				if (i < lines.size()) {
					lastL = lines.get(i);
				}
				
				drawFlavourPoint((float)x, (float)y, lastE.getFlow1(), lastE.getFlow2(), lastE.getFlow3());
//				ellipse((float)lastE.getX(), (float)lastE.getY(), 5, 5);
				if (lastL != null) {
					line((float)lastL.getX(), (float)lastL.getY(), (float)lastL.getxE(), (float)lastL.getyE());
				}
	
				lastX = x;
				lastY = y;
			}
			i++;

		}
	}
	
	private void drawBorder() {
	  background(162); // gray color
	  stroke(0); // black color line
	  strokeWeight(2);  // Increases the weight of the line
	  fill(255);	// fill the ellipse with white
	  
	  switch(mode) {
	  case 0:	// Half ellipse mode
		  ellipseMode(CENTER);
		  ellipse(width/2, height + (80 * 2), width, width);
		  break;
	  case 1:	// Cake mode
		  ellipseMode(CENTER);
		  ellipse(width/2, height/2, diameter * 2, diameter * 2);
		  break;
	  }

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
				lastPoint.setFlow1(flow1);

			} if (flow2 > 0) {	// Strawberry
			 	lastPoint.setFlow2(flow2);

			} if (flow3 > 0) {	// Other flavour
				lastPoint.setFlow3(flow3);
			}
		}
		
		// We change the color and set the actualFlow values
		if (flow1 > 0) {	// Chocolate
			actualFlow1 = flow1;
			strokeWeight(flow1 - 5);

		} if (flow2 > 0) {	// Strawberry
			actualFlow2 = flow2;
			strokeWeight(flow2 - 5);

		} if (flow3 > 0) {	// Other flavour
			actualFlow3 = flow3;
			strokeWeight(flow3 - 5);
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
	
	/** Sets the live mode **/
	public void setLiveMode(boolean liveMode) {
		
		if (liveMode) {
			if (!lastEllipse.isEmpty()) {
				simulationPApplet = new Simulation(Calculate.transformCoordinates(lastEllipse));
			} else if (!ellipsesLeapMotion.isEmpty()) {
				simulationPApplet = new Simulation(Calculate.transformCoordinates(ellipsesLeapMotion));
			} else { // There are no points yet
				simulationPApplet = new Simulation(new ArrayList<RelativePoint>());
			}
			
			simJFrame = new JFrame();
			simJFrame.setTitle("ARM SIMULATION");
					
	        JPanel mainPanel = new JPanel();
			JPanel printPanel = new JPanel();
			printPanel.setBounds(20, 20, 600, 600);
			printPanel.setVisible(true);
			printPanel.add(simulationPApplet);
			
			mainPanel.add(printPanel);

			simJFrame.setContentPane(mainPanel);
			simulationPApplet.init(); 
			
			simJFrame.setSize(0,0);
			simJFrame.setEnabled(true);
			simJFrame.setVisible(true);

		} else {
			if (simulationPApplet != null) {
				ArrayList<RelativePoint> pts = new ArrayList<RelativePoint>();
				((Simulation) simulationPApplet).addPoint(pts);
			}
		}
		Simulation.liveMode = liveMode;
		this.liveMode = liveMode;
	}

}

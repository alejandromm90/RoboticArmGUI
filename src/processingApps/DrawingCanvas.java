package processingApps;

/**
 * @author Alejandro Moran
 * 
 * */

import geometric.RelativePoint;

import java.util.ArrayList;

import processing.core.PApplet;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.Vector;

import constants.Constants;
import constants.Flavour;

public class DrawingCanvas extends PApplet{
	private double lastX, lastY;
	private int width = 640;
	private int height = 360;
	private boolean firstClick, leapMotionMode;
	private ArrayList<RelativePoint> lastEllipse, ellipsesRedo, ellipsesLeapMotion;
	private ArrayList<Line> lastLine, linesRedo;
	private Controller leapController;
	
	
	public void setup() {
	  frameRate(30);
	  size(width, height);

	  background(255);
	  firstClick = true;
	  lastEllipse = new ArrayList<RelativePoint>();
	  lastLine = new ArrayList<Line>();
	  ellipsesRedo = new ArrayList<RelativePoint>();
	  linesRedo = new ArrayList<Line>();
	  strokeWeight(2);  // Increases the weight of the line
  	  ellipsesLeapMotion = new ArrayList<RelativePoint>();

	  leapController = new Controller();
	  
	}
	
	
	public void draw() {
	  drawBorder();
	  stroke(0);
	  
	  if (leapMotionMode) { 
		  drawWithLeapMotion();
	  
	  } else {
		  if (mousePressed == true) {
		    if (firstClick) {
		      lastX = mouseX;  
		      lastY = mouseY;
		      
		    } else {
		    	/*
		    	if (((lastX != mouseX + 1) && (lastX != mouseX - 1)) ||
		      ((lastY != mouseY + 1) && (lastY != mouseY - 1))) {	
		    	*/
		    	if ((mouseX != lastX) && (mouseY != lastY)) {
		    		addLineToArray(mouseX, mouseY, lastX, lastY);
				}
		      // draw line between points
			  line(mouseX, mouseY,(float)lastX, (float)lastY);
			  
			  lastX = mouseX;
			  lastY = mouseY;
		  }
		    fill(0,0,0);  // Fills the ellipse in black color
		    
		    addEllipseToArray(mouseX, mouseY);

		    ellipse(mouseX, mouseY, 5, 5);
		    firstClick = false;
		  }
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
			    waits(5000000);
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
	
	private void addEllipseToArray(double mX, double mY) {
//		if (!lastEllipse.isEmpty()) {
//	    	if ((lastEllipse.get(lastEllipse.size() - 1).getX() != mX) && (lastEllipse.get(lastEllipse.size() - 1).getY() != mY)) {
//	    		lastEllipse.add(new Point(mX, mY, 0));
//	    	}
//	    } else {
//	    	lastEllipse.add(new Point(mouseX, mouseY, 0));
//	    }
		lastEllipse.add(new RelativePoint(mX, mY, 0, 5, Flavour.CHOCOLATE));

	}

	private void fingerPaint(Vector tip, int paintColor) {
		int maxBrushSize = 140;
	    fill(paintColor);
	    float x = tip.getX() * width;
	    float y = height - tip.getY() * height;
	    float brushSize = maxBrushSize - maxBrushSize * tip.getZ();
	    System.out.println(brushSize);
	    if (brushSize == maxBrushSize) {
	    	RelativePoint newEllipse = new RelativePoint(x, y, 0, 5, Flavour.CHOCOLATE);
	    	ellipsesLeapMotion.add(newEllipse);
	    	ellipse(x, y, 5, 5); //brushSize, brushSize);   
	    } else {
	    	background(255);
	    	drawBorder();
	    	ellipse(x, y, 10, 10);
	    	repaintCanvas(ellipsesLeapMotion, new ArrayList<Line>(), false);
	    }
	}
	
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
		background(255);
		firstClick = true;
		lastEllipse = new ArrayList<RelativePoint>();
		lastLine = new ArrayList<Line>();
		ellipsesRedo = new ArrayList<RelativePoint>();
		linesRedo = new ArrayList<Line>();
		ellipsesLeapMotion = new ArrayList<RelativePoint>();
	}
	
	public void undoCanvas() {
		
		background(255);
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
		background(255);
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

			Line lastL = null;
			if (i < lines.size()) {
				lastL = lines.get(i);
			}
			
			ellipse((float)lastE.getX(), (float)lastE.getY(), 5, 5);
			if (lastL != null) {
				line((float)lastL.getX(), (float)lastL.getY(), (float)lastL.getxE(), (float)lastL.getyE());
			}

			lastX = lastE.getX();
			lastY = lastE.getY();
			
			i++;
		}
	}
	
	private void drawBorder() {
	  line(0, 0, width, 0);
	  line(0, 0, 0, height-1);
	  line(0, height-1, width-1, height-1);
	  line(width-1, 0, width-1, height-1);
	}
	
	public ArrayList<RelativePoint> getPoints() {
		
		if (!lastEllipse.isEmpty()) {
			return lastEllipse;
		} else {
			return ellipsesLeapMotion;
		}
	}
	
}

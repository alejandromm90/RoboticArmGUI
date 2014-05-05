package processingApps;

/**
 * @author Alejandro Moran
 * 
 * */

import java.util.ArrayList;

import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.PointableList;
import com.leapmotion.leap.Vector;
import com.leapmotion.leap.Controller;


import processing.core.*;

public class DrawingCanvas extends PApplet{
	private float lastX, lastY;
	private int width = 640;
	private int height = 360;
	private boolean firstClick, leapMotionMode;
	private ArrayList<Ellipse> lastEllipse, ellipsesRedo, ellipsesLeapMotion;
	private ArrayList<Line> lastLine, linesRedo;
	private Controller leapController;
	
	
	public void setup() {
	  frameRate(30);
	  size(width, height);

	  background(255);
	  firstClick = true;
	  lastEllipse = new ArrayList<Ellipse>();
	  lastLine = new ArrayList<Line>();
	  ellipsesRedo = new ArrayList<Ellipse>();
	  linesRedo = new ArrayList<Line>();
	  strokeWeight(2);  // Increases the weight of the line
  	  ellipsesLeapMotion = new ArrayList<Ellipse>();

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
			  line(mouseX, mouseY, lastX, lastY);
			  
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
		  // TODO implement to detect only one finger
	        
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
			  }
//            }
//          }
	}
	
	private boolean addLineToArray(float mX, float mY, float lX, float lY) {
		if (lastLine.isEmpty()) {
			  lastLine.add(new Line(mX, mY, lX, lY));
		  } else if ((lastLine.get(lastLine.size()-1).getX() != mX) && (lastLine.get(lastLine.size()-1).getY() != mY)
				  && (lastLine.get(lastLine.size()-1).getxE() != lX) && (lastLine.get(lastLine.size()-1).getyE() != lY)) {
			  lastLine.add(new Line(mX, mY, lX, lY));
		  }
		return true;
	}
	
	private void addEllipseToArray(float mX, float mY) {
		if (!lastEllipse.isEmpty()) {
	    	if ((lastEllipse.get(lastEllipse.size() - 1).getX() != mX) && (lastEllipse.get(lastEllipse.size() - 1).getY() != mY)) {
	    		lastEllipse.add(new Ellipse(mX, mY));
	    	}
	    } else {
	    	lastEllipse.add(new Ellipse(mouseX, mouseY));
	    }
	}

	private void fingerPaint(Vector tip, int paintColor)
	{
		int maxBrushSize = 140;
	    fill(paintColor);
	    float x = tip.getX() * width;
	    float y = height - tip.getY() * height;
	    float brushSize = maxBrushSize - maxBrushSize * tip.getZ();
	    System.out.println(brushSize);
	    if (brushSize == maxBrushSize) {
	    	Ellipse newEllipse = new Ellipse(x, y);
	    	ellipsesLeapMotion.add(newEllipse);
	    	ellipse(x, y, 5, 5); //brushSize, brushSize);   
	    } else {
	    	background(255);
	    	drawBorder();
	    	ellipse(x, y, 10, 10);
	    	repaintCanvas(ellipsesLeapMotion, new ArrayList<Line>(), false);
	    }
	}

	
	public void setLeapMotionMode(boolean activate) {
		cleanCanvas();
		leapMotionMode = activate;
	}
	
	
	public void cleanCanvas() {
		background(255);
		firstClick = true;
		lastEllipse = new ArrayList<Ellipse>();
		lastLine = new ArrayList<Line>();
		ellipsesRedo = new ArrayList<Ellipse>();
		linesRedo = new ArrayList<Line>();
		ellipsesLeapMotion = new ArrayList<Ellipse>();
	}
	
	public void undoCanvas() {
		
		background(255);
		if (!lastEllipse.isEmpty()) {
			
			Ellipse undoEllipse = lastEllipse.remove(lastEllipse.size() - 1);
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
	
	private void repaintCanvas(ArrayList<Ellipse> ellipses, ArrayList<Line> lines, boolean redo) {
		int i = 0;
		if (redo && !ellipses.isEmpty()) {
			i = ellipses.size() - 1;
		}
		while (i < ellipses.size()) {
			Ellipse lastE = ellipses.get(i);

			Line lastL = null;
			if (i < lines.size()) {
				lastL = lines.get(i);
			}
			
			ellipse(lastE.getX(), lastE.getY(), 5, 5);
			if (lastL != null) {
				line(lastL.getX(), lastL.getY(), lastL.getxE(), lastL.getyE());
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
	
	public ArrayList<Ellipse> getPoints() {
		
		if (lastEllipse.size() != 0) {
			return lastEllipse;
		} else {
			return ellipsesLeapMotion;
		}
	}
	
}

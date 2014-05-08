package processingApps;

/**
 * @author Alejandro Moran
 * 
 * */

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.video.*;
import gab.opencv.*;
import geometric.RelativePoint;

public class ScanSketch extends PApplet {

	private int width = 640;
	private int height = 400;
	private ArrayList<RelativePoint> ellipses;
	private ArrayList<Line> lines;
	private boolean firstPaint, endCapture;

	OpenCV opencv;
	Capture video;
	PImage src, dst;

	ArrayList<Contour> contours;
	ArrayList<Contour> polygons;

	public void setup() {

	  size(width, height);
	  video = new Capture(this, width/2, height/2); 
	  opencv = new OpenCV(this, video.width, video.height);

	  background(255);
	  strokeWeight(2);  // Increases the weight of the line

	  ellipses = new ArrayList<RelativePoint>();
	  lines = new ArrayList<Line>();
	  firstPaint = true;
	  endCapture = false;
	}

	public void draw() {
		background(255);	// Clean the canvas

		if (!endCapture) {
			captureVideo();	
			drawBorder();
			
		} else {
		  video.stop();
		  drawBorder();
		  noFill();
		  strokeWeight(2);
		  scale((float) 1.5);
		  
		  for (Contour contour : contours) {
		    stroke(0);
	
		    if (contour != contours.get(contours.size()-1)) {	// Avoids to get the contours of the image border
	 
			    beginShape(LINES);
			    int contourCounter = 0;
			    for (PVector point : contour.getPolygonApproximation().getPoints()) {
			      fill(0,0,0);
			      if(firstPaint) {
			    	  ellipses.add(new RelativePoint(point.x, point.y, 0));
			    	  // We only get ellipses by pairs, to save the lines
			    	  if (contourCounter % 2 != 0) {
			    		  RelativePoint p1 = ellipses.get(contourCounter-1);
			    		  RelativePoint p2 = ellipses.get(contourCounter);
			    		  lines.add(new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
			    	  }
			      }
			      ellipse(point.x, point.y, 5, 5);
			      noFill();
			      vertex(point.x, point.y);
	
			      contourCounter++;
			    }
			    endShape();
		    }
		  }
		  firstPaint = false;
		}
	}
	
	/** Captures the video image, finds and shows the contours **/
	private void captureVideo() {
		video.start();
		opencv.loadImage(video);
		
		processImage();

	    image(video, 0, 0);
	    image(dst, video.width, 0);
	    
	    noFill();
	    strokeWeight(3);
	    
	    for (Contour contour : contours) {
		    stroke(0, 255, 0);
		    contour.draw();
		    
		    stroke(255, 0, 0);
		    beginShape();
		    for (PVector point : contour.getPolygonApproximation().getPoints()) {
		      vertex(point.x, point.y);
		    }
		    endShape();
		}
	    
	}
	
	/** Draws the border black line **/
	private void drawBorder() {
	  line(0, 0, width, 0);
	  line(0, 0, 0, height-1);
	  line(0, height-1, width-1, height-1);
	  line(width-1, 0, width-1, height-1);
	}
	
	/** Gets all the contours from the camera image **/
	private void processImage() {
		  opencv.gray();
		  opencv.threshold(70);
		  dst = opencv.getOutput();
	
		  contours = opencv.findContours();
	}
		
	/** Allows to start/stop the scanning mode **/
	public void setScanMode(boolean endCapture) {
		this.endCapture = !endCapture;
	}
	
	/** Sends the points from the scanned sketch **/
	public ArrayList<RelativePoint> getPoints() { 
		return ellipses;
	}
	
	public void captureEvent(Capture c) {
		  c.read();
	}
	
}

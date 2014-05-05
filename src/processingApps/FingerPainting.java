package processingApps;
import java.awt.Color;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.Vector;

import processing.core.PApplet;

public class FingerPainting extends PApplet{
	

	private int width = 640;
	private int height = 360;
	private int maxBrushSize = 120;
	private int canvasColor = 0xffffff;
	private float alphaVal = 10;

	Controller leap = new Controller();

	public void setup()
	{
	   frameRate(120);
	   size(width, height);
	   background(canvasColor);
	   stroke(0x00ffffff);
	}


	public void draw(){
		
	  Frame frame = leap.frame();
	  Pointable pointer = frame.pointables().frontmost();
	  if( pointer.isValid() )
	  {

	    int frontColor = color( 255, 0, 0, alphaVal );

	    InteractionBox iBox = frame.interactionBox();
	    Vector tip = iBox.normalizePoint(pointer.tipPosition());
	    fingerPaint(tip, frontColor);
	  }
	}

	private void fingerPaint(Vector tip, int paintColor)
	{
	   fill(paintColor);
	    float x = tip.getX() * width;
	    float y = height - tip.getY() * height;
	    float brushSize = maxBrushSize - maxBrushSize * tip.getZ();
	    ellipse( x, y, 20, 20);//brushSize, brushSize);   
	}

	public void keyPressed()
	{
	   background(canvasColor);
	}
}

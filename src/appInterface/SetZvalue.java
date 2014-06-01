package appInterface;

import geometric.Angles;
import geometric.RelativePoint;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.print.attribute.standard.Sides;

import math.Calculate;
import ArduinoComm.TalkWithArduino;
import constants.Constants;
import processingApps.DrawingCanvas;
import processingApps.Simulation;

public class SetZvalue implements MouseListener {

	private Timer timer = new Timer();
	private ZTimerTask task = new ZTimerTask();
	private boolean up = false;
	private static double zValue =Constants.START_Z;

	
	public SetZvalue(boolean isGoingUp) {
		this.up =isGoingUp;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		timer = new Timer();
		task = new ZTimerTask();
	    timer.scheduleAtFixedRate(task, 0, 50); 		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		task.cancel();
	}

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	@Override
	public void mouseExited(MouseEvent e) {		
	}

	private class ZTimerTask extends TimerTask {
        public void run() {
        	if(up){
        		incrementZValue();
        	} else {
        		decerementZValue();
        	}
        }
}
	
	
	private static void incrementZValue() {
		zValue++;
		if (zValue > Constants.MAX_Z){
			zValue = Constants.MAX_Z;
		}
		showheight();
	}


	private static void decerementZValue() {
		zValue--;
		if (zValue < Constants.MIN_Z){
			zValue = Constants.MIN_Z;
		}
		showheight();
	}
	
	private static void showheight(){
		RelativePoint actualPos = Simulation.getActualPointZ();
		if (actualPos == null){
			actualPos = new RelativePoint(Constants.START_X, Constants.START_Y, zValue, 0, 0, 0);
		} else {
			actualPos.setZ(zValue);
		}
		Angles angles = Calculate.calculateAngles(actualPos);
		TalkWithArduino.sendToArduino(angles, 0, 0, 0);
	}

	public static double getZValue() {
		return zValue;
	}
}

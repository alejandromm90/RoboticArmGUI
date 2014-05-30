package ArduinoComm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import appInterface.MainInterface;
import appInterface.ManualControl;
import geometric.Angles;
import constants.Constants;
import processing.core.PApplet;
import processing.serial.Serial;

public class TalkWithArduino {
	public static Serial port;// =new Serial(new PApplet(), Serial.list()[Serial.list().length-1], 19200);
	
	private TalkWithArduino() {
	}
	

	/**
	 * 
	 * @param angles
	 * @param flow1
	 * @param flow2
	 * @param flow3
	 */
	public static void sendToArduino(Angles angles, long flow1, long flow2, long flow3) {
		if(port == null)return;
		
		int thi = (int) Math.toDegrees(angles.getThi())
				+ Constants.CORRECT_ANGLE_THI;
		int theta = (int) Math.toDegrees(angles.getTheta())
				+ Constants.CORRECT_ANGLE_THETA;
		int kappa = (int) -Math.toDegrees(angles.getKappa())
				+ Constants.CORRECT_ANGLE_KAPPA;

		port.write(thi + "a");
		port.write(theta + "b");
		port.write(kappa + "c");

		sendFlowToArduino(flow1, flow2, flow3);
	}

	/**
	 * 
	 * @param flow1
	 * @param flow2
	 * @param flow3
	 */
	public static void sendFlowToArduino(long flow1, long flow2, long flow3) {
		if(port == null)return;

		
		flow1 = (100 * flow1) + 12000;
		port.write(flow1 + "d");

		flow2 = (-100 * flow2) + 12000;
		port.write(flow2 + "e");

		// flow3 = (100 * flow2) + 12000; // TODO
		// port.write(flow3 + "f");
	}

	
	
	private static void setPort(String portName){
		if(port != null){
			port.dispose();
		}
		if(portName == null){
			port = null;
		} else {
			port =new Serial(new PApplet(), portName, 19200);
		}
	}
	
	public static JMenu getSelectPortMenu(){
		
		
		JMenu menu = new JMenu("COM Port");	
		
		{
		final String  itemName = "Only Simulation" ;
		JMenuItem mItemStepperManual = new JMenuItem(itemName);
		mItemStepperManual.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setPort(null);
			}
		});
		menu.add(mItemStepperManual);
		}
		
		
		for (String portName : Serial.list()) {
			final String  itemName = portName;
			JMenuItem mItemStepperManual = new JMenuItem(itemName);
			mItemStepperManual.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					setPort(itemName);
				}
			});
			menu.add(mItemStepperManual);
		}
		
		
		return menu;
	}
}

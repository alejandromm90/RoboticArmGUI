package appInterface;

import javax.swing.JDialog; 

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;

import ArduinoComm.TalkWithArduino;


public class RotatingTable extends JDialog implements MouseListener {
	private static final long serialVersionUID = 1L;
	private JPanel myPanel;
	private JButton stepperFW = new JButton("Forward");;
	private JButton stepperBW = new JButton("Backward");;
	private JButton playStop = new JButton("Play");;
	private JLabel speedL = new JLabel("speed:");
	private JTextField speed = new JTextField("50");
	private boolean isRotating = false;

	public RotatingTable(JFrame frame) {
		super(frame, "rotating table control");
		myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(3, 2));
		getContentPane().add(myPanel);

		
		stepperFW.addMouseListener(this);
		stepperBW.addMouseListener(this);
		playStop.addMouseListener(this);
//		speed.addMouseListener(this);
		
		myPanel.add(stepperFW);
		myPanel.add(stepperBW);
		myPanel.add(speedL);
		myPanel.add(speed); 
		myPanel.add(playStop);
		

		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}



	public void mousePressed(MouseEvent e) { //TODO access Serial port write +- speedmax 
		int newV = getSpeed();
		if(stepperFW == e.getSource()) {
			int vel = Math.abs(newV);
			speed.setText(vel+"");
			TalkWithArduino.sendTableRotationToArduino(vel);

		} else if(stepperBW == e.getSource()){
			int vel = Math.abs(newV);
			speed.setText("-"+vel);
			TalkWithArduino.sendTableRotationToArduino(-vel);

//		} else if(speed == e.getSource()) {
//			if(isRotating){
//				v = newV;
//			}
		} else if(playStop == e.getSource()){
			if(isRotating){
				playStop.setText("Play");
				isRotating=false;
				TalkWithArduino.sendTableRotationToArduino(0);
				
			} else {
				playStop.setText("Stop");
				isRotating = true;
				TalkWithArduino.sendTableRotationToArduino(newV);
			}
				

			
		}
	}
	
	private int getSpeed(){
		try {
			int vset =Integer.valueOf(speed.getText());
			return vset;
			} catch (NumberFormatException ex) {
				return 0;
				
			}
			
	}

	public void mouseReleased(MouseEvent e) { //TODO access Serial port write 0;
		if(stepperFW == e.getSource()
				|| stepperBW == e.getSource()
				){
			TalkWithArduino.sendTableRotationToArduino(0);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {	
	}
	@Override
	public void mouseEntered(MouseEvent e) {	
	}
	@Override
	public void mouseExited(MouseEvent e) {

	}  
}

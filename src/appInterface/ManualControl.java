package appInterface;

import javax.swing.JDialog; 

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

import ArduinoComm.TalkWithArduino;


public class ManualControl extends JDialog implements MouseListener {
	private static final long serialVersionUID = 1L;
	private JPanel myPanel;
	private JButton stepper1FW;
	private JButton stepper1BW;
	private JButton stepper2FW;
	private JButton stepper2BW;
	private JButton bothFW;
	private JButton bothBW;

	public ManualControl(JFrame frame) {
		super(frame, "manual syringue control");
		myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(2, 3));
		getContentPane().add(myPanel);
		stepper1FW = new JButton("1 forward");
		stepper2FW = new JButton("2 forward");
		stepper1BW = new JButton("1 backward");
		stepper2BW = new JButton("2 backward");

		
		bothFW = new JButton("both forward");
		bothBW = new JButton("both backward");
		
		stepper1FW.addMouseListener(this);
		stepper2FW.addMouseListener(this);
		stepper1BW.addMouseListener(this);
		stepper2BW.addMouseListener(this);
		
		bothFW.addMouseListener(this);
		bothBW.addMouseListener(this);

		myPanel.add(stepper1FW);
		myPanel.add(stepper2FW);
		myPanel.add(bothFW);
		myPanel.add(stepper1BW); 
		myPanel.add(stepper2BW);
		myPanel.add(bothBW);
		

		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}



	public void mousePressed(MouseEvent e) { //TODO access Serial port write +- speedmax 
		int flow1 = 0;
		int flow2 = 0;
		int flow3 = 0;
		if(stepper1FW == e.getSource()) {
			flow1 =120;
		} else if(stepper1BW == e.getSource()){
			flow1 =-120;

		} else if(stepper2FW == e.getSource()) {
			flow2 = 120;
		} else if(stepper2BW == e.getSource()){
			flow2 = -120;

		}else if (bothBW == e.getSource()){
			flow2 = -120;
			flow1 = -120;

		}else if (bothFW == e.getSource()){
			flow2 = 120;
			flow1 = 120;

		}
		TalkWithArduino.sendFlowToArduino(flow1, flow2, flow3);
	}

	public void mouseReleased(MouseEvent e) { //TODO access Serial port write 0;
		if(stepper1FW == e.getSource()
				|| stepper1BW == e.getSource()
				|| stepper2FW == e.getSource()
				|| stepper2BW == e.getSource()
				|| bothBW == e.getSource()
				|| bothFW == e.getSource()
				){
			TalkWithArduino.sendFlowToArduino(0, 0, 0);
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

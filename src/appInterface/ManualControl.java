package appInterface;

import javax.swing.JDialog; 

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;


public class ManualControl extends JDialog implements MouseListener {
	private static final long serialVersionUID = 1L;
	private JPanel myPanel;
	private JButton stepper1FW;
	private JButton stepper1BW;
	private JButton stepper2FW;
	private JButton stepper2BW;

	public ManualControl(JFrame frame) {
		super(frame, "manual syringue control");
		myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(2, 2));
		getContentPane().add(myPanel);
		stepper1FW = new JButton("1 forward");
		stepper2FW = new JButton("2 forward");
		stepper1BW = new JButton("1 backward");
		stepper2BW = new JButton("2 backward");

		stepper1FW.addMouseListener(this);
		stepper2FW.addMouseListener(this);
		stepper1BW.addMouseListener(this);
		stepper2BW.addMouseListener(this);

		myPanel.add(stepper1FW);
		myPanel.add(stepper2FW); 
		myPanel.add(stepper1BW); 
		myPanel.add(stepper2BW); 

		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}



	public void mousePressed(MouseEvent e) { //TODO access Serial port write +- speedmax 
		if(stepper1FW == e.getSource()) {
			System.out.println("1fw");
		} else if(stepper1BW == e.getSource()){
			System.out.println("1bw");

		} else if(stepper2FW == e.getSource()) {
			System.out.println("2fw");
		} else if(stepper2BW == e.getSource()){
			System.out.println("2bw");

		}
	}

	public void mouseReleased(MouseEvent e) { //TODO access Serial port write 0;
		if(stepper1FW == e.getSource()) {
			System.out.println("1fw st");
		} else if(stepper1BW == e.getSource()){
			System.out.println("1bw st");

		} else if(stepper2FW == e.getSource()) {
			System.out.println("2fw st");
		} else if(stepper2BW == e.getSource()){
			System.out.println("2bw st");

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

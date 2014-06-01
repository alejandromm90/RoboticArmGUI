package appInterface;

import geometric.RelativePoint;

import javax.swing.JDialog; 

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;

import math.Calculate;



public class DrawShapes extends JDialog  implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JPanel myPanel;
	private JButton ok;
	private JButton cancel;

	JLabel radiusL = new JLabel("radius cm:");
	JTextField  radius = new JTextField();

	JLabel startdegreeL = new JLabel("start angle deg:");
	JLabel enddegreeL = new JLabel("end angle deg:");

	JTextField  startdegree = new JTextField();
	JTextField  enddegree = new JTextField();


	JLabel widthL = new JLabel("width cm:");
	JLabel heightL = new JLabel("height cm:");

	JTextField  width = new JTextField();
	JTextField  height = new JTextField();
	
	JLabel tipsL = new JLabel("tips number");
	JTextField  tips = new JTextField();
	
	



	private  static int[] answer = null;

	public static ArrayList<RelativePoint> getShapePoints(double x, double y,
			double z, long flow1,
			long flow2, long flow3) { 
		
		if(answer == null) return null;

		ArrayList<RelativePoint> points = null;

		switch (answer[0]) {
		case 0:
			points = Calculate.pointsOfArc(x, y, z, answer[1], answer[2], answer[3], flow1, flow2, flow3);

			break;
		case 1:
			points = Calculate.pointsOfCercle(x, y, z, answer[1], flow1, flow2, flow3);
			break;
		case 2:
			points = Calculate.pointsOfRectangle(x, y, z, answer[1], answer[2], flow1, flow2, flow3);
			break;
		case 3:
			points = Calculate.pointsOfSpiral(x, y, z, answer[1], answer[2], answer[3], flow1, flow2, flow3);
			break;
		case 4:
			points = Calculate.pointsOfStar(x, y, z, answer[1], answer[2], answer[3], flow1, flow2, flow3);
			break;

		default:
			break;
		}
		answer = null;
		return points; 
		}


	public DrawShapes(JFrame frame, int i) {
		super(frame, true);

		answer = new int[4];

		answer[0] = i;

		myPanel = new JPanel();

		getContentPane().add(myPanel);


		ok = new JButton("ok");
		ok.addActionListener(this);
		cancel = new JButton("cancel");
		cancel.addActionListener(this);




		switch (i) {
		case 0:
			myPanel.setLayout(new GridLayout(4, 2));


			myPanel.add(radiusL);
			myPanel.add(radius);
			myPanel.add(startdegreeL);
			myPanel.add(startdegree);
			myPanel.add(enddegreeL);
			myPanel.add(enddegree);


			break;

		case 1:
			myPanel.setLayout(new GridLayout(2, 2));

			myPanel.add(radiusL);
			myPanel.add(radius);


			break;

		case 2:
			myPanel.setLayout(new GridLayout(3, 2));

			myPanel.add(widthL);
			myPanel.add(width);
			myPanel.add(heightL);
			myPanel.add(height);

			break;
			
		case 3:
			myPanel.setLayout(new GridLayout(4, 2));

			myPanel.add(radiusL);
			myPanel.add(radius);

			myPanel.add(widthL);
			myPanel.add(width);
			
			tipsL.setText("degree:");
			myPanel.add(tipsL);
			myPanel.add(tips);

			break;
			
		case 4:
			myPanel.setLayout(new GridLayout(4, 2));

			myPanel.add(radiusL);
			myPanel.add(radius);
			
			myPanel.add(widthL);
			myPanel.add(width);
			
			myPanel.add(tipsL);
			myPanel.add(tips);


			break;

		default:
			break;
		}



		myPanel.add(ok);
		myPanel.add(cancel);


		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}



	public void actionPerformed(ActionEvent e) {
		if(ok == e.getSource()) {

			switch (answer[0]) {
			case 0:
				answer[1] = Calculate.transformToPixels(Double.valueOf(radius.getText()));
				answer[2] = Integer.valueOf(startdegree.getText());
				answer[3] = Integer.valueOf(enddegree.getText());
				
				
				break;
			case 1:
				answer[1] = Calculate.transformToPixels(Double.valueOf(radius.getText()));
				break;
			case 2:
				answer[1] = Calculate.transformToPixels(Double.valueOf(width.getText()));
				answer[2] = Calculate.transformToPixels(Double.valueOf(height.getText()));

				break;
			case 3:
				answer[1] = Calculate.transformToPixels(Double.valueOf(radius.getText()));
				answer[2] = Calculate.transformToPixels(Double.valueOf(width.getText()));
				answer[3] = Integer.valueOf(tips.getText());

				break;
			case 4:
				answer[1] = Calculate.transformToPixels(Double.valueOf(radius.getText()));
				answer[2] = Calculate.transformToPixels(Double.valueOf(width.getText()));
				answer[3] = Integer.valueOf(tips.getText());
				
				break;

			default:
				break;
			}



			setVisible(false);
		}
		else if(cancel == e.getSource()) {
			answer = null;
			setVisible(false);
		}
	}

}

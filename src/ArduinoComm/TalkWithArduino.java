package ArduinoComm;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import geometric.Angles;
import constants.Constants;
import processing.core.PApplet;
import processing.serial.Serial;

public class TalkWithArduino {
	private static Serial port;// =new Serial(new PApplet(), Serial.list()[Serial.list().length-1], 19200);
	private static boolean remote = false;
	private static Server server = null;
	private static Client client = null;
	private static int busyID = 0;
	private static long busyIdLastSeen =0;
	private static TalkWithArduino istance =  new TalkWithArduino();


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
		if(remote){

			int thi = (int) Math.toDegrees(angles.getThi())
					+ Constants.CORRECT_ANGLE_THI;
			int theta = (int) Math.toDegrees(angles.getTheta())
					+ Constants.CORRECT_ANGLE_THETA;
			int kappa = (int) -Math.toDegrees(angles.getKappa())
					+ Constants.CORRECT_ANGLE_KAPPA;

			sendToServer(thi + "a");
			sendToServer(theta + "b");
			sendToServer(kappa + "c");

			sendFlowToArduino(flow1, flow2, flow3);



		}
		//not else if this way can control a local and a remote arm
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
		if(remote){

			flow1 = (100 * flow1) + 12000;
			sendToServer(flow1 + "d");

			flow2 = (-100 * flow2) + 12000;
			sendToServer(flow2 + "e");


		}
		//not else if this way can control a local and a remote arm
		if(port == null)return;


		flow1 = (100 * flow1) + 12000;
		port.write(flow1 + "d");

		flow2 = (-100 * flow2) + 12000;
		port.write(flow2 + "e");

		// flow3 = (100 * flow2) + 12000; // TODO
		// port.write(flow3 + "f")
	}

	
	public static void sendTableRotationToArduino(long speed) {
		if(remote){

			speed = (100 * speed) + 12000;
			sendToServer(speed + "f");
		}
		//not else if this way can control a local and a remote arm
		if(port == null)return;

		speed = (100 * speed) + 12000;
		port.write(speed + "f");
	}


	private static void sendToServer(String string) {
		if(client == null){
			askConnectionDetails(false);
		}
		client.sendMessage(string);
	}

	public static void closeServer(){
		if(server != null) server.stopServer();
		server = null;
	}

	private static void closeClient(){
		if(client != null) client.closeConnection();
		client = null;
		remote = false;
	}
	private static void remoteEnableClientMode(String address, int port){
		client = new Client(address, port);
	}


	private static void remoteEnableServerMode(int port){
		server = new Server(port);
		Thread t = new Thread(server);
		t.start();
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


	public static JMenu getCientServerMenu(){


		JMenu menu = new JMenu("Remote Menu");	

		final String  disablen = "Disable remote" ;
		JMenuItem disableR = new JMenuItem(disablen);
		disableR.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				closeServer();
				closeClient();
			}
		});
		menu.add(disableR);

		final String  clientn = "Client mode" ;
		final JMenuItem cientR = new JMenuItem(clientn);
		cientR.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				askConnectionDetails(false);
			}
		});
		menu.add(cientR);

		final String  servern = "Server mode" ;
		final JMenuItem serverR = new JMenuItem(servern);
		serverR.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				askConnectionDetails(true);

			}
		});
		menu.add(serverR);



		return menu;
	}


	private static void askConnectionDetails(boolean isserver){
		istance.new Dialog(isserver);

	}

	public static boolean wirteDirectly(String command, int clientID){//TODO use client id also for local machine
		final long now = System.currentTimeMillis();
		if((now-busyIdLastSeen) > Constants.MAX_TIME_BUSY_MS){
			busyID = clientID;
		}

		if((busyID != clientID) || port == null){
			return false;
		} else {
			// TODO prevent malicious injection, check if line is in the correct format 
			port.write(command);
			
			busyIdLastSeen = now;
			busyID = clientID;
			
			return true;
		}
	}

	private class Dialog extends JDialog  implements ActionListener{
		private static final long serialVersionUID = 1L;
		private JPanel myPanel;
		private JButton ok;
		private JButton cancel;
		private boolean isserver;
		private JLabel addressL = new JLabel("Address:");
		private JTextField address  = new JTextField();
		private JLabel portL = new JLabel("Port:");
		private JTextField  port = new JTextField();



		public Dialog(boolean isserver) {
			this.isserver= isserver;
			myPanel = new JPanel();

			getContentPane().add(myPanel);

			ok = new JButton("ok");
			ok.addActionListener(this);
			cancel = new JButton("cancel");
			cancel.addActionListener(this);

			myPanel.setLayout(new GridLayout(3, 2));

			port.setText(Constants.DEFAULT_PORT);
			myPanel.add(addressL);
			myPanel.add(address);
			myPanel.add(portL);
			myPanel.add(port);


			if(isserver){
				try {
					address.setText(InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				address.setEditable(false);
			}

			myPanel.add(ok);
			myPanel.add(cancel);


			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		}



		public void actionPerformed(ActionEvent e) {
			if(ok == e.getSource()) {
				closeServer();
				closeClient();
				if(isserver){
					remoteEnableServerMode(Integer.valueOf(port.getText()));
					this.setVisible(false);
				}else {//is client
					remoteEnableClientMode(address.getText(), Integer.valueOf(port.getText()));

					if(Constants.ACK_NEW_CONNECTION.equals(client.ReceiveMessage())){
						remote = true;
						this.setVisible(false);
					} else {
						port.setText("connection error");
						client = null;
					}
				}
			} else {//cancel do nothing and close
				this.setVisible(false);
			}

		}

	}

}

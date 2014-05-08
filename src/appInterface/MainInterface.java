package appInterface;

/**
 * @author Alejandro Moran
 * 
 * */

import geometric.Move;
import geometric.RelativePoint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import leapMotion.LeapMotionListener;
import processingApps.DrawingCanvas;
import processingApps.ScanSketch;
import processingApps.Simulation;

import com.leapmotion.leap.Controller;

import constants.Constants;


public class MainInterface extends javax.swing.JFrame {
	
	private ArrayList<Flavor> flavors;
	private ArrayList<JButton> flavorsButton;
	private LeapMotionListener listener;
	private int numberFlavor;
	private JLabel printingLabel;
	private JPanel bigPanel, mainPanel;
	private boolean firstTimeManuallyPrinting, firstClickLeapMotionMode, manualPrinting, firstClickStartLeapMotion, firstClickScanMode;
	private processing.core.PApplet sketchDrawing, scanSketch, armSimulationSketch;
	private Font buttonsFont= new Font("Arial", Font.PLAIN, 15);
	
	public static void main(String[] args) {			
		MainInterface inter = new MainInterface();
		inter.initializeInterface();
	}
	
	private void initializeInterface() {

		flavors = new ArrayList<Flavor>();
		flavors.add(new Flavor("chocolate"));
		flavors.add(new Flavor("cream"));
		flavors.add(new Flavor("vanilla"));
		flavorsButton = new ArrayList<JButton>();
		numberFlavor = 0;
		firstTimeManuallyPrinting = true;
		firstClickLeapMotionMode = true;
		firstClickStartLeapMotion = false;
		firstClickScanMode = false;
		manualPrinting = true;
		printingLabel = new JLabel();
		
		this.setContentPane(getMainPanel());
		this.setTitle("PRINTER USER INTERFACE");
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		this.setSize(700,600);

		this.setEnabled(true);
		this.setVisible(true);

//		initializeLeapMotionListener();
		
	}
	
	private JPanel getMainPanel() {
		mainPanel = new JPanel();
		BorderLayout b = new BorderLayout(0,10);
		
		mainPanel.setLayout(b);
		mainPanel.validate();
		JMenuBar mBar = setMenuBar();
		mainPanel.add(mBar, BorderLayout.NORTH);
		
		// Depending on the menuItem you choose, it will show each panel
//		bigPanel = setManuallyPrintingPanel();
		bigPanel = setDrawingCanvasPanel();
//		bigPanel = setScanSketchPanel();
				
		mainPanel.add(bigPanel, BorderLayout.CENTER);
		
		return mainPanel;
	}
	
	/** This method sets the Panel the manual printing mode **/
	private JPanel setManuallyPrintingPanel() {
		manualPrinting = true;
		
		this.setSize(500,500);
		JPanel upPanel = new JPanel();
		upPanel.setLayout(new BorderLayout(0,5));
		
		JPanel centerPanel = getCenterManuallyPrintingPanel();		
		
		JPanel panelButtons = new JPanel();
		panelButtons.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panelButtons.setLayout(new GridLayout(0,3));
		
		for (int i = 0; i < flavors.size(); i++) {
			// We check the first access to generate the flavors buttons just once
			if (firstTimeManuallyPrinting) {
				JButton flavorButton = getFlavorButton(flavors.get(i));
				flavorsButton.add(flavorButton);
				panelButtons.add(flavorButton);
			} else {
				JButton flavorButton = flavorsButton.get(i);
				panelButtons.add(flavorButton);
			}
		}
		firstTimeManuallyPrinting = false;
		
		upPanel.add(panelButtons, BorderLayout.NORTH);
		upPanel.add(centerPanel, BorderLayout.CENTER);
		return upPanel;
	}
	
	/** This method sets the Panel of the Scan Sketch mode **/
	private JPanel setScanSketchPanel() {
                
        JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel title = new JLabel("Scan Sketch");
		title.setFont(new Font("Arial", Font.BOLD, 20));
		
		JPanel scanPanel = new JPanel();
		scanSketch = new ScanSketch();
		scanPanel.setBounds(20, 20, 600, 600);
		scanPanel.add(scanSketch);
		
		JPanel buttonsPanel = setButtonsScanSketchPanel();
		
		mainPanel.add(title);
		mainPanel.add(scanPanel);
		mainPanel.add(buttonsPanel);
        
		scanSketch.init(); 
		
		this.setSize(700,600);
		scanPanel.setVisible(true);

        return mainPanel;
	}
	
	/** Sets the arm simulation JPanel to show how the arm will get the coordinates **/
	private JFrame setSimulationPanel(ArrayList<RelativePoint> points) {
		JFrame simJFrame = new JFrame();
		simJFrame.setTitle("ARM SIMULATION");
				
        JPanel mainPanel = new JPanel();
//		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//		mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
//		JLabel title = new JLabel("Arm Simulation");
//		title.setFont(new Font("Arial", Font.BOLD, 20));
//		
		JPanel printPanel = new JPanel();
		System.out.println(points);
		armSimulationSketch = new Simulation(Move.smoothMovement(transformRelativePoint(transformCoordinates(points))), transformCoordinates(points));//m1(m2(points)));
//		((ArmSimulationSide) armSimulationSideSketch).setPoints(points);
		printPanel.setBounds(20, 20, 600, 600);
		printPanel.setVisible(true);
		printPanel.add(armSimulationSketch);
		
//		mainPanel.add(title);
		mainPanel.add(printPanel);

		simJFrame.setContentPane(mainPanel);
		armSimulationSketch.init(); 
        
		simJFrame.setSize(Constants.SIZE_WIDTH ,Constants.SIZE_HEIGHT);
		simJFrame.setEnabled(true);
		simJFrame.setVisible(true);

		return simJFrame;
	}
	
	/**
	* 
	* @param points
	* @return
	*/
	public static ArrayList<RelativePoint> transformCoordinates(
		ArrayList<RelativePoint> points) {
		ArrayList<RelativePoint> points2 = new ArrayList<RelativePoint>();
	
		for (int i = 0; i < points.size(); i++) {
			RelativePoint point = points.get(i);
		
			points2.add(i, new RelativePoint(point.getX(), 360 - point.getY(),
			point.getZ()));
		}
	
		return points2;
	}

	/**
	* 
	* @param points
	* @return
	*/
	public static ArrayList<RelativePoint> transformRelativePoint(
		ArrayList<RelativePoint> points) {
		ArrayList<RelativePoint> points2 = new ArrayList<RelativePoint>();
		RelativePoint pointBefore;
	
		for (int i = 0; i < points.size(); i++) {
			RelativePoint point = points.get(i);
		
			if (i == 0) {
				pointBefore = new RelativePoint(0, 0, 0);
			} else {
				pointBefore = new RelativePoint(points.get(i - 1).getX(),
				points.get(i - 1).getY(), points.get(i - 1).getZ());
			}
		
			points2.add(i, new RelativePoint(point.getX() - pointBefore.getX(),
			point.getY() - pointBefore.getY(), point.getZ()
			- pointBefore.getZ()));
		}
	
		return points2;
	}
	
	
	/** This method sets the buttons that appear in the Scan Sketch JPanel **/
	private JPanel setButtonsScanSketchPanel() {
		JPanel buttonsPanel = new JPanel();
		
		final JButton scanButton = new JButton("Get Scanned Sketch");
		scanButton.setFont(buttonsFont);
		scanButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				if (firstClickScanMode) {
					((ScanSketch) scanSketch).setScanMode(true); 
					scanButton.setText("Get Scanned Sketch");
					firstClickScanMode = false;
				} else {
					((ScanSketch) scanSketch).setScanMode(false);
					scanButton.setText("Scan Mode");
					firstClickScanMode = true;
				}
			}
		});
		
		JButton printButton = new JButton("Print");
		printButton.setFont(buttonsFont);
		printButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO Send coordinates to print the sketch
				setSimulationPanel(((ScanSketch) scanSketch).getPoints());
			}
		});
		
		JButton saveButton = new JButton("Save");
		saveButton.setFont(buttonsFont);
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO Save sketch
				
			}
		});
		
		buttonsPanel.add(scanButton);
		buttonsPanel.add(printButton);
		buttonsPanel.add(saveButton);
		
		return buttonsPanel;
	}
	
	/** This method sets the Panel of the Drawing Canvas mode **/
	private JPanel setDrawingCanvasPanel() {
				
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel title = new JLabel("Drawing Canvas");
		title.setFont(new Font("Arial", Font.BOLD, 20));
		
		JPanel drawingPanel = new JPanel();
		sketchDrawing = new DrawingCanvas();
		drawingPanel.setBounds(20, 20, 600, 600);
		drawingPanel.add(sketchDrawing);
		
		JPanel buttonsPanel = setButtonsDrawingCanvasPanel();
		
		this.setSize(700,600);
		drawingPanel.setVisible(true);
	
		mainPanel.add(title);
		mainPanel.add(drawingPanel);
		mainPanel.add(buttonsPanel);
        
		sketchDrawing.init(); 
        return mainPanel;
	}
	
	/** This method sets the buttons that appear in the Drawing Canvas JPanel **/
	private JPanel setButtonsDrawingCanvasPanel() {
		JPanel buttonsPanel = new JPanel();
		
		final JButton leapMotionButton = new JButton("Leap Motion Mode");
		leapMotionButton.setFont(buttonsFont);
		leapMotionButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				if (firstClickLeapMotionMode) {
					((DrawingCanvas) sketchDrawing).setLeapMotionMode(true); 
					firstClickLeapMotionMode = false;
					leapMotionButton.setText("Disable Leap Motion Mode");
				} else {
					// TODO ask to save sketch and save coordinates array
					((DrawingCanvas) sketchDrawing).setLeapMotionMode(false);
					firstClickLeapMotionMode = true;
					leapMotionButton.setText("Leap Motion Mode");
				}
			}
		});
		
		JButton printButton = new JButton("Print");
		printButton.setFont(buttonsFont);
		printButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO Send coordinates to print the sketch
				setSimulationPanel(((DrawingCanvas) sketchDrawing).getPoints());
			}
		});
		
		JButton saveButton = new JButton("Save");
		saveButton.setFont(buttonsFont);
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// ((DrawingCanvas) sketchDrawing).saveCanvas();
				// TODO It should export the drawn points and lines, and also the Array of points to move the arm
				// Also add confirmation window YES, NO
			}
		});
		
		JButton cleanButton = new JButton("Clean Canvas");
		cleanButton.setFont(buttonsFont);
		cleanButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				((DrawingCanvas) sketchDrawing).cleanCanvas();
				// TODO add confirmation window YES, NO
			}
		});
		
		JButton undoButton = new JButton("UNDO");
		undoButton.setFont(buttonsFont);
		undoButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				((DrawingCanvas) sketchDrawing).undoCanvas();
				// TODO add confirmation window YES, NO
			}
		});
		
		JButton redoButton = new JButton("REDO");
		redoButton.setFont(buttonsFont);
		redoButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				((DrawingCanvas) sketchDrawing).redoCanvas();
				// TODO add confirmation window YES, NO
			}
		});
		
		buttonsPanel.add(leapMotionButton);
		buttonsPanel.add(printButton);
		buttonsPanel.add(saveButton);
		buttonsPanel.add(cleanButton);
		buttonsPanel.add(undoButton);
		buttonsPanel.add(redoButton);
		
		return buttonsPanel;
	}
	
	/** Sets the contain of the big Panel. Depending on the MenuItem Mode selected **/
	private void setBigPanel(int mode) {
		switch(mode){
		case 0:	// Manual Printing
			mainPanel.remove(bigPanel);
			bigPanel = setManuallyPrintingPanel();
			mainPanel.add(bigPanel, BorderLayout.CENTER);
			break;
		case 1:	// Scan Sketch
			manualPrinting = false;
			mainPanel.remove(bigPanel);
			bigPanel = setScanSketchPanel();
			mainPanel.add(bigPanel, BorderLayout.CENTER);
			break;
		case 2:	// Predefined Sketch
//			manualPrinting = false;
//			mainPanel.remove(bigPanel);
//			bigPanel = setDrawingCanvasPanel();
//			mainPanel.add(bigPanel, BorderLayout.CENTER);
			break;
		case 3:	// Drawing Canvas
			manualPrinting = false;
			mainPanel.remove(bigPanel);
			bigPanel = setDrawingCanvasPanel();
			mainPanel.add(bigPanel, BorderLayout.CENTER);
			mainPanel.repaint();
			mainPanel.revalidate();
//			mainPanel.setSize(700, 610);
			
			break;
		default:
			break;
		}
	}
	
	/** This method destroys the PApplet threads create from a Processing PApplet **/
	private void destroyPAppletThreads(int mode) {
		switch (mode) {
		case 0: // Manually Printing mode
			if (sketchDrawing != null) { 
				sketchDrawing.destroy();
			} 
			if (scanSketch != null) {
				scanSketch.destroy();
			}
			break;
		case 1: // Scan Sketch mode
			if (sketchDrawing != null) { 
				sketchDrawing.destroy();
			}
			if (scanSketch != null) {
				scanSketch.destroy();
			}
			break;
		case 2: // Predefined sketch mode
			break;
		case 3: // Drawing Canvas mode
			if (scanSketch != null) {
					scanSketch.destroy();
			}
			if (sketchDrawing != null) { 
				sketchDrawing.destroy();
			} 
			break;
		}
	}
	
	private JMenuBar setMenuBar() {
		JMenuBar mBar = new JMenuBar();
		
		JMenu menuModes = new JMenu("Modes");		
		JMenuItem mItemManually = new JMenuItem("Manually printing");
		mItemManually.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				destroyPAppletThreads(0); // We destroy the PApplet thread to stop it every time we change the mode
				setBigPanel(0);
				initializeLeapMotionListener();
			}
		});
		
		JMenuItem mItemScanSketch = new JMenuItem("Scan Sketch"); 
		mItemScanSketch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				destroyPAppletThreads(1);
				setBigPanel(1);
			}
		});
		
		JMenuItem mItemPredefinedModels = new JMenuItem("Predefined Sketch"); 
		mItemPredefinedModels.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				destroyPAppletThreads(2);
				setBigPanel(2);
			}
		});
		
		JMenuItem mItemDrawingCanvas = new JMenuItem("Drawing Canvas"); 
		mItemDrawingCanvas.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				destroyPAppletThreads(3);
				setBigPanel(3);
			}
		});
		
		menuModes.add(mItemManually);
		menuModes.add(mItemScanSketch);
		menuModes.add(mItemPredefinedModels);
		menuModes.add(mItemDrawingCanvas);
		mBar.add(menuModes);
		return mBar;
	}

	private JPanel getCenterManuallyPrintingPanel() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout (0, 5));
		
		JPanel panelPrintingLabel = new JPanel();
		// Border used as padding
		Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
		// JLabel will be involved for this border
		Border border = BorderFactory.createLineBorder(new Color(0, 0, 0), 2);
		printingLabel.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
		printingLabel.setSize(100, 100);
		printingLabel.setFont(new Font("Arial", Font.BOLD, 30));
		panelPrintingLabel.add(printingLabel);
		
		JPanel panelStartLM = new JPanel();
		final JButton startLeapMotion = new JButton("Stop Leap Motion");
		startLeapMotion.setFont(buttonsFont);
		startLeapMotion.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (firstClickStartLeapMotion) { 
					manualPrinting = true;
					firstClickStartLeapMotion = false;
					startLeapMotion.setText("Stop Leap Motion");
					initializeLeapMotionListener();
				} else {
					firstClickStartLeapMotion = true;
					startLeapMotion.setText("Start Leap Motion");
					manualPrinting = false;
				}
			}
		});

		panelStartLM.add(startLeapMotion);
		
		centerPanel.add(panelPrintingLabel, BorderLayout.NORTH);
		centerPanel.add(panelStartLM, BorderLayout.CENTER);
		return centerPanel;
	}
	
	private JButton getFlavorButton(final Flavor newFlavor){
		
		ImageIcon image = new ImageIcon(newFlavor.getFlavor() + ".png");
		ImageIcon imageRedefined = redefineImagen(image);
		JButton flavorButton = new JButton();
		flavorButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO send flavor info to arduino and set current flavor
				numberFlavor = flavors.indexOf(newFlavor);
				listener.setNumberFlavor(numberFlavor);
			}
		});
		
		flavorButton.setIcon(imageRedefined);
		return flavorButton;
	}
	
	
	public ImageIcon redefineImagen(ImageIcon originalImage){
		// new width and height: to stay with the same we send a -1
		int width = 150; 
		int height = 150; 

		// It obtains an icon redefined with specified dimension 
		ImageIcon imageRedefined = new ImageIcon(originalImage.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT));
		
		return imageRedefined;		
	}
	
	/** Initialize the Leap Motion Listener and its Controller when it is called **/
	private void initializeLeapMotionListener() {
		// Create a sample listener and controller
		listener = new LeapMotionListener(flavors);

        Thread thread = new Thread(){
            public void run(){
              Controller controller = new Controller();

              // Have the sample listener receive events from the controller
              controller.addListener(listener);
              
              workFlow();
              
              // Remove the sample listener when done
              controller.removeListener(listener);
            }
          };
         
          thread.start(); 
        
	}

	/** Stays all the time working, 
	 * listening Leap Motion to change the flavor buttons and the printing label **/
	public void workFlow(){
		numberFlavor = listener.getNumberFlavor();
		while(numberFlavor != -1 && manualPrinting){
			selectFlavorButton();			
			
			printText();
		}
	}
	
	/** Sets the buttons to selected or not depending on the selected selected **/
	public void selectFlavorButton() {
		numberFlavor = listener.getNumberFlavor();
		
		for (int i = 0; i < flavorsButton.size(); i++) {
 			if (i == numberFlavor) {
 				flavorsButton.get(numberFlavor).setSelected(true);
 			} else {
 				flavorsButton.get(i).setSelected(false);
 			}
 		}
	}
	
	/** Changes the Label text to indicate if it is printing or not **/
	public void printText() {
		
		String printingText = "";
		
		if (listener.isPrinting()) {			
			printingText = "Printing " + flavors.get(numberFlavor).getFlavor();
		} else {
			printingText = "Stopped printing";
		}
		printingLabel.setText(printingText);
	}
}

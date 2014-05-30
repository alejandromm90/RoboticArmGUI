package appInterface;

/**
 * @author Alejandro Moran
 * 
 * */

import geometric.RelativePoint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import leapMotion.LeapMotionListener;
import math.Calculate;
import processingApps.DrawingCanvas;
import processingApps.ScanSketch;
import processingApps.Simulation;
import ArduinoComm.TalkWithArduino;

import com.leapmotion.leap.Controller;

import constants.Constants;


public class MainInterface extends javax.swing.JFrame {
	
	private ArrayList<Flavor> flavors;
	private ArrayList<JButton> flavorsButton;
	private LeapMotionListener listener;
	private int numberFlavor;
	private JLabel printingLabel;
	private JPanel bigPanel, mainPanel;
	private boolean firstTimeManuallyPrinting, firstClickLeapMotionMode, manualPrinting, firstClickStartLeapMotion, firstClickScanMode, firstClickPauseColorTracking, firstClickLiveMode;
	private processing.core.PApplet sketchDrawing, scanSketch, armSimulationSketch;
	private Font buttonsFont = new Font("Arial", Font.PLAIN, 15);
	private int actualFlow1, actualFlow2, actualFlow3, actualFlow;
	private ColoredObjectTrack colorTrackingObject;

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
		firstClickPauseColorTracking = false;
		manualPrinting = true;
		printingLabel = new JLabel();
		actualFlow = Constants.DEFAULT_FLOW;
		actualFlow1 = Constants.DEFAULT_FLOW;	
		actualFlow2 = 0;
		actualFlow3 = 0;
		
		this.setContentPane(getMainPanel());
		this.setTitle("PRINTER USER INTERFACE");
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(!Simulation.finishPrint) {
					// If we are printing it doesn't close
					JOptionPane.showMessageDialog(null, "Printing in process");

				}else { 
					// If not, we close the system
					System.exit(0);	

				}
			}
		});
		
		this.setSize(700,600);
		this.setEnabled(true);
		this.pack();
		this.setVisible(true);

//		initializeLeapMotionListener();
		
	}
	
	
	/********************************************************************************
	 * 								Main Panel										*
	 * 																				*
	 * ******************************************************************************/
	
	private JPanel getMainPanel() {
		mainPanel = new JPanel();
		BorderLayout b = new BorderLayout(0,10);
		
		mainPanel.setLayout(b);

		JMenuBar mBar = setMenuBar();
		mainPanel.add(mBar, BorderLayout.NORTH);
		
		// Depending on the menuItem you choose, it will show each panel
//		bigPanel = setManuallyPrintingPanel();
		bigPanel = setDrawingCanvasPanel(0, 0);
//		bigPanel = setColorTrackingPanel();
//		bigPanel = setScanSketchPanel();
		mainPanel.add(bigPanel, BorderLayout.CENTER);

		return mainPanel;
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
			manualPrinting = false;
			mainPanel.remove(bigPanel);	// TODO
			boolean correctValue = false;
			float diameter = 0;
			while (!correctValue) {
				String cakeDiameter = JOptionPane.showInputDialog("Diameter cake's length (between 20 and 5 cm): ", 20);
				try {
					diameter = Float.parseFloat(cakeDiameter);
					if (diameter <= 20 && diameter >= 5)
						correctValue = true;	
				}catch (NumberFormatException e){
//					System.out.println("exception");
				}					
			}
			
			bigPanel = setDrawingCanvasPanel(1, diameter);
			mainPanel.add(bigPanel, BorderLayout.CENTER);
			break;
		case 3:	// Drawing Canvas
			manualPrinting = false;
			mainPanel.remove(bigPanel);
			bigPanel = setDrawingCanvasPanel(0, 0);
			mainPanel.add(bigPanel, BorderLayout.CENTER);
			this.pack();	// I am not sure if this works absolutely good
			this.setVisible(true);
			break;
		case 4: // Color Detection
			manualPrinting = false;
	        mainPanel.remove(bigPanel);
	        bigPanel = setColorTrackingPanel();
	        mainPanel.add(bigPanel, BorderLayout.CENTER);
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
		case 2: // cake drawing mode
			if (scanSketch != null) {
				scanSketch.destroy();
			}
			if (sketchDrawing != null) { 
				sketchDrawing.destroy();
			} 
			break;
		case 3: // Drawing Canvas mode
			if (scanSketch != null) {
				scanSketch.destroy();
			}
			if (sketchDrawing != null) { 
				sketchDrawing.destroy();
			} 
//			if (colorTrackingObject != null) {
//				colorTrackingObject.
//			} // TODO
			break;
			
		case 4: // Color Tracking mode
			if (scanSketch != null) {
					scanSketch.destroy();
			}
			if (sketchDrawing != null) { 
				sketchDrawing.destroy();
			} 
			break;
		}
	}
	
	/********************************************************************************
	 * 								Menu Bar										*
	 * 																				*
	 * ******************************************************************************/
	
	/**	Sets the menu bar with the drawing modes and the tools for the motors and Arduino **/
	private JMenuBar setMenuBar() {
		JMenuBar mBar = new JMenuBar();
		
		/****************************************
		 * 				Drawing Modes			*
		 * **************************************/
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
		
		JMenuItem mItemPredefinedModels = new JMenuItem("Cake Drawing Mode"); 
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
		
		JMenuItem mItemColorTracking = new JMenuItem("Color Tracking"); 
		mItemColorTracking.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				destroyPAppletThreads(4);
				setBigPanel(4);
			}
		});
		
		menuModes.add(mItemManually);
		menuModes.add(mItemScanSketch);
		menuModes.add(mItemPredefinedModels);
		menuModes.add(mItemDrawingCanvas);
		menuModes.add(mItemColorTracking);
		
		/****************************************
		 * 					Tools				*
		 * **************************************/
		
		JMenu menuTools = new JMenu("Tools");		
		JMenuItem mItemStepperManual = new JMenuItem("Steppers Manual Control");
		mItemStepperManual.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new ManualControl(MainInterface.this);
			}
		});
		menuTools.add(mItemStepperManual);
		
		
		
		mBar.add(menuModes);
		mBar.add(TalkWithArduino.getSelectPortMenu());
		mBar.add(menuTools);

		return mBar;
	}
	
	
	/********************************************************************************
	 * 								Simulation										*
	 * 																				*
	 * ******************************************************************************/	
	
	/** Sets the arm simulation JPanel to show how the arm will get the coordinates **/
	private JFrame setSimulationPanel(ArrayList<RelativePoint> points) {
		if (!points.isEmpty()) {
			final JFrame simJFrame = new JFrame();
			simJFrame.setTitle("ARM SIMULATION");
					
	        JPanel mainPanel = new JPanel();
	//		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	//		mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
			
	//		JLabel title = new JLabel("Arm Simulation");
	//		title.setFont(new Font("Arial", Font.BOLD, 20));
	//		
			JPanel printPanel = new JPanel();
			System.out.println(points);
			armSimulationSketch = new Simulation(Calculate.transformCoordinates(points));
	//		((ArmSimulationSide) armSimulationSideSketch).setPoints(points);
			printPanel.setBounds(20, 20, 600, 600);
			printPanel.setVisible(true);
			printPanel.add(armSimulationSketch);
			
	//		mainPanel.add(title);
			mainPanel.add(printPanel);
	
			simJFrame.setContentPane(mainPanel);
			armSimulationSketch.init(); 
			
			simJFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			simJFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					if(Simulation.finishPrint) {
						armSimulationSketch.destroy();	
						simJFrame.setVisible(false);
						simJFrame.dispose();
						
					}else { 
						JOptionPane.showMessageDialog(null, "Printing in process");
					}
				}
			});
			simJFrame.setSize(Constants.SIZE_WIDTH + 80 ,Constants.SIZE_HEIGHT + 80);
			simJFrame.setEnabled(true);
			simJFrame.setVisible(true);
	
			return simJFrame;
		}
		return null;
	}
	
	
	/********************************************************************************
	 * 								Scan Sketch Mode								*
	 * 																				*
	 * ******************************************************************************/
	
	/** This method sets the Panel of the Scan Sketch mode **/
	private JPanel setScanSketchPanel() {
                
        JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel title = new JLabel("Scan Sketch");
		title.setFont(new Font("Arial", Font.BOLD, 20));
		title.setAlignmentX(CENTER_ALIGNMENT);

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
	
	/** This method sets the buttons that appear in the Scan Sketch JPanel **/
	private JPanel setButtonsScanSketchPanel() {
		JPanel buttonsPanel = new JPanel();

		final JButton scanButton = new JButton("Get Scanned Sketch");
		scanButton.setFont(buttonsFont);
		scanButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				if (firstClickScanMode) {
					((ScanSketch) scanSketch).setActualFlow(1,15); 				
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
				setSimulationPanel(((ScanSketch) scanSketch).getPoints());
			}

		});
		
//		// Create the check box
//		JCheckBox chocolateButton = setFlavourCheckBox(1); 
//        JCheckBox strawberryButton = setFlavourCheckBox(2);
//
//        JPanel checkPanel = new JPanel(new GridLayout(0, 1));
//        JLabel flavoursLabel = new JLabel("Select flavours: ");
//        flavoursLabel.setFont(buttonsFont);
//        checkPanel.add(flavoursLabel);
//        checkPanel.add(chocolateButton);
//        checkPanel.add(strawberryButton);
        
		JButton saveButton = new JButton("Save");
		saveButton.setFont(buttonsFont);
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){				
			}
		});
		
		buttonsPanel.add(scanButton);
		buttonsPanel.add(printButton);
		buttonsPanel.add(saveButton);
		
		return buttonsPanel;
	}
	
	
	/********************************************************************************
	 * 								Drawing Canvas Mode								*
	 * 																				*
	 * ******************************************************************************/
	
	
	/** This method sets the Panel of the Drawing Canvas mode **/
	private JPanel setDrawingCanvasPanel(int mode, float diameter) {
				
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel title = new JLabel("Drawing Canvas");
		title.setFont(new Font("Arial", Font.BOLD, 20));
		title.setAlignmentX(CENTER_ALIGNMENT);
		
		JPanel drawingPanel = new JPanel();
		switch (mode) {
		case 0:	// Half ellipse mode
			sketchDrawing = new DrawingCanvas(0, diameter);
			break;
		case 1:	// Cake mode
			sketchDrawing = new DrawingCanvas(1, diameter);	
			break;
		}
		drawingPanel.add(sketchDrawing);
		JPanel buttonsPanel = setButtonsDrawingCanvasPanel();

		mainPanel.add(title);
		mainPanel.add(drawingPanel);
		mainPanel.add(buttonsPanel);
		sketchDrawing.init(); 
		
        return mainPanel;
	}
	
	/** This method sets the buttons that appear in the Drawing Canvas JPanel **/
	private JPanel setButtonsDrawingCanvasPanel() {
		JPanel buttonsPanel = new JPanel();
		
		final JButton leapMotionButton = new JButton();//"Leap Motion Mode");
		leapMotionButton.setIcon(new ImageIcon("icons/leap_motion_icon.png"));
		leapMotionButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				if (firstClickLeapMotionMode) {
					((DrawingCanvas) sketchDrawing).setLeapMotionMode(true); 
					firstClickLeapMotionMode = false;
					leapMotionButton.setIcon(new ImageIcon("icons/leap_motion_icon_pressed.png"));
//					leapMotionButton.setText("Disable Leap Motion Mode");
				} else {
					((DrawingCanvas) sketchDrawing).setLeapMotionMode(false);
					firstClickLeapMotionMode = true;
					leapMotionButton.setIcon(new ImageIcon("icons/leap_motion_icon.png"));
//					leapMotionButton.setText("Leap Motion Mode");
				}
			}
		});
		
		JButton printButton = new JButton("Print", new ImageIcon("icons/print_icon.png"));
		printButton.setFont(buttonsFont);
		printButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		printButton.setHorizontalTextPosition(SwingConstants.CENTER);
		printButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
//				((DrawingCanvas) sketchDrawing).pauseLeapMotionMode(true);
				setSimulationPanel(((DrawingCanvas) sketchDrawing).getPoints());
			}
		});
		
		JButton cleanButton = new JButton("Clean Canvas", new ImageIcon("icons/clean_canvas_icon.png"));
		cleanButton.setFont(buttonsFont);
		cleanButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		cleanButton.setHorizontalTextPosition(SwingConstants.CENTER);
		cleanButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				((DrawingCanvas) sketchDrawing).cleanCanvas();
			}
		});
		
		JButton splitButton = new JButton("Split", new ImageIcon("icons/split_icon.png"));
		splitButton.setFont(buttonsFont);
		splitButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		splitButton.setHorizontalTextPosition(SwingConstants.CENTER);
		splitButton.setFont(buttonsFont);
		splitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				((DrawingCanvas) sketchDrawing).splitDraw();
			}
		});
		
		// Create the check box
		JCheckBox chocolateButton = setFlavourCheckBox(1); 
        JCheckBox strawberryButton = setFlavourCheckBox(2);

        JPanel checkPanel = new JPanel(new GridLayout(0, 1));
        JLabel flavoursLabel = new JLabel("Select flavours: ");
        flavoursLabel.setFont(buttonsFont);
        checkPanel.add(flavoursLabel);
        checkPanel.add(chocolateButton);
        checkPanel.add(strawberryButton);
		
		JPanel flowPanel = new JPanel();
		JLabel flowLabel = new JLabel("Set Flow: ");
		flowLabel.setFont(buttonsFont);

		SpinnerNumberModel flows = new SpinnerNumberModel(15, 5, 20, 5); // initial, min, max, step 
		JSpinner spinnerFlow = new JSpinner(flows);
		spinnerFlow.addChangeListener( new ChangeListener() {
		      @Override
		      public void stateChanged( ChangeEvent e ) {
		    	  setSpinnerFlow(e);	// We set the flow value from the spinner value
		      }
		    } );
		
		flowPanel.add(flowLabel);
		flowPanel.add(spinnerFlow);
		
		final JButton liveButton = new JButton("Live Mode", new ImageIcon("icons/live_mode_icon.png"));
		liveButton.setFont(buttonsFont);
		liveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		liveButton.setHorizontalTextPosition(SwingConstants.CENTER);
		liveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (firstClickLiveMode) {
					((DrawingCanvas) sketchDrawing).setLiveMode(false);	
					liveButton.setText("Live Mode");
					firstClickLiveMode = false;
				} else {
					((DrawingCanvas) sketchDrawing).setLiveMode(true);	
					liveButton.setText("Stop Live Mode");
					firstClickLiveMode = true;
				}
			}
		});
		
		buttonsPanel.add(leapMotionButton);
		buttonsPanel.add(printButton);
		buttonsPanel.add(cleanButton);
		buttonsPanel.add(splitButton);
		buttonsPanel.add(checkPanel);
		buttonsPanel.add(flowPanel);
		buttonsPanel.add(liveButton);
		
		return buttonsPanel;
	}
	
	/** Sets the flow from the spinner change event value 
	 * @params event - from ChangeEvent
	**/
	private void setSpinnerFlow(ChangeEvent event) {
		JSpinner spinner = ( JSpinner ) event.getSource();
        SpinnerNumberModel spinnerModel = (SpinnerNumberModel) spinner.getModel();
        actualFlow = (Integer) spinnerModel.getValue();
        if (actualFlow1 > 0) {	// Here we check which flows we have, to set them on the canvas 
        	actualFlow1 = actualFlow;
        } if (actualFlow2 > 0) {
        	actualFlow2 = actualFlow;
        } if (actualFlow3 > 0) {
        	actualFlow3 = actualFlow;
        }
        System.out.println(actualFlow);
        ((DrawingCanvas) sketchDrawing).setActualFlow(actualFlow1, actualFlow2, actualFlow3); 
	}
	
	/** Sets the flavours checkBoxes
	 * @params flavour 1 = Chocolate, 2 = Strawberry
	**/
	private JCheckBox setFlavourCheckBox(int flavour) {
		switch (flavour) {
		case 1:
			JCheckBox chocolateButton = new JCheckBox("Chocolate");
			chocolateButton.setFont(buttonsFont);
			chocolateButton.setSelected(true);
			chocolateButton.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent itemEvent) {
			        int state = itemEvent.getStateChange();
			        if (state == ItemEvent.SELECTED) {
			        	actualFlow1 = actualFlow;	// We need to update flow1 to know that we are on chocolate
						((DrawingCanvas) sketchDrawing).setActualFlavour(1, actualFlow1); // Activate Chocolate
			        } else if (state == ItemEvent.DESELECTED) {
			        	actualFlow1 = 0;
						((DrawingCanvas) sketchDrawing).setActualFlavour(1, 0); // Deactivate Chocolate
			        }
			    }
			});
			return chocolateButton;
			
		case 2:
			JCheckBox strawberryButton = new JCheckBox("Strawberry");
		    strawberryButton.setFont(buttonsFont);
		    strawberryButton.setSelected(false);
		    strawberryButton.addItemListener(new ItemListener() {
		    	public void itemStateChanged(ItemEvent itemEvent) {
		        int state = itemEvent.getStateChange();

		        if (state == ItemEvent.SELECTED) {
		        	actualFlow2 = actualFlow;	// We need to update flow2 to know that we are on strawberry
					((DrawingCanvas) sketchDrawing).setActualFlavour(2, actualFlow2); // Activate Strawberry
		        } else if (state == ItemEvent.DESELECTED) {
		        	actualFlow2 = 0;
					((DrawingCanvas) sketchDrawing).setActualFlavour(2, 0); // Deactivate Strawberry
		        }
		    }	
		    });     
		    return strawberryButton;
		default:
			return null;
		}
	}
	
	/********************************************************************************
	 * 								Color Tracking Mode								*
	 * 																				*
	 * ******************************************************************************/
	
	/** Sets the Color Tracking Mode JPanel **/
	private JPanel setColorTrackingPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		JLabel title = new JLabel("Color Tracking Canvas");
		title.setFont(new Font("Arial", Font.BOLD, 20));
		title.setAlignmentX(CENTER_ALIGNMENT);

		JPanel trackingPanel = new JPanel();
		
		JPanel buttonsPanel = setButtonsColorTrackingPanel();
		
		// Init color tracking thread
		colorTrackingObject = new ColoredObjectTrack(trackingPanel);		
        Thread threadColorTrack = new Thread(colorTrackingObject);
        threadColorTrack.start();
       
		mainPanel.add(title);
		mainPanel.add(trackingPanel);
		mainPanel.add(buttonsPanel);
		
        return mainPanel;
	}
	
	/** This method sets the buttons that appear in the Drawing Canvas JPanel **/
	private JPanel setButtonsColorTrackingPanel() {
		JPanel buttonsPanel = new JPanel();
		
		final JButton stopButton = new JButton("Pause");
		stopButton.setFont(buttonsFont);
		stopButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (firstClickPauseColorTracking) { // Continue mode
					colorTrackingObject.setPauseMode(false);
					firstClickPauseColorTracking = false;
					stopButton.setText("Pause");
				} else {	// Pause mode
					colorTrackingObject.setPauseMode(true);
					firstClickPauseColorTracking = true;
					stopButton.setText("Continue");
				}
			}
		});
		
		JButton printButton = new JButton("Print");
		printButton.setFont(buttonsFont);
		printButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
//				setSimulationPanel(colorTrackingObject.getPoints());
			}
		});
				
		buttonsPanel.add(stopButton);
//		buttonsPanel.add(printButton);
		
		return buttonsPanel;
	}
	
	
	/********************************************************************************
	 * 								Manual Printing Mode							*
	 * 																				*
	 * ******************************************************************************/
	
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
	
	/** Used for the Images to change flavors **/
	private JButton getFlavorButton(final Flavor newFlavor){
		
		ImageIcon image = new ImageIcon("icons/" + newFlavor.getFlavor() + ".png");
		ImageIcon imageRedefined = redefineImagen(image);
		JButton flavorButton = new JButton();
		flavorButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				numberFlavor = flavors.indexOf(newFlavor);
				listener.setNumberFlavor(numberFlavor);
			}
		});
		
		flavorButton.setIcon(imageRedefined);
		return flavorButton;
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

	public ImageIcon redefineImagen(ImageIcon originalImage){
		// new width and height: to stay with the same we send a -1
		int width = 150; 
		int height = 150; 

		// It obtains an icon redefined with specified dimension 
		ImageIcon imageRedefined = new ImageIcon(originalImage.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT));
		
		return imageRedefined;		
	}
	
}

package constants;

/**
 * 
 * @author Philippe Heer
 * 
 */
public abstract class Constants {
	
	/**
	 * Speed relevant
	 */
	public static final int SIMULATION_SPEED = 0;
	public static final double STEP_RESOLUTION = 2.0;
	
	/**
	 * Flow relevant
	 */
	public static final int NUMBER_OF_FLAVOURS = 3;
	public static final int FLOW_SEPARATION_SIZE = 5;
	public static final int MAX_ALLOWED_FLOW_BY_USER = 40;
	
	public static final long[] FLOW_WAIT_SPEED_START = {80, 80}; // TODO have to test the chocolate	
	public static final long[] FLOW_WAIT_SPEED_END = {20, 51};	
	public static final int FLOW_WAIT = 1000;
	public static final int DEFAULT_FLOW1 = 10;
	public static final int DEFAULT_FLOW2 = 15;
	
	/**
	 * Grid relevant
	 */
	public static final double GRID_WIDTH = 116.5; 
	
	
	/****************************************************************************************************
	 *																									*
	 *										Arm movement												*
	 *																									*
	 ****************************************************************************************************/


	/**
	 * Dimensions of arm
	 */
	public static final double ARM_LENGTH_SHOULDER = 159.0;
	public static final double ARM_LENGTH_ELBOW = 173.0;

	/**
	 * Axes neither in center of turn or at the boottom end (both x and y)
	 */
	public static final double BASE_TOP_LENGTH = 32;
	public static final double BASE_BOTTOM_LENGTH = 17;
	
	/**
	 * Correct angle for 0 degree servo
	 */
	public static final int CORRECT_ANGLE_THI = 1; //0;
	public static final int CORRECT_ANGLE_THETA = 35;
	public static final int CORRECT_ANGLE_KAPPA = 24;

	/**
	 * Start arm position
	 */
	public static final double START_X = 0;
	public static final double START_Y = 100;
	public static final double START_Z = 0;
	
	

	/****************************************************************************************************
	 *																									*
	 *										Simulation part												*
	 *																									*
	 ****************************************************************************************************/

	/**
	 * Dimensions of applet
	 */
	public static final int SIZE_WIDTH = 700;
	public static final int SIZE_HEIGHT = 700;

	/**
	 * Dimension of drawing Canvas
	 */
	public static final int DRAWING_APPLET_SIZE_WIDTH = 682; //640;
	public static final int DRAWING_APPLET_SIZE_HEIGHT = 270; //360;

	/**
	 * Move the drawing applet grid in the print applet
	 */
	public static final int DRAWING_APPLET_RELATIVE_X = -341;
	public static final int DRAWING_APPLET_RELATIVE_Y = 100; //100;

	/**
	 * Only for drawing in the applet
	 */
	public static final double BASE_X = 350; //280;
	public static final double BASE_Y = 230;
	public static final double BASE_Z = 109;
	public static final double MIN_Z = -75;
	public static final double MAX_Z = 130;
	
	
	public static final double X_FACTOR = 0.8;
	public static final double Z_FACTOR = 0.9;
	public static final Double PIXEL_CENTIMETER = 23.54;
	
	
	///remote
	public static final String ACK_NEW_CONNECTION = "ackconnection";
	public static final long MAX_TIME_BUSY_MS = 1500;
	public static final String DEFAULT_PORT = "1234";

}

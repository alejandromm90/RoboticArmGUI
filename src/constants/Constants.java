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
	public static final int SIMULATION_SPEED = 5;
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
	public static final int DEFAULT_FLOW = 15;
	
	/**
	 * Flavour relevant
	 */
//	public static final int DEFAULT_FLAVOUR = 1; // Chocolate by default
	
	
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
	public static final int CORRECT_ANGLE_THI = 0;
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
	public static final int DRAWING_APPLET_RELATIVE_Y = 200; //100;

	/**
	 * Only for drawing in the applet
	 */
	public static final double BASE_X = 350; //280;
	public static final double BASE_Y = 230;
	public static final double BASE_Z = 109;
}

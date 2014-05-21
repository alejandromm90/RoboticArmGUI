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
//	public static final double BASE_TOP_SIDE = -10; // TODO
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
	public static final int DRAWING_APPLET_SIZE_WIDTH = 640;
	public static final int DRAWING_APPLET_SIZE_HEIGHT = 360;

	/**
	 * Move the drawing applet grid in the print applet
	 */
	public static final int DRAWING_APPLET_RELATIVE_X = -270;
	public static final int DRAWING_APPLET_RELATIVE_Y = 100;

	/**
	 * Only for drawing in the applet
	 */
	public static final double BASE_X = 280;
	public static final double BASE_Y = 230;
	public static final double BASE_Z = 109;
}

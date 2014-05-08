package geometric;

import java.util.ArrayList;

import math.Calculate;


import constants.Constants;

/**
 * 
 * @author Philippe Heer
 * 
 */
public abstract class Move {

	/**
	 * 
	 * @param points
	 * @return 
	 */
	public static ArrayList<RelativePoint> smoothMovement(ArrayList<RelativePoint> points) {	
		ArrayList<RelativePoint> points2 = new ArrayList<RelativePoint>();
		
		RelativePoint pointBefore = new RelativePoint(0, 0, 0);
		
		while (!points.isEmpty()) {
			double x_before = pointBefore.getX();
			double y_before = pointBefore.getY();
			double z_before = pointBefore.getZ();			
			
			RelativePoint point = points.remove(0);
			
			double x = point.getX();
			double y = point.getY();
			double z = point.getZ();

			double length = Math.sqrt(x * x + y * y + z * z);
			
			if (length > Constants.STEP_RESOLUTION) {
				Angles angles = Calculate.calculateAngles2(point);

				double z_temp = Constants.STEP_RESOLUTION * Math.sin(angles.getTheta());
				double length_temp = Constants.STEP_RESOLUTION * Math.cos(angles.getTheta());
				double x_temp = length_temp * Math.cos(angles.getThi());
				double y_temp = length_temp * Math.sin(angles.getThi());
				
				points2.add(new RelativePoint(x_temp + x_before, y_temp + y_before, z_temp + z_before));
				
				points.add(0, new RelativePoint(x - x_temp, y - y_temp, z - z_temp));
			} else {
				points2.add(new RelativePoint(x + x_before, y + y_before, z + z_before));
			}
			
			pointBefore = points2.get(points2.size() - 1);
		}
		
		return points2;
	}
}

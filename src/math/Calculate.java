package math;

import geometric.Angles;
import geometric.Point;
import geometric.RelativePoint;

import java.util.ArrayList;

import constants.Constants;

/**
 * 
 * @author Philippe Heer
 * 
 */
public abstract class Calculate {

	/**
	 * 
	 * @param point
	 * @return angles
	 */
	public static Angles calculateAngles(RelativePoint point) {
		double x = point.getX();
		double y = point.getY();
		double z = point.getZ();

		double thi = Math.atan2(y, x);
		double length = Math.sqrt(x * x + y * y) - Constants.BASE_BOTTOM_LENGTH
				- Constants.BASE_TOP_LENGTH;

		double kappa = 2 * Math
				.atan(-Math
						.sqrt(((Constants.ARM_LENGTH_ELBOW + Constants.ARM_LENGTH_SHOULDER)
								* (Constants.ARM_LENGTH_ELBOW + Constants.ARM_LENGTH_SHOULDER) - (length
								* length + z * z))
								/ ((length * length + z * z) - (Constants.ARM_LENGTH_ELBOW - Constants.ARM_LENGTH_SHOULDER)
										* (Constants.ARM_LENGTH_ELBOW - Constants.ARM_LENGTH_SHOULDER))));
		double theta = Math.atan2(z, length)
				- Math.atan2(Constants.ARM_LENGTH_ELBOW * Math.sin(kappa),
						Constants.ARM_LENGTH_SHOULDER
								+ Constants.ARM_LENGTH_ELBOW * Math.cos(kappa));

		// elbow always parallel to shoulder
		kappa += theta;

		return new Angles(thi, theta, kappa);
	}

	/**
	 * 
	 * @param point
	 * @return
	 */
	public static Angles calculateAngles2(RelativePoint point) {
		double x = point.getX();
		double y = point.getY();
		double z = point.getZ();

		double thi = Math.atan2(y, x);

		double length = Math.sqrt(x * x + y * y);
		double theta = Math.atan2(z, length);

		return new Angles(thi, theta, 0);
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

			points2.add(i,
					new RelativePoint(point.getX()
							+ Constants.DRAWING_APPLET_RELATIVE_X,
							Constants.DRAWING_APPLET_SIZE_HEIGHT - point.getY()
									+ Constants.DRAWING_APPLET_RELATIVE_Y,
							point.getZ(), point.getFlow1(), point.getFlow2(),
							point.getFlow3()));
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
				pointBefore = new RelativePoint(Constants.START_X,
						Constants.START_Y, Constants.START_Z, 0, 0, 0);
			} else {
				pointBefore = new RelativePoint(points.get(i - 1).getX(),
						points.get(i - 1).getY(), points.get(i - 1).getZ(),
						points.get(i - 1).getFlow1(), points.get(i - 1)
								.getFlow2(), points.get(i - 1).getFlow3());
			}

			points2.add(
					i,
					new RelativePoint(point.getX() - pointBefore.getX(), point
							.getY() - pointBefore.getY(), point.getZ()
							- pointBefore.getZ(), point.getFlow1(), point
							.getFlow2(), point.getFlow3()));
		}

		return points2;
	}
	
	public static ArrayList<RelativePoint> transformHundred(
			ArrayList<RelativePoint> points) {
		ArrayList<RelativePoint> points2 = new ArrayList<RelativePoint>();

		for (int i = 0; i < points.size(); i++) {
			points2.add(new RelativePoint(points.get(i).getX(), points.get(i).getY() + 100, points.get(i).getZ(),
					points.get(i).getFlow1(), points.get(i).getFlow2(), points.get(i).getFlow3()));
		}
		
		return points2;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @param flow1
	 * @param flow2
	 * @param flow3
	 * @return
	 */
	public static ArrayList<RelativePoint> pointsOfCercle(double x, double y,
			double z, double radius, long flow1, long flow2, long flow3) {
		return pointsOfArc(x, y, z, radius, 0, 360, flow1, flow2, flow3);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @param angle1
	 * @param angle2
	 * @param flow1
	 * @param flow2
	 * @param flow3
	 * @return
	 */
	public static ArrayList<RelativePoint> pointsOfArc(double x, double y,
			double z, double radius, int angle1, int angle2, long flow1,
			long flow2, long flow3) {
		ArrayList<RelativePoint> points = new ArrayList<RelativePoint>();

		for (int degree = angle1; degree < angle2;) {
			points.add(new Point(x + radius * Math.cos(Math.toRadians(degree)),
					y + radius * Math.sin(Math.toRadians(degree)), z, flow1,
					flow2, flow3));
			degree += 4;
		}

		RelativePoint point = points.get(points.size() - 1);
		points.add(new RelativePoint(point.getX(), point.getY(), point.getZ(),
				0, 0, 0));

		return points;
	}

	/**
	 * Give leftdown corner coordinates, clockwise direction
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 * @param flow1
	 * @param flow2
	 * @param flow3
	 * @return
	 */
	public static ArrayList<RelativePoint> pointsOfRectangle(double x,
			double y, double z, int width, int height, long flow1, long flow2,
			long flow3) {
		ArrayList<RelativePoint> points = new ArrayList<RelativePoint>();

		points.add(new Point(x, y, z, flow1, flow2, flow3));
		points.add(new Point(x, y + height, z, flow1, flow2, flow3));
		points.add(new Point(x + width, y + height, z, flow1, flow2, flow3));
		points.add(new Point(x + width, y, z, flow1, flow2, flow3));
		points.add(new Point(x, y, z, flow1, flow2, flow3));

		RelativePoint point = points.get(points.size() - 1);
		points.add(new RelativePoint(point.getX(), point.getY(), point.getZ(),
				0, 0, 0));

		return points;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param x_length
	 * @param y_length
	 * @param flow1
	 * @param flow2
	 * @param flow3
	 * @return
	 */
	public static ArrayList<RelativePoint> pointsOfLine(double x, double y,
			double z, int x_length, int y_length, long flow1, long flow2,
			long flow3) {
		ArrayList<RelativePoint> points = new ArrayList<RelativePoint>();

		points.add(new Point(x, y, z, flow1, flow2, flow3));
		points.add(new Point(x + x_length, y + y_length, z, flow1, flow2, flow3));

		RelativePoint point = points.get(points.size() - 1);
		points.add(new RelativePoint(point.getX(), point.getY(), point.getZ(),
				0, 0, 0));

		return points;
	}
	
	
	public static float transformToPixels(float cm) {
		cm = (float) (cm * 13.3);
		return cm;
	}
}

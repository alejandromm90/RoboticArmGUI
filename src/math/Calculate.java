package math;

import java.util.ArrayList;

import constants.Constants;
import constants.Flavour;
import geometric.Angles;
import geometric.RelativePoint;

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
		double length = Math.sqrt(x * x + y * y);// TODO -
													// Constants.BASE_TOP_LENGTH;

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

			points2.add(i, new RelativePoint(point.getX()
					+ Constants.DRAWING_APPLET_RELATIVE_X,
					Constants.DRAWING_APPLET_SIZE_HEIGHT - point.getY()
							+ Constants.DRAWING_APPLET_RELATIVE_Y,
					point.getZ(), point.getFlow(), point.getFlavour()));
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
						Constants.START_Y, Constants.START_Z, 0,
						Flavour.CHOCOLATE);
			} else {
				pointBefore = new RelativePoint(points.get(i - 1).getX(),
						points.get(i - 1).getY(), points.get(i - 1).getZ(),
						points.get(i - 1).getFlow(), points.get(i - 1)
								.getFlavour());
			}

			points2.add(
					i,
					new RelativePoint(point.getX() - pointBefore.getX(), point
							.getY() - pointBefore.getY(), point.getZ()
							- pointBefore.getZ(), point.getFlow(), point
							.getFlavour()));
		}

		return points2;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @param flow
	 * @param flavour
	 * @return
	 */
	public static ArrayList<RelativePoint> pointsOfCercle(double x, double y,
			double z, double radius, int flow, Flavour flavour) {
		ArrayList<RelativePoint> points = new ArrayList<RelativePoint>();

		for (int degree = 0; degree < 360; degree++) {
			points.add(new RelativePoint(x + radius
					* Math.cos(Math.toRadians(degree)), y + radius
					* Math.sin(Math.toRadians(degree)), z, flow, flavour));
		}

		return points;
	}
}

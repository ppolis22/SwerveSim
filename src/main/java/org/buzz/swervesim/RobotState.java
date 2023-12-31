package org.buzz.swervesim;

public class RobotState {
    private final Wheel frontLeft;
    private final Wheel frontRight;
    private final Wheel rearLeft;
    private final Wheel rearRight;

    private Point location;
    private double gyroAngle;
    private Vector direction;

    private static final double WIDTH = 50;
    private static final double HEIGHT = 50;

    /**
     * Initializes robot at given point. Assumes robot will initialize facing up (+90deg).
     * @param x x-coordinate of starting position
     * @param y y-coordinate of starting position
     */
    public RobotState(double x, double y) {
        location = new Point(x, y);
        gyroAngle = 90.0;
        direction = new Vector(0.0, -1.0);

        frontLeft = new Wheel(location.fromOffset(-WIDTH / 2.0, HEIGHT / 2.0));
        frontRight = new Wheel(location.fromOffset(WIDTH / 2.0, HEIGHT / 2.0));
        rearLeft = new Wheel(location.fromOffset(-WIDTH / 2.0, -HEIGHT / 2.0));
        rearRight = new Wheel(location.fromOffset(WIDTH / 2.0, -HEIGHT / 2.0));
    }

    /**
     * Allows each wheel to update its own position based on its speed and direction. Its worth keeping in mind that
     * this logic wouldn't be present in actual robot code, since physics updates that for us.
     */
    public void update() {
        frontLeft.update(gyroAngle);
        frontRight.update(gyroAngle);
        rearLeft.update(gyroAngle);
        rearRight.update(gyroAngle);

        updateLocationAndGyro();
    }

    public Wheel getFrontLeft() {
        return frontLeft;
    }

    public Wheel getFrontRight() {
        return frontRight;
    }

    public Wheel getRearLeft() {
        return rearLeft;
    }

    public Wheel getRearRight() {
        return rearRight;
    }

    public Point getLocation() {
        return location;
    }

    public Vector getDirectionVector() {
        return direction;
    }

    public double getGyroAngle() {
        return gyroAngle;
    }

    /**
     * Updates the point considered the robot center and its direction based on the wheels
     */
    private void updateLocationAndGyro() {
        Point front = frontLeft.getLocation().average(frontRight.getLocation());
        Point rear = rearLeft.getLocation().average(rearRight.getLocation());
        location = front.average(rear);
        direction = new Vector(front.x - location.x, front.y - location.y);
        direction.normalize();
        gyroAngle = Math.toDegrees(Math.atan2(direction.y, direction.x));
    }
}

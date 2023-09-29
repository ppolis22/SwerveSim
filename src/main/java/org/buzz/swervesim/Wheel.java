package org.buzz.swervesim;

public class Wheel {
    private double driveSpeed;
    private double turnSpeed;
    private double turnAngle;
    private Vector directionVector;
    private Point location;
    private boolean isInverted;

    private static final double TURN_SCALE = 45.0;  // rate of turn, 0.0 = 0deg per frame, 1.0 = 15 deg per frame
    private static final double DRIVE_SCALE = 5.0;  // rate of drive, 0.0 = 0px per frame, 1.0 = 5px per frame

    public Wheel(Point location) {
        this.location = location;
        this.driveSpeed = 0.0;
        this.turnSpeed = 0.0;
        this.turnAngle = 90.0;
        this.directionVector = new Vector();
        this.isInverted = false;
        update(90.0);   // calculate direction vector
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public void setDriveSpeed(double driveSpeed) {
        driveSpeed = Math.min(1.0, driveSpeed);
        driveSpeed = Math.max(-1.0, driveSpeed);
        this.driveSpeed = driveSpeed;
    }

    public double getTurnSpeed() {
        return turnSpeed;
    }

    public void setTurnSpeed(double turnSpeed) {
        this.turnSpeed = turnSpeed;
    }

    public double getTurnAngle() {
        return turnAngle;
    }

    public Point getLocation() {
        return this.location;
    }

    public Vector getDirectionVector() {
        return directionVector;
    }

    public void update(double gyroAngle) {
        double adjustedTurnSpeed = turnSpeed * TURN_SCALE;
        double adjustedDriveSpeed = driveSpeed * DRIVE_SCALE * (isInverted ? -1.0 : 1.0);

        turnAngle = (turnAngle + adjustedTurnSpeed) % 360.0;
        double globalTurnAngle = gyroAngle + turnAngle - 90.0;  // not sure about this transformation

        double xOffset = adjustedDriveSpeed * Math.cos(Math.toRadians(globalTurnAngle));
        double yOffset = adjustedDriveSpeed * Math.sin(Math.toRadians(globalTurnAngle));
        directionVector.x = xOffset;
        directionVector.y = yOffset;
        location.offset(directionVector);
    }

    public void setInverted(boolean inverted) {
        this.isInverted = inverted;
    }

    public boolean isInverted() {
        return isInverted;
    }
}

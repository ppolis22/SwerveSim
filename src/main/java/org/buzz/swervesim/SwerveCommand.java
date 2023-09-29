package org.buzz.swervesim;

public class SwerveCommand {

    private final RobotState robotState;
    private final Wheel frontLeft, frontRight, rearLeft, rearRight;
    private final SwivelController fl, fr, rl, rr;

    public SwerveCommand(RobotState robotState) {
        this.robotState = robotState;
        this.frontLeft = robotState.getFrontLeft();
        this.frontRight = robotState.getFrontRight();
        this.rearLeft = robotState.getRearLeft();
        this.rearRight = robotState.getRearRight();
        this.fl = new SwivelController(this.frontLeft);
        this.fr = new SwivelController(this.frontRight);
        this.rl = new SwivelController(this.rearLeft);
        this.rr = new SwivelController(this.rearRight);
    }

    public void execute(InputState inputs) {
        double stickAngle = inputs.getStickAngle();
        double stickMagnitude = inputs.getStickMagnitude();
        double stickTwist = inputs.getStickTwist();

        double localStickAngle = 90.0 + stickAngle - robotState.getGyroAngle();

        updateWheelParameters(225.0, stickTwist, localStickAngle, stickMagnitude, frontLeft, fl);
        updateWheelParameters(135.0, stickTwist, localStickAngle, stickMagnitude, frontRight, fr);
        updateWheelParameters(315.0, stickTwist, localStickAngle, stickMagnitude, rearLeft, rl);
        updateWheelParameters(45.0, stickTwist, localStickAngle, stickMagnitude, rearRight, rr);
    }

    private void updateWheelParameters(double wheelTwistAngle, double stickTwist, double localStickAngle, double stickMagnitude,
                                       Wheel wheel, SwivelController wheelController) {
        Vector result = getResultVector(wheelTwistAngle, stickTwist, localStickAngle, stickMagnitude);
        double goalAngle = (Math.toDegrees(Math.atan2(result.y, result.x)) + 360.0) % 360.0;
        double goalSpeed = result.getMagnitude();

        // determine error in shortest turn direction
        wheelController.setGoalAngle(goalAngle);
        double error = wheelController.getSmallestSignedError();

        // if shortest turn is still over 90deg, would be faster to reverse drive
        if (Math.abs(error) > 90.0) {
            wheel.setInverted(true);
            wheelController.setGoalAngle((wheel.getTurnAngle() + error + 180.0) % 360.0);
        } else {
            wheel.setInverted(false);
            wheelController.setGoalAngle(wheel.getTurnAngle() + error);
        }

        wheel.setTurnSpeed(wheelController.getMotorOutput());
        wheel.setDriveSpeed(goalSpeed);
    }

    private Vector getResultVector(double wheelTwistAngle, double stickTwist, double localStickAngle, double stickMagnitude) {
        Vector twist = new Vector(
                stickTwist * Math.cos(Math.toRadians(wheelTwistAngle)),
                stickTwist * Math.sin(Math.toRadians(wheelTwistAngle)));

        Vector move = new Vector(
                stickMagnitude * Math.cos(Math.toRadians(localStickAngle)),
                stickMagnitude * Math.sin(Math.toRadians(localStickAngle)));

        // sum the vectors and clamp to 1.0 magnitude (?) or scale proportionately (?)
        Vector result = new Vector(twist.x + move.x, twist.y + move.y);
        if (result.getMagnitude() > 1.0) {
            result.normalize();
        }

        return result;
    }
}
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

    /**
     * Takes in the stick inputs and sets the drive speed and turn speed of each wheel
     * @param inputs the stick inputs
     */
    public void execute(InputState inputs) {
        double stickAngle = inputs.getStickAngle();
        double stickMagnitude = inputs.getStickMagnitude();
        double stickTwist = inputs.getStickTwist();

        // This is done to convert the stick angle to be relative to current robot orientation. Assuming "forward"
        // is considered +90deg.
        double localStickAngle = 90.0 + stickAngle - robotState.getGyroAngle();

        updateWheelParameters(225.0, stickTwist, localStickAngle, stickMagnitude, frontLeft, fl);
        updateWheelParameters(135.0, stickTwist, localStickAngle, stickMagnitude, frontRight, fr);
        updateWheelParameters(315.0, stickTwist, localStickAngle, stickMagnitude, rearLeft, rl);
        updateWheelParameters(45.0, stickTwist, localStickAngle, stickMagnitude, rearRight, rr);
    }

    /**
     * Calculates the drive speed and turn speed for a specific wheel
     * @param wheelTwistAngle the "ideal" turn angle for this wheel, i.e. when all wheels are 90 relative to each other
     * @param stickTwist the input stick twist, from -1.0 to 1.0
     * @param localStickAngle the input stick angle adjusted for the robot's orientation, from 0 to 360
     * @param stickMagnitude the magnitude of the stick, from 0.0, to 1.0
     * @param wheel the wheel object to set the output speeds on
     * @param wheelController the controller to calculate the motor output given the target angle,
     *                        essentially a "P" controller right now
     */
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

    /**
     * Calculates the goal output vector for a single wheel by adding the "twist" vector to the "translation" vector. As
     * of right now I'm not handling the case where the magnitude is greater than 1.0, TODO
     * @param wheelTwistAngle the "ideal" turn angle for this wheel, i.e. when all wheels are 90 relative to each other
     * @param stickTwist the input stick twist, from -1.0 to 1.0
     * @param localStickAngle the input stick angle adjusted for the robot's orientation, from 0 to 360
     * @param stickMagnitude the magnitude of the stick, from 0.0, to 1.0
     * @return a Vector representing the goal direction and magnitude of the wheel for the given inputs
     */
    private Vector getResultVector(double wheelTwistAngle, double stickTwist, double localStickAngle, double stickMagnitude) {
        Vector twist = new Vector(
                stickTwist * Math.cos(Math.toRadians(wheelTwistAngle)),
                stickTwist * Math.sin(Math.toRadians(wheelTwistAngle)));

        Vector move = new Vector(
                stickMagnitude * Math.cos(Math.toRadians(localStickAngle)),
                stickMagnitude * Math.sin(Math.toRadians(localStickAngle)));

        // sum the vectors and in the future clamp to 1.0 magnitude (?) or scale proportionately (??)
        Vector result = new Vector(twist.x + move.x, twist.y + move.y);
        if (result.getMagnitude() > 1.0) {
            result.normalize();
        }

        return result;
    }
}
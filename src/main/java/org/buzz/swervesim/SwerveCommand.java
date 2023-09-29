package org.buzz.swervesim;

public class SwerveCommand {

    SwivelController fl = null;
    SwivelController fr = null;
    SwivelController rl = null;
    SwivelController rr = null;

    public void execute(Wheel frontLeft, Wheel frontRight, Wheel rearLeft, Wheel rearRight, double gyroAngle) {

        if (fl == null) {
            fl = new SwivelController(frontLeft);
            fr = new SwivelController(frontRight);
            rl = new SwivelController(rearLeft);
            rr = new SwivelController(rearRight);
        }

        double stickAngle = 90.0;
        double stickMagnitude = 0.25;
        double stickTwist = 0.25;

        double localStickAngle = 90.0 + stickAngle - gyroAngle;

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

class SwivelController {
    private Wheel wheel;
    private double goalAngle;

    public SwivelController(Wheel wheel) {
        this.wheel = wheel;
        this.goalAngle = wheel.getTurnAngle();
    }

    public void setGoalAngle(double newGoal) {
        goalAngle = newGoal;
    }

    public double getMotorOutput() {
        double error = getSmallestSignedError();
        double output = error / 180.0;    // P

        return output;
    }

    // a little hacky to expose this, should find better way
    public double getSmallestSignedError() {
        double error = goalAngle - wheel.getTurnAngle();
        if (Math.abs(error) > 180) {
            error -= Math.signum(error) * 360;
        }
        return error;
    }
}
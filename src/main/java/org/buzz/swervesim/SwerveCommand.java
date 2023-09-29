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
        double stickMagnitude = 0.5;
        double stickTwist = 0.5;

//        double localStickAngle = 90.0 + stickAngle - gyroAngle;
//
//        // 225 deg
//        Vector flTwist = new Vector(
//                stickTwist * Math.cos(Math.toRadians(225.0)),
//                stickTwist * Math.sin(Math.toRadians(225.0)));
//
//        Vector flMove = new Vector(
//                stickMagnitude * Math.cos(Math.toRadians(localStickAngle)),
//                stickMagnitude * Math.sin(Math.toRadians(localStickAngle)));
//
//        // sum the vectors and normalize
//        Vector flResult = new Vector(flTwist.x + flMove.x, flTwist.y + flMove.y);
//        flResult.normalize();

        fl.setGoalAngle(stickAngle);
        frontLeft.setTurnSpeed(fl.getMotorOutput());
        frontLeft.setDriveSpeed(stickMagnitude);

        fr.setGoalAngle(stickAngle);
        frontRight.setTurnSpeed(fr.getMotorOutput());
        frontRight.setDriveSpeed(stickMagnitude);

        rl.setGoalAngle(stickAngle);
        rearLeft.setTurnSpeed(rl.getMotorOutput());
        rearLeft.setDriveSpeed(stickMagnitude);

        rr.setGoalAngle(stickAngle);
        rearRight.setTurnSpeed(rr.getMotorOutput());
        rearRight.setDriveSpeed(stickMagnitude);
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
        double output = 0.0;

        // determine error in shortest turn direction
        double wheelAngle = wheel.getTurnAngle();
        double error = goalAngle - wheelAngle;
        if (Math.abs(error) > 180) {
            error -= Math.signum(error) * 360;
        }

        output += error / 180.0;    // P

        return output;
    }
}
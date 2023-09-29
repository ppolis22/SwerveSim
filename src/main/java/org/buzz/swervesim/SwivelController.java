package org.buzz.swervesim;

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
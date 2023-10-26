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

    /**
     * Get the motor output for the given goal angle. Essentially acts as a "P" controller
     * since it just scales the output proportionally to the error.
     * @return The motor output for the given angle, from -1.0 to 1.0
     */
    public double getMotorOutput() {
        double error = getSmallestSignedError();
        double output = error / 180.0;    // P

        return output;
    }

    /**
     * Calculates the offset in degrees from the current angle to the goal angle, e.g. +45deg for counter-clockwise
     * rotation or -45deg for clockwise rotation. Returns the smaller of the two potential offsets (clockwise or counter-
     * clockwise). Doesn't take into account the "reverse the drive trick", that's handled elsewhere.
     * @return the signed smallest error between current angle and goal angle, in degrees
     */
    public double getSmallestSignedError() {
        double error = goalAngle - wheel.getTurnAngle();
        if (Math.abs(error) > 180) {
            error -= Math.signum(error) * 360;
        }
        return error;
    }
}
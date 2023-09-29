package org.buzz.swervesim;

public class InputState {

    private final double stickAngle;
    private final double stickMagnitude;
    private final double stickTwist;

    public InputState(double stickAngle, double stickMagnitude, double stickTwist) {
        this.stickAngle = stickAngle;
        this.stickMagnitude = stickMagnitude;
        this.stickTwist = stickTwist;
    }

    public double getStickAngle() {
        return stickAngle;
    }

    public double getStickMagnitude() {
        return stickMagnitude;
    }

    public double getStickTwist() {
        return stickTwist;
    }
}

package org.buzz.swervesim;

public class Vector {
    public double x;
    public double y;

    public Vector() {
        this(0.0, 1.0);
    }

    public Vector(double xComp, double yComp) {
        this.x = xComp;
        this.y = yComp;
    }

    public void normalize() {
        double magnitude = Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));
        x /= magnitude;
        y /= magnitude;
    }
}

package org.buzz.swervesim;

public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void offset(Vector v) {
        offset(v.x, v.y);
    }

    public void offset(double xOffset, double yOffset) {
        this.x += xOffset;
        this.y += yOffset;
    }

    public Point fromOffset(double xOffset, double yOffset) {
        return new Point(this.x + xOffset, this.y + yOffset);
    }

    public Point average(Point other) {
        return new Point((this.x + other.x) / 2.0, (this.y + other.y) / 2.0);
    }

    public double distance(Point other) {
        return Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2));
    }
}

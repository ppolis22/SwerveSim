package org.buzz.swervesim;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class SwerveSimViewController {
    @FXML
    private Canvas canvas;

    @FXML
    private Button frameButton;

    private SwerveSimController appController;
    private GraphicsContext canvasGC;
    private static final double DIR_VEC_SCALE = 8.0;
    private static final double ROBOT_VEC_SCALE = 15.0;
    private static final double CANVAS_HEIGHT = 400;
    private static final double CANVAS_WIDTH = 400;

    @FXML
    protected void onFrameButtonClick() {
        appController.advanceFrame();
    }

    @FXML
    protected void onStartButtonClick() {
        appController.start();
    }

    @FXML
    protected void onStopButtonClick() {
        appController.stop();
    }

    @FXML
    public void initialize() {
        canvasGC = canvas.getGraphicsContext2D();
        canvasGC.setFill(Color.GRAY);
        canvasGC.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    public void setAppController(SwerveSimController controller) {
        this.appController = controller;
    }

    public void drawWheel(Point p, Vector v, boolean inverted) {
        canvasGC.setFill(Color.BLUE);
        drawRectInverted(p.x - 3, p.y + 3, 6, 6);
        canvasGC.setStroke(inverted ? Color.LAWNGREEN : Color.LIGHTBLUE);
        drawLineInverted(p.x, p.y,p.x + v.x * DIR_VEC_SCALE, p.y + v.y * DIR_VEC_SCALE);
    }

    public void clearCanvas() {
        canvasGC.setFill(Color.GRAY);
        canvasGC.fillRect(0, 0, 400, 400);
    }

    public void setFrameButtonEnabled(boolean enable) {
        frameButton.setDisable(!enable);
    }

    public void drawRobotCenter(Point p, Vector v) {
        canvasGC.setFill(Color.RED);
        drawRectInverted(p.x - 3, p.y + 3, 6, 6);
        canvasGC.setStroke(Color.LIGHTSALMON);
        drawLineInverted(p.x, p.y,p.x + v.x * ROBOT_VEC_SCALE, p.y + v.y * ROBOT_VEC_SCALE);
    }

    // convenience methods to make positive direction movement appear "up" on the canvas
    private void drawRectInverted(double x, double y, double w, double h) {
        canvasGC.fillRect(x, CANVAS_HEIGHT - y, w, h);
    }

    private void drawLineInverted(double x1, double y1, double x2, double y2) {
        canvasGC.strokeLine(x1, CANVAS_HEIGHT - y1, x2, CANVAS_HEIGHT - y2);
    }
}
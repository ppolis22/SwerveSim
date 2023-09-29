package org.buzz.swervesim;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SwerveSimController {

    private final RobotState robotState;
    private final SwerveSimViewController viewController;
    private final SwerveCommand command;
    private final Timeline swerveSimTask;
    private boolean isStarted;

    public SwerveSimController(SwerveSimViewController viewController, SwerveCommand command) {
        this.command = command;
        this.viewController = viewController;
        viewController.setAppController(this);

        robotState = new RobotState(200.0, 100.0);

        swerveSimTask = new Timeline(new KeyFrame(Duration.millis(100), event -> {
                advanceFrame();
        }));
        swerveSimTask.setCycleCount(Timeline.INDEFINITE);

        isStarted = false;
    }

    public void advanceFrame() {
        command.execute(robotState.getFrontLeft(), robotState.getFrontRight(),
                robotState.getRearLeft(), robotState.getRearRight(),
                robotState.getGyroAngle());

        robotState.update();

        viewController.clearCanvas();
        viewController.drawWheel(robotState.getFrontLeft().getLocation(), robotState.getFrontLeft().getDirectionVector(), robotState.getFrontLeft().isInverted());
        viewController.drawWheel(robotState.getFrontRight().getLocation(), robotState.getFrontRight().getDirectionVector(), robotState.getFrontRight().isInverted());
        viewController.drawWheel(robotState.getRearLeft().getLocation(), robotState.getRearLeft().getDirectionVector(), robotState.getRearLeft().isInverted());
        viewController.drawWheel(robotState.getRearRight().getLocation(), robotState.getRearRight().getDirectionVector(), robotState.getRearRight().isInverted());
        viewController.drawRobotCenter(robotState.getLocation(), robotState.getDirectionVector());
    }

    public void start() {
        if (!isStarted) {
            isStarted = true;
            viewController.setFrameButtonEnabled(false);
            swerveSimTask.play();
        }
    }

    public void stop() {
        if (isStarted) {
            swerveSimTask.stop();
            viewController.setFrameButtonEnabled(true);
            isStarted = false;
        }
    }
}


package gui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * * Обрабатывает ввод пользователя и управляет движением робота
 */
public class RobotController {

    private final Timer timer = initTimer();

    private final RobotModel model;
    private final GameVisualizer view;

    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.1;

    public RobotController(RobotModel model, GameVisualizer view) {
        this.model = model;
        this.view = view;
    }

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public void start() {
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setTargetPosition(e.getX(), e.getY());
            }
        });

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> onModelUpdateEvent());
            }
        }, 0, 10);
    }

    protected void onModelUpdateEvent() {
        double distance = distance(model.getTargetPositionX(), model.getTargetPositionY(),
                model.getRobotPositionX(), model.getRobotPositionY());
        if (distance < 0.5) {
            return;
        }

        double angleToTarget = angleTo(model.getRobotPositionX(), model.getRobotPositionY(),
                model.getTargetPositionX(), model.getTargetPositionY());

        double angleDiff = angleToTarget - model.getRobotDirection();
        while (angleDiff <= -Math.PI) angleDiff += 2 * Math.PI;
        while (angleDiff > Math.PI) angleDiff -= 2 * Math.PI;

        double velocity = 0;
        double angularVelocity = 0;

        if (Math.abs(angleDiff) > 0.1) {
            angularVelocity = angleDiff * (MAX_ANGULAR_VELOCITY / Math.PI);
            velocity = 0;
        } else {

            velocity = MAX_VELOCITY;
            angularVelocity = 0;
        }

        moveRobot(velocity, angularVelocity, 10);
    }
    private void moveRobot(double velocity, double angularVelocity, double duration) {

        velocity = applyLimits(velocity, 0, MAX_VELOCITY);
        angularVelocity = applyLimits(angularVelocity, -MAX_ANGULAR_VELOCITY, MAX_ANGULAR_VELOCITY);

        double robotPositionX = model.getRobotPositionX();
        double robotPositionY = model.getRobotPositionY();
        double robotDirection = model.getRobotDirection();

        double newX = robotPositionX + velocity / angularVelocity *
                (Math.sin(robotDirection + angularVelocity * duration) -
                        Math.sin(robotDirection));
        if (!Double.isFinite(newX)) {
            newX = robotPositionX + velocity * duration * Math.cos(robotDirection);
        }

        double newY = robotPositionY - velocity / angularVelocity *
                (Math.cos(robotDirection + angularVelocity * duration) -
                        Math.cos(robotDirection));
        if (!Double.isFinite(newY)) {
            newY = robotPositionY + velocity * duration * Math.sin(robotDirection);
        }

        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * duration);

        model.setRobotState(newX, newY, newDirection);
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }


}

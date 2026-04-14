package gui;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Хранит состояние робота и цели уведомляет подписчиков об изменениях.
 */
public class RobotModel {
    /**
     * поля состояния
     */
    private volatile double robotPositionX = 100;
    private volatile double robotPositionY = 100;
    private volatile double robotDirection = 0;
    private volatile int targetPositionX = 150;
    private volatile int targetPositionY = 100;
    /**
     *
     **/
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * методы модели
     */
    public double getRobotPositionX() {
        return robotPositionX;
    }

    public double getRobotPositionY() {
        return robotPositionY;
    }

    public double getRobotDirection() {
        return robotDirection;
    }

    public int getTargetPositionX() {
        return targetPositionX;
    }

    public int getTargetPositionY() {
        return targetPositionY;
    }

    public void setTargetPosition(int x, int y) {
        targetPositionX = x;
        targetPositionY = y;
        pcs.firePropertyChange("state", null, null);
    }

    public void setRobotState(double x, double y, double direction) {
        robotDirection = direction;
        robotPositionX = x;
        robotPositionY = y;
        pcs.firePropertyChange("state", null, null);
    }

}
package gui;

import gui.state.StateSupport;
import gui.state.StatefulComponent;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * Внутреннее окно отображения состояния робота
 * Показывает координаты, угол робота и угол до цели
 */
public class RobotInfoWindow extends JInternalFrame implements PropertyChangeListener, StatefulComponent {
    /**
     * Модель робота из которой берутся данные.
     */
    private RobotModel model;
    /**
     * Текстовая область для отображения состояния
     */
    private TextArea infoContent;

    /**
     * Конструктор который создаёт окно состояния и регистрирует слушателя изменений модели
     */
    public RobotInfoWindow(RobotModel model) {
        super("Состояние робота", true, true, true, true);
        this.model = model;
        this.model.addPropertyChangeListener(this);
        this.infoContent = new TextArea("");
        this.infoContent.setSize(250, 120);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(infoContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateInfoContent();
    }

    /**
     * Обновляет содержимое окна состояния на основе текущего состояния модели.
     */
    private void updateInfoContent() {
        double x = model.getRobotPositionX();
        double y = model.getRobotPositionY();
        double robotAngle = model.getRobotDirection();
        double angleToTarget = Math.atan2(
                model.getTargetPositionY() - y,
                model.getTargetPositionX() - x
        );
        String content = "x = " + x + "\n"
                + "y = " + y + "\n"
                + "robot angle = " + robotAngle + "\n"
                + "angle to targe   t = " + angleToTarget + "\n";
        infoContent.setText(content);
    }

    /**
     * Вызывается при изменении модели и обновляет отображение
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateInfoContent();
    }

    private final StateSupport stateSupport = new StateSupport();

    @Override
    public void saveState(Map<String, String> state) {
        stateSupport.save(this, state);
    }

    @Override
    public void loadState(Map<String, String> state) {
        stateSupport.load(this, state);
    }
}
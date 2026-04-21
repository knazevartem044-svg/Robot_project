package gui;

import gui.state.StateSupport;
import gui.state.StatefulComponent;

import java.util.Map;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JInternalFrame implements StatefulComponent {
    private final GameVisualizer gameVisualizer;
    private final RobotController robotController;
    private final RobotModel model;

    public GameWindow(RobotModel model) {
        super("Игровое поле", true, true, true, true);
        this.model = model;
        this.gameVisualizer = new GameVisualizer(model);
        this.robotController = new RobotController(model, gameVisualizer);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public void startController() {
        robotController.start();
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
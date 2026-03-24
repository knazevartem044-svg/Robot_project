package gui;
import gui.state.StateSupport;
import gui.state.StatefulComponent;

import java.util.Map;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JInternalFrame implements StatefulComponent
{
    private final GameVisualizer gameVisualizer;

    public GameWindow()
    {
        super("Игровое поле", true, true, true, true);
        gameVisualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
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

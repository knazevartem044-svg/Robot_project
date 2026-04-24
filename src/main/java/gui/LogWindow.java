package gui;
import gui.state.StateSupport;
import gui.state.StatefulComponent;
import java.util.Map;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import javax.swing.*;
import java.awt.*;

public class LogWindow extends JInternalFrame implements LogChangeListener , StatefulComponent
{
    private LogWindowSource logSource;
    private TextArea logContent;

    public LogWindow(LogWindowSource logSource)
    {
        super("Протокол работы", true, true, true, true);
        this.logSource = logSource;
        this.logSource.registerListener(this);
        this.logContent = new TextArea("");
        this.logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();

        /**
         * слушатель событий окна
         * при закрытии окна отписывается от логгера
         */
        this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                logSource.unregisterListener(LogWindow.this);
            }
        });
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        int totalSize = logSource.size();
        int countToShow = Math.min(totalSize, 4);
        int startFrom = totalSize - countToShow;
        Iterable<LogEntry> entries = logSource.range(startFrom, countToShow);
        for (LogEntry entry : entries)
        {
            content.append(entry.getMessage()).append("\n");
        }

        logContent.setText(content.toString());
        logContent.invalidate();
    }

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
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

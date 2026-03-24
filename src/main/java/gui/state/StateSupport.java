package gui.state;

import javax.swing.JInternalFrame;
import java.beans.PropertyVetoException;
import java.util.Map;

/**
 * Общий код сохранения и восстановления
 * состояния внутреннего окна.
 */
public class StateSupport {

    /**
     * Сохраняет состояние внутреннего окна.
     */
    public void save(JInternalFrame frame, Map<String, String> state) {
        state.put("x", Integer.toString(frame.getX()));
        state.put("y", Integer.toString(frame.getY()));
        state.put("width", Integer.toString(frame.getWidth()));
        state.put("height", Integer.toString(frame.getHeight()));
        state.put("icon", Boolean.toString(frame.isIcon()));
        state.put("maximum", Boolean.toString(frame.isMaximum()));
    }

    /**
     * Восстанавливает состояние внутреннего окна.
     */
    public void load(JInternalFrame frame, Map<String, String> state) {
        Integer x = parseInt(state.get("x"));
        Integer y = parseInt(state.get("y"));
        Integer width = parseInt(state.get("width"));
        Integer height = parseInt(state.get("height"));

        if (x != null && y != null && width != null && height != null) {
            frame.setBounds(x, y, width, height);
        }

        try {
            if (state.get("maximum") != null) {
                frame.setMaximum(Boolean.parseBoolean(state.get("maximum")));
            }
            if (state.get("icon") != null) {
                frame.setIcon(Boolean.parseBoolean(state.get("icon")));
            }
        } catch (PropertyVetoException ignored) {
        }
    }

    /**
     * Преобразует строку в число.
     */
    private Integer parseInt(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
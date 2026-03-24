package gui.state;
import java.util.Map;

/**
 * Интерфейс для компонентов, которые умеют
 * сохранять и восстанавливать своё состояние
 */
public interface StatefulComponent {
    /**
     * Сохраняет состояние компонента в словарь
     */
    void saveState (Map<String,String> state);
    /**
     * Восстанавливает состояние компонента из словаря
     */
    void loadState (Map<String,String> state);

}

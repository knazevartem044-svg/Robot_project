package gui.state;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Управляет сохранением и восстановлением
 * состояния компонентов приложения.
 */
public class StateManager {

    /**
     * Файл состояния.
     */
    private final File stateFile;

    /**
     * Зарегистрированные компоненты.
     */
    private final Map<String, StatefulComponent> components = new LinkedHashMap<>();

    /**
     * Объект чтения и записи состояния.
     */
    private final FileState fileState = new FileState();

    /**
     * Создаёт менеджер состояния.
     */
    public StateManager(File stateFile) {
        this.stateFile = stateFile;
    }

    /**
     * Регистрирует компонент по префиксу.
     */
    public void register(String prefix, StatefulComponent component) {
        components.put(prefix, component);
    }

    /**
     * Загружает состояние всех компонентов.
     */
    public void loadAll() {
        Map<String, String> globalState;

        try {
            globalState = fileState.load(stateFile);
        } catch (IOException e) {
            globalState = new LinkedHashMap<>();
        }

        for (Map.Entry<String, StatefulComponent> entry : components.entrySet()) {
            entry.getValue().loadState(new PrefixedMap(globalState, entry.getKey()));
        }
    }

    /**
     * Сохраняет состояние всех компонентов.
     */
    public void saveAll() {
        Map<String, String> globalState = new LinkedHashMap<>();

        for (Map.Entry<String, StatefulComponent> entry : components.entrySet()) {
            entry.getValue().saveState(new PrefixedMap(globalState, entry.getKey()));
        }

        try {
            fileState.save(stateFile, globalState);
        } catch (IOException ignored) {
        }
    }
}
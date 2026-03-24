
package gui.state;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 * Представление подсловаря общего словаря
 * по заданному префиксу.
 */
public class PrefixedMap extends AbstractMap<String, String> {

    /**
     * Общий словарь со всеми данными.
     */
    private final Map<String, String> rootMap;

    /**
     * Префикс с точкой.
     */
    private final String prefixWithDot;

    /**
     * Создаёт подсловарь по префиксу
     */
    public PrefixedMap(Map<String, String> rootMap, String prefix) {
        this.rootMap = rootMap;
        this.prefixWithDot = prefix.endsWith(".") ? prefix : prefix + ".";
    }

    /**
     * Возвращает значение по ключу без префикса
     */
    @Override
    public String get(Object key) {
        if (!(key instanceof String)) {
            return null;
        }
        return rootMap.get(prefixWithDot + key);
    }

    /**
     * Сохраняет значение по ключу без префиксаr
     */
    @Override
    public String put(String key, String value) {
        return rootMap.put(prefixWithDot + key, value);
    }

    /**
     * абстрактный метод который нужно реализовать, не используется
     */
    @Override
    public Set<Entry<String, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}

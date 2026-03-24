package gui.state;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Чтение и запись словаря состояния в файл.
 */
public class  FileState {

    /**
     * Загружает словарь из файла.
     */
    public Map<String, String> load(File file) throws IOException {
        Map<String, String> map = new LinkedHashMap<>();

        if (!file.exists()) {
            return map;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                int pos = line.indexOf('=');

                if (pos >= 0) {
                    String key = line.substring(0, pos);
                    String value = line.substring(pos + 1);
                    map.put(key, value);
                }
            }
        }

        return map;
    }

    /**
     * Сохраняет словарь в файл.
     */
    public void save(File file, Map<String, String> map) throws IOException {
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writer.println(entry.getKey() + "=" + entry.getValue());
            }
        }
    }
}
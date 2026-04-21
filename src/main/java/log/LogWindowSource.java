package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * хранит ограниченную очередь логов и уведомляет подписчиков об изменениях
 */
public class LogWindowSource
{
    /** Максимальное количество сообщений в логе */
    private int queueLength;
    /**
     * Хранилище сообщений
     */
    private final LinkedList<LogEntry> messages;
    /** Список подписчиков на обновления лога. */
    private final ArrayList<LogChangeListener> listeners;
    private volatile LogChangeListener[] activeListeners;
    
    public LogWindowSource(int iQueueLength) 
    {
        queueLength = iQueueLength;
        messages = new LinkedList<>();
        listeners = new ArrayList<>();
    }
    
    public void registerListener(LogChangeListener listener)
    {
        synchronized(listeners)
        {
            listeners.add(listener);
            activeListeners = null;
        }
    }
    
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(listeners)
        {
            listeners.remove(listener);
            activeListeners = null;
        }
    }
    
    public void append(LogLevel logLevel, String strMessage)
    {
        /**
         * теперь удаляем первый элемент если вышли за границу
         */
        LogEntry entry = new LogEntry(logLevel, strMessage);
        synchronized (messages) {
            if (messages.size() >= queueLength) {
                messages.removeFirst();
            }
            messages.addLast(entry);
        }

        LogChangeListener [] activeListeners = this.activeListeners;
        if (activeListeners == null)
        {
            synchronized (listeners)
            {
                if (this.activeListeners == null)
                {
                    activeListeners = listeners.toArray(new LogChangeListener [0]);
                    this.activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }

    public int size() {
        synchronized (messages) {
            return messages.size();
        }
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {synchronized (messages) {
            if (startFrom < 0 || startFrom >= messages.size())
            {
                return Collections.emptyList();
            }
            int indexTo = Math.min(startFrom + count, messages.size());
            return new ArrayList<>(messages.subList(startFrom, indexTo));}
    }

    public Iterable<LogEntry> all() {
        synchronized (messages) {
            return new ArrayList<>(messages);
        }
    }
}

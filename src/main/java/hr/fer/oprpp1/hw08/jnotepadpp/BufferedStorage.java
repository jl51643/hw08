package hr.fer.oprpp1.hw08.jnotepadpp;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton model of buffered  storage used to actions like cut and paste
 */
public class BufferedStorage {

    /**
     * List of registered listeners
     */
    private final List<BufferedStorageListener> listenerList = new ArrayList<>();

    /**
     * Singleton instance of buffered storage
     */
    private static final BufferedStorage instance = new BufferedStorage();

    /**
     * Stored value of this buffered storage
     */
    private String storage;

    /**
     * Constructing singleton instance of this buffered storage
     */
    private BufferedStorage() {
        this.setStorage(null);
    }

    /**
     * @return returns singleton instance of buffered storage
     */
    public static BufferedStorage getInstance() {
        return instance;
    }

    /**
     * Stores string
     *
     * @param storage string to store
     */
    public void setStorage(String storage) {
        this.storage = storage;
        fire();
        System.out.println(storage);
    }

    /**
     * @return returns stored value
     */
    public String getStorage() {
        return storage;
    }

    /**
     * Registers buffered storage listener
     *
     * @param l buffered storage listener
     */
    public void addBufferedStorageListener(BufferedStorageListener l) {
        this.listenerList.add(l);
    }

    /**
     * Unregisters buffered storage listener
     *
     * @param l buffered storage listener
     */
    public  void removeBufferedStorageListener(BufferedStorageListener l) {
        this.listenerList.remove(l);
    }

    /**
     * Notifies all registered listeners
     */
    void fire(){
        for (BufferedStorageListener l : listenerList) {
            l.storageChanged();
        }
    }
}

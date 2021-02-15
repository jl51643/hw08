package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract model of localization provider
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider{

    /**
     * List of registered listeners
     */
    private final List<ILocalizationListener> listeners = new ArrayList<>();

    public AbstractLocalizationProvider() {
    }

    @Override
    public void addLocalizationListener(ILocalizationListener l) {
        this.listeners.add(l);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener l) {
        this.listeners.remove(l);
    }

    /**
     * Alerts all registered listeners
     */
    void fire(){
        for (ILocalizationListener l : listeners) {
            l.localizationChanged();
        }
    }

}

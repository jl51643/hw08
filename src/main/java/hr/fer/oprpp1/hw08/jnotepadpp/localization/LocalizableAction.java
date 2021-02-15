package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Model of localizable action
 */
public class LocalizableAction extends AbstractAction {

    /**
     * Localization provider
     */
    private final ILocalizationProvider provider;

    /**
     * Localization key
     */
    private final String key;

    /**
     * Constructing new localized action
     *
     * @param key localization key
     * @param provider localization provider
     */
    public LocalizableAction(String key, ILocalizationProvider provider) {
        this.provider = provider;
        this.key = key;
        String translation = provider.getString(key);
        putValue(NAME, translation);

        provider.addLocalizationListener(() -> {
            String translation1 = provider.getString(key);
            putValue(NAME, translation1);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String translation = provider.getString(key);
        putValue(NAME, translation);
    }

}

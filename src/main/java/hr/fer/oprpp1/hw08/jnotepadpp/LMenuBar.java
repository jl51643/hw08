package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;

import javax.swing.*;

/**
 * Model of localizable menu
 */
public class LMenuBar extends JMenu {

    /**
     * Localization key
     */
    private final String key;

    /**
     * Localization provider
     */
    private final ILocalizationProvider provider;

    /**
     * Constructing new Localized menu
     *
     * @param key localization key
     * @param provider localization provider
     */
    public LMenuBar(String key, ILocalizationProvider provider) {
        this.provider = provider;
        this.key = key;
        updateText();

        provider.addLocalizationListener(this::updateText);
    }

    /**
     * Translates localized text
     */
    private void updateText() {
        String translation = provider.getString(key);
        setText(translation);
    }
}

package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

import java.awt.event.ActionEvent;

/**
 * Action used to start change of language of application
 */
public class LanguageAction extends LocalizableAction {

    /**
     * Language of application
     */
    private final String language;

    private final ILocalizationProvider provider;

    /**
     * Constructing new language action
     *
     * @param key localization key
     * @param provider localization provider
     * @param language language of application
     */
    public LanguageAction(String key, ILocalizationProvider provider, String language) {
        super(key, provider);
        this.provider = provider;
        this.language = language;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LocalizationProvider.getInstance().setLanguage(this.language);
    }
}

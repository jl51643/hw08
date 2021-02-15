package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton model of localization provider
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    /**
     * Singleton instance of this localization provider
     */
    private static final LocalizationProvider instance = new LocalizationProvider();

    /**
     * Current language
     */
    private String language;

    /**
     * Resource bundle
     */
    private ResourceBundle bundle;

    /**
     * Constructing singleton instance of localization provider
     */
    private LocalizationProvider() {
        this.setLanguage("en");
    }

    /**
     * @return returns instance singleton localization provider
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }

    /**
     * Sets language for this localization provider
     *
     * @param language language
     */
    public void setLanguage(String language) {
        this.language = language;
        Locale locale = Locale.forLanguageTag(this.language);
        this.bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.translations.translations", locale);
        fire();
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return this.language;
    }
}

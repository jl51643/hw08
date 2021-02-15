package hr.fer.oprpp1.hw08.jnotepadpp.localization;

/**
 * Model of localization provider
 */
public interface ILocalizationProvider {

    /**
     * Registers localization listener
     *
     * @param l localization listener
     */
    void addLocalizationListener(ILocalizationListener l);

    /**
     * Unregisters localization listener
     *
     * @param l localization listener
     */
    void removeLocalizationListener(ILocalizationListener l);

    /**
     * Returns translation of string
     *
     * @param s key
     * @return returns translation of string
     */
    String getString(String s);

    /**
     * @return Returns current language
     */
    String getCurrentLanguage();
}

package hr.fer.oprpp1.hw08.jnotepadpp.localization;

/**
 * Model of localization provider bridge
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider{

    /**
     * Shows if bridge is connected
     */
    private boolean connected;

    /**
     * Localization provider parent
     */
    private ILocalizationProvider parent;

    public LocalizationProviderBridge(ILocalizationProvider localizationProvider) {
        this.connected = true;
        this.parent = localizationProvider;
        parent.addLocalizationListener(this::fire);
    }

    /**
     * Disconnects bridge
     */
    public void disconnect() {
        if (connected)
            this.connected = false;
    }

    /**
     * Connects bridge
     */
    public void connect() {
        this.connected = true;
        parent.addLocalizationListener(() -> {
        });
    }

    @Override
    public String getString(String s) {
        return parent.getString(s);
    }

    @Override
    public String getCurrentLanguage() {
        return parent.getCurrentLanguage();
    }

}

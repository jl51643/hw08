package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Form model of localization provider
 */
public class FormLocalizationProvider extends LocalizationProviderBridge{

    /**
     * Constructing new form localization provider
     *
     * @param localizationProvider localization provider
     * @param window parent frame
     */
    public FormLocalizationProvider(ILocalizationProvider localizationProvider, JFrame window) {
        super(localizationProvider);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                FormLocalizationProvider.super.connect();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                FormLocalizationProvider.super.disconnect();
            }
        });
    }
}

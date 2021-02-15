package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * Action used to exit application
 */
public class ExitAction extends LocalizableAction {

    /**
     * Parent frame
     */
    private final JFrame parent;

    /**
     * Construing new exit action
     *
     * @param key localization key
     * @param provider localization provider
     * @param parent parent frame
     */
    public ExitAction(String key, ILocalizationProvider provider, JFrame parent) {
        super(key, provider);
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Arrays.stream(parent.getWindowListeners())
                .forEach(windowListener -> windowListener.windowClosing(null));
    }
}

package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Action used to save document
 */
public class SaveDocumentAction extends LocalizableAction {

    /**
     * Parent frame
     */
    private final JFrame parent;

    /**
     * Tabbed pane with documents
     */
    private final DefaultMultipleDocumentModel model;

    /**
     * Localization provider
     */
    private final ILocalizationProvider provider;

    /**
     * Localized text
     */
    private String ok, errorMessageLoading, errorTitle, errorMessageAlreadyExists, fileSavedTitle, fileSavedMessage;

    /**
     * Localized options for <code>JOptionPane</code> dialog
     */
    private final String[] options;

    /**
     * Constructing new save document action
     *
     * @param key localization key
     * @param provider localization key
     * @param parent parent frame
     * @param model tabbed pane with documents
     */
    public SaveDocumentAction(String key, ILocalizationProvider provider, JFrame parent, DefaultMultipleDocumentModel model) {
        super(key, provider);
        this.parent = parent;
        this.model = model;
        this.provider = provider;
        this.options = new String[]{this.ok};
        updateStringValues();

        provider.addLocalizationListener(this::updateStringValues);

    }

    /**
     * Translates localized text
     */
    private void updateStringValues() {
        ok = provider.getString("ok");
        options[0] = ok;
        errorMessageLoading = provider.getString("error_message_loading");
        errorTitle = provider.getString("error");
        errorMessageAlreadyExists = provider.getString("error_message_file_already_exists");
        fileSavedTitle = provider.getString("file_saved_title");
        fileSavedMessage = provider.getString("file_saved_message");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        saveDocument(null);
    }

    /**
     * Saves document to given path, if given path is <code>null</code>
     * then saves document to it's default path
     *
     * @param path path to save document
     */
    public void saveDocument(Path path) {
        try {
            model.saveDocument(model.getCurrentDocument(), path);
        } catch (IOException ex) {
            JOptionPane.showOptionDialog(parent, errorMessageLoading, errorTitle, JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            return;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showOptionDialog(parent, errorMessageAlreadyExists, errorTitle, JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        }
        String title = model.getCurrentDocument().getFilePath().getFileName().toString();
        JOptionPane.showOptionDialog(parent, fileSavedMessage + title, fileSavedTitle, JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }
}

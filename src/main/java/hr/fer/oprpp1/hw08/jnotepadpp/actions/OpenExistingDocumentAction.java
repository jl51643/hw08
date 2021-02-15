package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Action used to open existing document
 */
public class OpenExistingDocumentAction extends LocalizableAction {

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
    private String openFile, fileNotExist, error;

    /**
     * Constructing new open existing action
     *
     * @param key localization key
     * @param provider localization provider
     * @param model tabbed pane with documents
     */
    public OpenExistingDocumentAction(String key, ILocalizationProvider provider, DefaultMultipleDocumentModel model) {
        super(key, provider);
        this.model = model;
        this.provider = provider;

        provider.addLocalizationListener(this::updateStringValues);
    }

    /**
     * Translates localized text
     */
    private void updateStringValues() {
        openFile = provider.getString("open_file");
        fileNotExist = provider.getString("file_not_exist");
        error = provider.getString("error");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(openFile);
        if(fc.showOpenDialog(model) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File fileName = fc.getSelectedFile();
        Path filePath = fileName.toPath();
        if(!Files.isReadable(filePath)) {
            JOptionPane.showMessageDialog(model, fileNotExist + fileName.getAbsolutePath(), error, JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            model.loadDocument(filePath);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

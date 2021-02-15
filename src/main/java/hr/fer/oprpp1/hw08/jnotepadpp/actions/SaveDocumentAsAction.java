package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;

/**
 * Action used to save document to certain path
 */
public class SaveDocumentAsAction extends LocalizableAction {

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
    private String saveFile;

    /**
     * Constructing new save document as action
     *
     * @param key localization key
     * @param provider localization provider
     * @param parent parent frame
     * @param model tabbed pane with documents
     */
    public SaveDocumentAsAction(String key, ILocalizationProvider provider, JFrame parent, DefaultMultipleDocumentModel model) {
        super(key, provider);
        this.parent = parent;
        this.model = model;
        this.provider = provider;

        updateStringValues();

        provider.addLocalizationListener(this::updateStringValues);
    }

    /**
     * Translates localized text
     */
    private void updateStringValues() {
        saveFile = provider.getString("save_file");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(saveFile);
        if (fc.showOpenDialog(model) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File fileName = fc.getSelectedFile();
        Path filePath = fileName.toPath();

        new SaveDocumentAction("save", provider, parent, model).saveDocument(filePath);
    }
}

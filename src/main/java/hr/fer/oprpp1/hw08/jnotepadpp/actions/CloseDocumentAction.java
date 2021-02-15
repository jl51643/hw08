package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Model of action used to close single document
 */
public class CloseDocumentAction extends LocalizableAction {

    /**
     * Parent frame
     */
    private final JFrame parent;

    /**
     * Model of tabbed panel with documents
     */
    private final DefaultMultipleDocumentModel model;

    /**
     * Localization provider
     */
    private final ILocalizationProvider provider;

    /**
     * Keys for <code>JOptionPane</code> dialog buttons
     */
    private final String[] keys = new String[] {"yes", "no", "cancel"};

    /**
     * Options for <code>JOptionPane</code> dialog buttons
     */
    private final String[] options = new String[keys.length];

    /**
     * Localized text
     */
    private String closingTitle, closingMessage, closingQuestion, unnamed;

    /**
     * Constructing new close document action
     *
     * @param key localized name
     * @param provider localization provider
     * @param parent parent frame
     * @param model tabbed model with documents
     */
    public CloseDocumentAction(String key, ILocalizationProvider provider, JFrame parent, DefaultMultipleDocumentModel model) {
        super(key, provider);
        this.parent = parent;
        this.model = model;
        this.provider = provider;

        updateStringValues();

        provider.addLocalizationListener(this::updateStringValues);
    }

    /**
     * Translates all localized strings
     */
    private void updateStringValues() {
        for (int i = 0; i < keys.length; i++) {
            options[i] = provider.getString(keys[i]);
        }
        closingTitle = provider.getString("warning");
        closingMessage = provider.getString("warning_message");
        closingQuestion = provider.getString("warning_question");
        unnamed = provider.getString("unnamed");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        closeOneDocument();

        if (model.getNumberOfDocuments() == 0)
            model.createNewDocument();
    }

    /**
     * Closes current document
     */
    public void closeOneDocument() {

        SingleDocumentModel currentDocument = model.getCurrentDocument();

        if (!currentDocument.isModified()) {
            model.closeDocument(currentDocument);
            return;
        }

        String title = currentDocument.getFilePath() == null ? unnamed : currentDocument.getFilePath().getFileName().toString();

        int result = JOptionPane.showOptionDialog(parent, closingMessage + title + "\n" + closingQuestion, closingTitle, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (result) {
            case JOptionPane.CLOSED_OPTION:
            case 2 :
                return;
            case 0:
                if (currentDocument.getFilePath() == null)
                    new SaveDocumentAsAction("save", provider, parent, model).actionPerformed(null);
                else
                    new SaveDocumentAction("save_as", provider, parent, model).actionPerformed(null);
                model.closeDocument(currentDocument);
                return;
            case 1:
                model.closeDocument(currentDocument);
        }
    }
}

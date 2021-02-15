package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;
import java.awt.event.ActionEvent;


/**
 * Action used to open new document
 */
public class OpenNewDocumentAction extends LocalizableAction {

    /**
     * Tabbed pane with documents
     */
    private final DefaultMultipleDocumentModel model;

    /**
     * Constructing new open new document action
     *
     * @param key localization key
     * @param provider localization provider
     * @param model tabbed pane with documents
     */
    public OpenNewDocumentAction(String key, ILocalizationProvider provider, DefaultMultipleDocumentModel model) {
        super(key, provider);
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            model.createNewDocument();
    }
}

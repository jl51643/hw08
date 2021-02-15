package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.BufferedStorage;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Action used to cut selected part of text
 */
public class CutAction extends AbstractEditTextAction {

    /**
     * buffered storage
     */
    private final BufferedStorage bufferedStorage;

    /**
     * Constructing new cut action
     *
     * @param key translation key
     * @param provider localization provider
     * @param bufferedStorage buffered storage
     * @param model tabbed model with documents
     */
    public CutAction(String key, ILocalizationProvider provider, BufferedStorage bufferedStorage, DefaultMultipleDocumentModel model) {
        super(key, provider, bufferedStorage, model);
        this.bufferedStorage = bufferedStorage;
    }

    @Override
    public void consumeText(Document document, int offset, int len) throws BadLocationException {
        bufferedStorage.setStorage(document.getText(offset, len));
        document.remove(offset, len);
    }
}

package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.BufferedStorage;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Action used to paste copied or cut text into buffered storage
 */
public class PasteAction extends AbstractEditTextAction {

    /**
     * Buffered storage
     */
    private final BufferedStorage bufferedStorage;

    /**
     * Constructing new paste action
     *
     * @param key localization key
     * @param provider localization provider
     * @param bufferedStorage buffered storage
     * @param model tabbed pane with documents
     */
    public PasteAction(String key, ILocalizationProvider provider, BufferedStorage bufferedStorage, DefaultMultipleDocumentModel model) {
        super(key, provider, bufferedStorage, model);
        this.bufferedStorage = bufferedStorage;
    }

    @Override
    public void consumeText(Document document, int offset, int len) throws BadLocationException {
        document.insertString(offset, bufferedStorage.getStorage(), null);
    }
}

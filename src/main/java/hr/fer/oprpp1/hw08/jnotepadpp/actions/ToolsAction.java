package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.BufferedStorage;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Abstract action used to edit selected text
 */
public abstract class ToolsAction extends AbstractEditTextAction {

    /**
     * Constructing new tools action
     *
     * @param key localization key
     * @param provider localization provider
     * @param bufferedStorage buffered storage
     * @param model tabbed pane with documents
     */
    public ToolsAction(String key, ILocalizationProvider provider, BufferedStorage bufferedStorage, DefaultMultipleDocumentModel model) {
        super(key, provider, bufferedStorage, model);
    }

    @Override
    public void consumeText(Document document, int offset, int len) throws BadLocationException {
        String text = document.getText(offset, len);
        text = changeCase(text);
        document.remove(offset, len);
        document.insertString(offset, text, null);
    }

    /**
     * Returns string with changed case
     *
     * @param text selected text of document
     * @return returns string with changed case
     */
    public abstract String changeCase(String text);
}

package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.BufferedStorage;
import hr.fer.oprpp1.hw08.jnotepadpp.BufferedStorageListener;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;


/**
 * Abstract action used to edit text
 */
public abstract class AbstractEditTextAction extends LocalizableAction {

    /**
     * Tabbed model of documents
     */
    private final DefaultMultipleDocumentModel model;

    /**
     * Buffered storage to
     */
    private String storage;

    /**
     * Model of buffered storage
     */
    private final BufferedStorage bufferedStorage;

    /**
     * Constructing new action to edit text
     *
     * @param key localization key
     * @param lp localization provider
     * @param bufferedStorage buffered storage
     * @param model tabbed model with documents
     */
    public AbstractEditTextAction(String key, ILocalizationProvider lp, BufferedStorage bufferedStorage, DefaultMultipleDocumentModel model) {
        super(key, lp);
        this.model = model;
        this.bufferedStorage = bufferedStorage;

        updateStorage();

        bufferedStorage.addBufferedStorageListener(new BufferedStorageListener() {
            @Override
            public void storageChanged() {
                updateStorage();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextComponent component = model.getCurrentDocument().getTextComponent();
        Document document = component.getDocument();

        int len = Math.abs(component.getCaret().getDot() - component.getCaret().getMark());
        int offset = Math.min(component.getCaret().getDot(),component.getCaret().getMark());

        try {
            consumeText(document, offset, len);
        } catch (BadLocationException badLocationException) {
            badLocationException.printStackTrace();
        }
    }

    /**
     * Updates storage
     */
    private void updateStorage() {
        this.storage = bufferedStorage.getStorage();
    }

    /**
     * Consume text
     *
     * @param document document
     * @param offset offset in document
     * @param len length of selected text
     * @throws BadLocationException if given offset for given document do not exist
     */
    public abstract void consumeText(Document document, int offset, int len) throws BadLocationException;

    /**
     * @return returns this model
     */
    public DefaultMultipleDocumentModel getModel() {
        return model;
    }

    /**
     * @return returns buffered storage
     */
    public BufferedStorage getBufferedStorage() {
        return bufferedStorage;
    }
}

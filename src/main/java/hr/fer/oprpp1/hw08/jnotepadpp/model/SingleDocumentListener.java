package hr.fer.oprpp1.hw08.jnotepadpp.model;

/**
 *  Interface for an observer to register to receive notifications
 *  of changes to a document model.
 */
public interface SingleDocumentListener {
    /**
     * Gives notification that an attribute which tracks if
     * document has been modified is changed
     *
     * @param model Model which status is changed
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * Gives notification that an attribute of documents file path
     * is changed
     *
     * @param model Model which path is changed
     */
    void documentFilePathUpdated(SingleDocumentModel model);
}

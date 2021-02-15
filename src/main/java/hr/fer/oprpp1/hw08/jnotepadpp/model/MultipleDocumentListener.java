package hr.fer.oprpp1.hw08.jnotepadpp.model;

/**
 * Interface for an observer to register to receive notifications
 * of changes to a multiple document model.
 */
public interface MultipleDocumentListener {
    /**
     * Gives notification that focus is changed from <code>previousModel</code>
     * to <code>currentModel</code>
     *
     * @param previousModel last document in focus
     * @param currentModel document in focus
     */
    void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

    /**
     * Gives notification that certain document is added
     *
     * @param model Added document
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * Gives notification that certain document is removed
     *
     * @param model removed document model
     */
    void documentRemoved(SingleDocumentModel model);
}

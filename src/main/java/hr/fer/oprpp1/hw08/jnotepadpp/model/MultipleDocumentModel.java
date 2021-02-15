package hr.fer.oprpp1.hw08.jnotepadpp.model;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface for model which holds multiple single documents
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
    /**
     * @return Returns new document model of empty document
     */
    SingleDocumentModel createNewDocument() throws IOException;

    /**
     * @return Returns document this is currently in focus
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * Loads document from specified path
     *
     * @param path path of document
     * @return return document model from given path
     */
    SingleDocumentModel loadDocument(Path path) throws IOException;

    /**
     * Saves changes in document model to given path
     *
     * @param model document model
     * @param newPath path of document
     */
    void saveDocument(SingleDocumentModel model, Path newPath) throws IOException;

    /**
     * Closes given document
     *
     * @param model model of document which is closed
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * Adds <code>MultipleDocumentListener</code> to list of listeners
     * registered over this <code>MultipleDocumentModel</code>
     *
     * @param l <code>MultipleDocumentListener</code>
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Removes <code>MultipleDocumentListener</code> from list of listeners
     * registered over this <code>MultipleDocumentModel</code>
     *
     * @param l <code>MultipleDocumentListener</code>
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * @return Returns number of currently opened documents
     */
    int getNumberOfDocuments();

    /**
     * Returns document model on given index
     *
     * @param index index of document
     * @return returns document model on given index
     */
    SingleDocumentModel getDocument(int index);
}

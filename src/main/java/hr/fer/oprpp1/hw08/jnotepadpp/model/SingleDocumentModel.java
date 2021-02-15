package hr.fer.oprpp1.hw08.jnotepadpp.model;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Interface for single model of document
 */
public interface SingleDocumentModel {
    /**
     * @return Returns text component of this document
     */
    JTextArea getTextComponent();

    /**
     * @return Returns path of this document
     */
    Path getFilePath();

    /**
     * Sets path attribute of this document
     *
     * @param path new path of this document
     */
    void setFilePath(Path path);

    /**
     * @return Returns true if document is modified, false otherwise
     */
    boolean isModified();

    /**
     * Sets document's modified status
     *
     * @param modified document's modified status
     */
    void setModified(boolean modified);

    /**
     * Adds <code>SingleDocumentListener</code> to list of listeners
     * registered over this <code>SingleDocumentListener</code>
     *
     * @param l <code>SingleDocumentListener</code>
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * Removes <code>SingleDocumentListener</code> from list of listeners
     * registered over this <code>SingleDocumentListener</code>
     *
     * @param l <code>SingleDocumentListener</code>
     */
    void removeSingleDocumentListener(SingleDocumentListener l);
}

package hr.fer.oprpp1.hw08.jnotepadpp.model;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model of single document
 */
public class DefaultSingleDocumentModel implements  SingleDocumentModel {
    /**
     * <SingleDocumentListener> registered over this document model
     */
    private final List<SingleDocumentListener> listeners = new ArrayList<>();

    /**
     * Path of this document
     */
    private Path filePath;

    /**
     * Text content of this document
     */
    private final String textContent;

    /**
     * Text component showing text of this document
     */
    private final JTextArea textArea;

    /**
     * Flag that determines if document is edited
     */
    private boolean modified;

    /**
     * Constructing new Single document model
     *
     * @param filePath path of document
     * @param textContent text content of document
     */
    public DefaultSingleDocumentModel(Path filePath, String textContent) {
        this.modified = false;
        this.filePath = filePath;
        this.textContent = textContent;
        this.textArea = getTextComponent();

        this.textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                System.out.println("dsm insert 54");

                setModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                System.out.println("dsm remove 60");

                setModified(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                System.out.println("dsm change 66");

                setModified(true);

                fireModifyStatusListeners();
            }
        });

    }

    /**
     * @return Returns new component with text content of this document
     */
    @Override
    public JTextArea getTextComponent() {
        return Objects.requireNonNullElseGet(this.textArea, () -> new JTextArea(this.textContent));
    }

    /**
     * @return Returns path of this file
     */
    @Override
    public Path getFilePath() {
        return this.filePath;
    }

    /**
     * Sets path attribute of this document
     *
     * @param path new path of this document
     */
    @Override
    public void setFilePath(Path path) {
        this.filePath = path;
        fireFilePathListeners();
    }

    /**
     * @return Returns true if document is modified, false otherwise
     */
    @Override
    public boolean isModified() {
        return this.modified;
    }

    /**
     * Sets document's modified status
     *
     * @param modified document's modified status
     */
    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
        fireModifyStatusListeners();
    }

    /**
     * Adds <code>SingleDocumentListener</code> to list of listeners
     * registered over this <code>SingleDocumentListener</code>
     *
     * @param l <code>SingleDocumentListener</code>
     */
    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(l);
    }

    /**
     * Removes <code>SingleDocumentListener</code> from list of listeners
     * registered over this <code>SingleDocumentListener</code>
     *
     * @param l <code>SingleDocumentListener</code>
     */
    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies all registered listeners that modify status is changed
     */
    private void fireModifyStatusListeners() {
        for (SingleDocumentListener l : listeners)
            l.documentModifyStatusUpdated(this);
    }

    /**
     * Notifies all registered listeners that file path is changed
     */
    private void fireFilePathListeners() {
        for (SingleDocumentListener l : listeners)
            l.documentFilePathUpdated(this);
    }
}

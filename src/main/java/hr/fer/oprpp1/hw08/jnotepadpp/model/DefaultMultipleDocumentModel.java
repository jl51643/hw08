package hr.fer.oprpp1.hw08.jnotepadpp.model;

import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Model containing multiple models of documents
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

    /**
     * Opened documents
     */
    private final List<SingleDocumentModel> documents = new ArrayList<>();

    /**
     * Listeners registered over this model
     */
    private final List<MultipleDocumentListener> listeners = new ArrayList<>();

    /**
     * Focused document
     */
    private SingleDocumentModel currentDocument;

    /**
     * Localized text
     */
    private String unnamed;

    /**
     * Localization provider
     */
    private final ILocalizationProvider provider;

    /**
     * Creating new multiple document model
     */
    public DefaultMultipleDocumentModel(JFrame parent) {
        super(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        this.provider = LocalizationProvider.getInstance();

        unnamed = provider.getString("unnamed");

        provider.addLocalizationListener(() -> unnamed = provider.getString("unnamed"));

        this.addChangeListener(e -> {

            System.out.println("dmm state change 55");

            if (documents.size() == 0)
                return;
            fireCurrentDocumentChangedListeners(documents.get(getSelectedIndex()));

            String filePath = currentDocument.getFilePath() == null ? unnamed : DefaultMultipleDocumentModel.this.getDocument(model.getSelectedIndex()).getFilePath().toString();
            parent.setTitle(filePath + " - JNotepad++");

        });

        this.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {

                System.out.println("dmm current doc changed 74");

                if (documents.size() == 0)
                    return;
                currentDocument = currentModel;
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {

                System.out.println("dmm doc aded 85");

                model.addSingleDocumentListener(new SingleDocumentListener() {
                    @Override
                    public void documentModifyStatusUpdated(SingleDocumentModel model) {

                        System.out.println("dmm modifi status 91");
                    }

                    @Override
                    public void documentFilePathUpdated(SingleDocumentModel model) {
                        System.out.println("dmm path update 96");
                        DefaultMultipleDocumentModel.this.setTitleAt(DefaultMultipleDocumentModel.this.documents.indexOf(model), model.getFilePath().getFileName().toString());
                    }
                });
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {

                System.out.println("dmm doc removed 107");
            }
        });

    }

    /**
     * @return Returns new document model of empty document
     */
    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel newDocument =  new DefaultSingleDocumentModel(null, null);
        this.addDocument(newDocument);

        setSelectedIndex(documents.indexOf(newDocument));
        fireStateChanged();

        return newDocument;
    }

    /**
     * @return Returns document this is currently in focus
     */
    @Override
    public SingleDocumentModel getCurrentDocument() {
        return this.currentDocument;
    }

    /**
     * Loads document from specified path
     *
     * @param path path of document
     * @return return document model from given path
     */
    @Override
    public SingleDocumentModel loadDocument(Path path) throws IOException {
        if (path == null)
            throw new IllegalArgumentException("Can not open file");
        String textContext;
        try {
            textContext = Files.readString(path);
        } catch (IOException e) {
            System.out.println("Problem occurred while opening file: " + path.getFileName().toString() + ".");
            return null;
        }

        SingleDocumentModel loadedDocument = null;
        for (SingleDocumentModel document : documents) {
            if (document.getFilePath() != null && document.getFilePath().toString().equals(path.toString())) {
                loadedDocument = document;
            }
        }
        if (loadedDocument == null) {
            loadedDocument = new DefaultSingleDocumentModel(path, textContext);
            fireDocumentAddedListeners(loadedDocument);

            if (getNumberOfDocuments() == 1 && documents.get(0).getFilePath() == null && !documents.get(0).isModified()) {
                closeDocument(getDocument(0));
            }

            this.addDocument(loadedDocument);
        }

        setSelectedIndex(documents.indexOf(loadedDocument));
        fireStateChanged();

        return loadedDocument;
    }

    /**
     * Opens new document model
     *
     * @param document document model
     */
    private void addDocument(SingleDocumentModel document) {
        String title = document.getFilePath() == null ? unnamed : document.getFilePath().getFileName().toString();
        String path = document.getFilePath() == null ? unnamed : document.getFilePath().toString();
        ImageIcon icon = new ImageIcon(JNotepadPP.class.getResource("icons/saveIcon.png"));
        Image image = icon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
        this.addTab(title, new ImageIcon(image), new JScrollPane(document.getTextComponent()), path);

        this.documents.add(document);
    }

    /**
     * Saves changes in document model to given path
     *
     * @param model document model
     * @param newPath path of document
     * @throws IllegalArgumentException if given path is null
     * @throws IOException if there is problem with writing file
     */
    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) throws IOException {
        if (newPath != null && Files.exists(newPath))
            throw new IllegalArgumentException("File already exists");
        if (newPath != null)
            model.setFilePath(newPath);

        FileOutputStream os = new FileOutputStream(model.getFilePath().toFile());
        os.write(model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8));

        model.setModified(false);
    }

    /**
     * Closes given document
     *
     * @param model model of document which is closed
     */
    @Override
    public void closeDocument(SingleDocumentModel model) {
        this.documents.remove(model);

        this.remove(this.getSelectedIndex());

        fireDocumentRemovedListeners(model);
    }

    /**
     * Adds <code>MultipleDocumentListener</code> to list of listeners
     * registered over this <code>MultipleDocumentModel</code>
     *
     * @param l <code>MultipleDocumentListener</code>
     */
    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.add(l);
    }

    /**
     * Removes <code>MultipleDocumentListener</code> from list of listeners
     * registered over this <code>MultipleDocumentModel</code>
     *
     * @param l <code>MultipleDocumentListener</code>
     */
    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.remove(l);
    }

    /**
     * @return Returns number of currently opened documents
     */
    @Override
    public int getNumberOfDocuments() {
        return documents.size();
    }

    /**
     * Returns document model on given index
     *
     * @param index index of document
     * @return returns document model on given index
     * @throws IllegalArgumentException if there is no document on given index
     */
    @Override
    public SingleDocumentModel getDocument(int index) {
        if (index < 0 || index >= this.documents.size())
            throw new IllegalArgumentException("There is no document on position: " + index + ".");
        return this.documents.get(index);
    }

    /**
     * @return Returns iterator for tabbed pane
     */
    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return new TabbedIterator();
    }

    /**
     * Model of iterator
     */
    private class TabbedIterator implements Iterator<SingleDocumentModel> {

        private  int i;

        public TabbedIterator() {
            this.i = 0;
        }

        @Override
        public boolean hasNext() {
            return DefaultMultipleDocumentModel.this.documents.size() > i;
        }

        @Override
        public SingleDocumentModel next() {
            return DefaultMultipleDocumentModel.this.documents.get(i++);
        }
    }

    /**
     * Notifies all registered listeners that currently focused document is changed
     *
     * @param currentDocument document that will be currently focused document
     */
    private void fireCurrentDocumentChangedListeners(SingleDocumentModel currentDocument) {
        for (MultipleDocumentListener l : listeners)
            l.currentDocumentChanged(this.currentDocument, currentDocument);
    }

    /**
     * Notifies all registered listeners that is new document opened
     *
     * @param newDocument new opened document
     */
    private void fireDocumentAddedListeners(SingleDocumentModel newDocument) {
        for (MultipleDocumentListener l : listeners)
            l.documentAdded(newDocument);
    }

    /**
     * Notifies all registered listeners that given document is closed
     *
     * @param removedDocument closed document
     */
    private void fireDocumentRemovedListeners(SingleDocumentModel removedDocument) {
        for (MultipleDocumentListener l : listeners)
            l.documentRemoved(removedDocument);
    }
}

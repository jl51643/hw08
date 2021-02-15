package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.actions.CloseDocumentAction;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Window adapter
 */
public class ExitApplication extends WindowAdapter {

    /**
     * Parent frame
     */
    private final JFrame parent;

    /**
     * Tabbed pane with documents
     */
    private final DefaultMultipleDocumentModel model;

    /**
     * Constructing new window adapter
     *
     * @param parent parent frame
     * @param model tabbed pane with documents
     */
    public ExitApplication(JFrame parent, DefaultMultipleDocumentModel model) {
        super();
        this.parent = parent;
        this.model = model;
    }

    @Override
    public void windowClosing(WindowEvent e) {

        System.out.println("ExitApp wl 49");

        int numberOfDocuments = model.getNumberOfDocuments();
        for (int i = 0; i < numberOfDocuments; i++) {
            int numberOfDocumentsBeforeClosing = model.getNumberOfDocuments();
            new CloseDocumentAction("close", LocalizationProvider.getInstance(), parent, model).closeOneDocument();
            if (numberOfDocumentsBeforeClosing == model.getNumberOfDocuments())
                break;
        }
        if (model.getNumberOfDocuments() == 0)
            parent.dispose();
    }

}

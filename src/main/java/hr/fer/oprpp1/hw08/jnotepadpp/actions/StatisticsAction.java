package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;

/**
 * Action used to calculate statistic informations of current document
 */
public class StatisticsAction extends LocalizableAction {

    /**
     * Parent frame
     */
    private final JFrame parent;

    /**
     * Localization provider
     */
    private final ILocalizationProvider provider;

    /**
     * Tabbed pane with documents
     */
    private final DefaultMultipleDocumentModel model;

    /**
     * Localized text
     */
    private String ok, statistics, carNum, lineNum, nonBlank;
    private final String[] options;

    /**
     * Constructing new statistics action
     *
     * @param key localization key
     * @param provider localization provider
     * @param parent parent frame
     * @param model tabbed pane with documents
     */
    public StatisticsAction(String key, ILocalizationProvider provider, JFrame parent, DefaultMultipleDocumentModel model) {
        super(key, provider);
        this.parent = parent;
        this.model = model;
        this.provider = provider;
        this.options = new String[] {this.ok};

        updateStringValues();

        provider.addLocalizationListener(this::updateStringValues);
    }

    /**
     * Translates localized text
     */
    private void updateStringValues() {
        ok = provider.getString("ok");
        options[0] = ok;
        statistics = provider.getString("statistics");
        carNum = provider.getString("car_num");
        lineNum = provider.getString("line_num");
        nonBlank = provider.getString("non_blank");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextArea component = model.getCurrentDocument().getTextComponent();
        Document document = component.getDocument();

        int countOfAllCharacters = document.getLength();
        int countOFNonBlankCharacters = component.getText().replaceAll("\\s+", "").length();
        int lineCount = component.getLineCount();

        String dialogMessage = String.format("%s%d%n%s%d%n%s%d",carNum, countOfAllCharacters, lineNum, lineCount, nonBlank, countOFNonBlankCharacters);
        JOptionPane.showOptionDialog(parent, dialogMessage, statistics, JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }
}

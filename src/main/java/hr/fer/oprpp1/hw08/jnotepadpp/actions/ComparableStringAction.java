package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract model of action that operates actions with comparing strings
 */
public abstract class ComparableStringAction extends LocalizableAction {

    /**
     * Localization provider
     */
    private final ILocalizationProvider provider;

    /**
     * Tabbed pane with documents
     */
    private final DefaultMultipleDocumentModel model;

    /**
     * Current language
     */
    private String language;

    /**
     * Locale region provider
     */
    private Locale locale;

    /**
     * Locale sensitive comparator
     */
    private Collator collator;

    /**
     * @param key translation key
     * @param provider localization provider
     */
    public ComparableStringAction(String key, ILocalizationProvider provider, DefaultMultipleDocumentModel model) {
        super(key, provider);
        this.provider = provider;
        this.model = model;

        updateLanguage();

        provider.addLocalizationListener(this::updateLanguage);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Document doc = model.getCurrentDocument().getTextComponent().getDocument();

        int offset = getOffset();

        int len = getLen(offset);
        if (len == 0)
            return;

        try {
            String[] lines = getSelectedLines(offset, len);
            doc.remove(offset, len);
            lines = consumeLines(lines);
            doc.insertString(offset, Arrays.stream(lines).collect(Collectors.joining("\n", "", "\n")), null);
        } catch (BadLocationException badLocationException) {
            badLocationException.printStackTrace();
        }

    }

    /**
     * Returns offset of line start in which is smaller of caret dot or caret mark
     *
     * @return returns offset of line start caret
     */
    private int getOffset() {
        JTextArea area = model.getCurrentDocument().getTextComponent();
        Caret caret = area.getCaret();
        int offset = Math.min(caret.getDot(), caret.getMark());

        try {
            int line = area.getLineOfOffset(offset);
            if (area.getLineStartOffset(line) != offset) {
                offset = area.getLineStartOffset(line);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return offset;
    }

    /**
     * Returns length of selected text,
     * if selection do not capture whole line,
     * rest of line is included in length
     *
     * @param offset start off set of selection
     * @return returns length of selection
     */
    private int getLen(int offset) {
        JTextArea area = model.getCurrentDocument().getTextComponent();
        Caret caret = area.getCaret();

        int end = Math.max(caret.getDot(), caret.getMark());
        if (end == 0)
            return 0;
        try {
            int line = area.getLineOfOffset(end);
            if (area.getLineEndOffset(line) != end) {
                end = area.getLineEndOffset(line);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return end - offset;
    }

    /**
     * Consumes selected lines
     *
     * @param lines selected lines
     * @return returns consumed lines
     */
    public abstract String[] consumeLines(String[] lines);

    private String[] getSelectedLines(int offset, int len) {
        JTextComponent component = model.getCurrentDocument().getTextComponent();
        try {
            return component.getText(offset, len).split("\\n");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates localized attributes
     */
    private void updateLanguage() {
        language = provider.getCurrentLanguage();
        locale = new Locale(language);
        collator = Collator.getInstance(locale);
    }

    /**
     * @return returns localization provider
     */
    public ILocalizationProvider getProvider() {
        return provider;
    }

    /**
     * @return returns current language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return returns locale region provider
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return returns locale sensitive comparator
     */
    public Collator getCollator() {
        return collator;
    }
}

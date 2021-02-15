package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;

import java.util.Arrays;

/**
 * Action used to remove all duplicated lines in selected text
 */
public class UniqueLinesAction extends ComparableStringAction {


    /**
     * Constructing new unique lines action
     *
     * @param key localization key
     * @param provider localization provider
     * @param model tabbed pane with documents
     */
    public UniqueLinesAction(String key, ILocalizationProvider provider, DefaultMultipleDocumentModel model) {
        super(key, provider, model);
    }

    @Override
    public String[] consumeLines(String[] lines) {
        return Arrays.stream(lines)
                .distinct()
                .toArray(String[]::new);
    }
}

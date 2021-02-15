package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;
import java.util.Arrays;

/**
 * Action used to sort string lines
 */
public class SortAction extends ComparableStringAction {

    /**
     * Boolean flag to determine if sort is ascending or descending
     */
    private final boolean asc;

    /**
     * Constructing new sort action
     *
     * @param key localization key
     * @param provider localization provider
     * @param model tabbed pane with documents
     * @param asc boolean flag to determine if sort is ascending or descending
     */
    public SortAction(String key, ILocalizationProvider provider, DefaultMultipleDocumentModel model, boolean asc) {
        super(key, provider, model);
        this.asc = asc;

    }

    @Override
    public String[] consumeLines(String[] lines) {
        return Arrays.stream(lines)
                .sorted((o1, o2) ->
                        asc ? getCollator().compare(o1, o2) : getCollator().compare(o2, o1))
                .toArray(String[]::new);
    }
}

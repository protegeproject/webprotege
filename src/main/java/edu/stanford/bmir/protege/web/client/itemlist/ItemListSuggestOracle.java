package edu.stanford.bmir.protege.web.client.itemlist;

import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.itemlist.*;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public class ItemListSuggestOracle<T> extends SuggestOracle {

    private final CursorPositionProvider cursorPositionProvider;

    private final ItemNameAtCursorParser itemNameAtCursorParser;

    private final GetPossibleItemCompletionsActionFactory<T> factory;

    private final DispatchServiceManager dispatchServiceManager;

    private final ItemRenderer<T> itemRenderer;

    public ItemListSuggestOracle(CursorPositionProvider cursorPositionProvider,
                                 ItemNameAtCursorParser itemNameAtCursorParser,
                                 GetPossibleItemCompletionsActionFactory<T> factory,
                                 DispatchServiceManager dispatchServiceManager,
                                 ItemRenderer<T> itemRenderer) {
        this.cursorPositionProvider = checkNotNull(cursorPositionProvider);
        this.itemNameAtCursorParser = checkNotNull(itemNameAtCursorParser);
        this.factory = checkNotNull(factory);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.itemRenderer = checkNotNull(itemRenderer);
    }

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        final String query = request.getQuery();
        int cursorPos = cursorPositionProvider.getCursorPosition();
        String currentItemName = itemNameAtCursorParser.parseItemNameAtCursor(query, cursorPos);
        if (currentItemName.isEmpty()) {
            // No suggestions
            callback.onSuggestionsReady(request, new Response());
        }
        else {
            // Request suggestions
            GetPossibleItemCompletionsAction<T> action = factory.createGetPossibleItemCompletionsAction(currentItemName);
            dispatchServiceManager.execute(action, new DispatchServiceCallback<GetPossibleItemCompletionsResult<T>>() {
                @Override
                public void handleSuccess(GetPossibleItemCompletionsResult<T> result) {
                    List<T> possibleItemCompletions = result.getPossibleItemCompletions();
                    List<Suggestion> suggestions = generateSuggestionsFromPossibleCompletions(possibleItemCompletions, query);
                    callback.onSuggestionsReady(request, new Response(suggestions));
                }

                @Override
                public void handleErrorFinally(Throwable throwable) {
                    callback.onSuggestionsReady(request, new Response());
                }
            });
        }

    }

    private List<Suggestion> generateSuggestionsFromPossibleCompletions(List<T> possibleItemCompletions, String query) {
        List<Suggestion> suggestions = new ArrayList<>();
        for (final T item : possibleItemCompletions) {
            Suggestion suggestion = buildSuggestion(item, query);
            suggestions.add(suggestion);
        }
        return suggestions;
    }

    /**
     * Build a suggestion for the specified item.
     * @param item The item. Not {@code null}.
     * @param query The original query string.  Not {@code null}.
     */
    private Suggestion buildSuggestion(final T item, final String query) {
        return new Suggestion() {
            @Override
            public String getDisplayString() {
                return itemRenderer.getDisplayString(item);
            }

            @Override
            public String getReplacementString() {
                String[] lines = query.split("\n");
                StringBuilder replacementString = new StringBuilder();
                StringBuilder prefix = new StringBuilder();
                for (String line : lines) {
                    int start = prefix.length();
                    prefix.append(line);
                    prefix.append("\n");
                    int end = prefix.length();
                    int caretPos = cursorPositionProvider.getCursorPosition();
                    if (caretPos >= start && caretPos <= end) {
                        // This is the line where the auto-completion is taking place
                        replacementString.append(itemRenderer.getReplacementString(item));
                    }
                    else {
                        replacementString.append(line);
                    }
                    replacementString.append("\n");
                }
                return replacementString.toString().trim();
            }
        };
    }
}
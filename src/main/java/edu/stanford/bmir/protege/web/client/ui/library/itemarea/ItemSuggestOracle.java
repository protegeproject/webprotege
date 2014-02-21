package edu.stanford.bmir.protege.web.client.ui.library.itemarea;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBoxBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/02/2012
 */
public class ItemSuggestOracle<T> extends SuggestOracle {

    private ItemProvider<T> itemProvider;

    private TextBoxBase textBoxBase;


    public ItemSuggestOracle(ItemProvider<T> itemProvider, TextBoxBase textBoxBase) {
        this.itemProvider = itemProvider;
        this.textBoxBase = textBoxBase;
    }

    @Override
    public void requestSuggestions(Request request, Callback callback) {
        String query = request.getQuery();
        String[] split = query.split("\n");
        Collection<Suggestion> suggestions = new ArrayList<Suggestion>();


        StringBuilder prefix = new StringBuilder();
        for (String line : split) {
            int start = prefix.length();
            prefix.append(line);
            int end = prefix.length();
            int caretPos = textBoxBase.getCursorPos();
            if (caretPos >= start && caretPos <= end) {
                int relativeCaretPos = caretPos - start;
                String autoCompletionString = line.substring(0, relativeCaretPos).trim();
                List<T> matchingItems = itemProvider.getItemsMatching(autoCompletionString);
                for (T item : matchingItems) {
                    final String itemRendering = itemProvider.getRendering(item);
                    final String replacementString = query.substring(0, start) + itemRendering + query.substring(end);
                    suggestions.add(new Suggestion() {
                        public String getDisplayString() {
                            return itemRendering;
                        }

                        public String getReplacementString() {
                            return replacementString;
                        }
                    });
                }
                break;
            }
            prefix.append("\n");
        }

            callback.onSuggestionsReady(request, new Response(suggestions));

    }
}

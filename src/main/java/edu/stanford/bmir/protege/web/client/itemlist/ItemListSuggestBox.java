package edu.stanford.bmir.protege.web.client.itemlist;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/02/2012
 *
 * A suggest box that inhibits the up and down arrow key actions in the text box base if the suggest popup is
 * showing.
 */
public class ItemListSuggestBox<T> extends SuggestBox {

    public ItemListSuggestBox(SuggestOracle suggestOracle, ValueBoxBase<String> textBoxBase) {
        super(suggestOracle, textBoxBase, new ItemListSuggestionDisplay());
    }

    private static class ItemListSuggestionDisplay extends DefaultSuggestionDisplay {

        @Override
        public void hideSuggestions() {
            if (isSuggestionListShowing()) {
                super.hideSuggestions();
                Event.getCurrentEvent().preventDefault();
            }
        }

        @Override
        protected void moveSelectionDown() {
            if (isSuggestionListShowing()) {
                super.moveSelectionDown();
                Event.getCurrentEvent().preventDefault();
            }
        }

        @Override
        protected void moveSelectionUp() {
            if (isSuggestionListShowing()) {
                super.moveSelectionUp();
                Event.getCurrentEvent().preventDefault();
            }
        }


    }
}

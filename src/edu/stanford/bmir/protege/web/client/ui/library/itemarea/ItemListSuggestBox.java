package edu.stanford.bmir.protege.web.client.ui.library.itemarea;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/02/2012
 */
public class ItemListSuggestBox<T> extends SuggestBox {

    private ItemProvider<T> itemProvider;
    
    public ItemListSuggestBox(ItemProvider<T> itemProvider, TextBoxBase textBoxBase) {
        super(new ItemSuggestOracle<T>(itemProvider, textBoxBase), textBoxBase, new ItemListSuggestionDisplay());
        this.itemProvider = itemProvider;
    }
    
    public Set<T> getItems() {
        TextBoxBase base = getTextBox();
        String [] lines = base.getText().split("\n");
        Set<T> items = new HashSet<T>();
        for(String line : lines) {
            final List<T> itemsMatchingExactly = itemProvider.getItemsMatchingExactly(line.trim());
            items.addAll(itemsMatchingExactly);
        }
        return items;
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

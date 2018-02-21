package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.list.ListBoxCellRenderer;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Aug 2017
 */
public class CollectionItemRenderer implements ListBoxCellRenderer<CollectionItem> {

    @Override
    public IsWidget render(CollectionItem element) {
        return new Label(element.getName());
    }
}

package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.list.ListBoxCellRenderer;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Aug 2017
 */
public class CollectionElementRenderer implements ListBoxCellRenderer<CollectionElementId> {

    @Override
    public IsWidget render(CollectionElementId element) {
        return new Label(element.getId());
    }
}

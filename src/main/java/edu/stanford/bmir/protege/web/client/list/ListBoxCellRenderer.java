package edu.stanford.bmir.protege.web.client.list;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Aug 2017
 */
public interface ListBoxCellRenderer<E> {

    IsWidget render(E element);
}

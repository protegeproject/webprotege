package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public interface ChangeListView extends IsWidget {

    void addChangeDetailsView(ChangeDetailsView view);

    void addSeparator(String separatorText);

    void clear();
}

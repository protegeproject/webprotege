package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public interface ChangeListView extends IsWidget, HasPagination {

    void addChangeDetailsView(ChangeDetailsView view);

    void addSeparator(String separatorText);

    void clear();

    void setDetailsVisible(boolean showDetails);

}

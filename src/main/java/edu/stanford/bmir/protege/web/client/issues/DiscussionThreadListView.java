package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public interface DiscussionThreadListView extends HasEnabled, IsWidget {

    void clear();

    void addDiscussionThreadView(DiscussionThreadView view);
}

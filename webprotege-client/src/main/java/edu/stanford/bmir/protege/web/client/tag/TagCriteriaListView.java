package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public interface TagCriteriaListView extends IsWidget {

    interface AddHandler {
        void handleAdd();
    }

    void setAddHandler(@Nonnull AddHandler handler);

    void clearView();

    void addTagCriteriaViewContainer(@Nonnull TagCriteriaViewContainer container,
                                     boolean scrollIntoView,
                                     boolean animate);

    void removeTagCriteriaViewContainer(int viewContainerIndex);
}

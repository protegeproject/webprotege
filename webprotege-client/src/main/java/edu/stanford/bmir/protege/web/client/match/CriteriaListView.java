package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public interface CriteriaListView extends IsWidget {

    void removeCriteriaView(int index);

    void setMultiMatchType(@Nonnull MultiMatchType multiMatchType);

    void removeAllCriteriaViews();

    interface AddCriteriaHandler {
        void handleAddCriteria();
    }

    interface RemoveCriteriaHandler {
        void handleRemoveCriteria(int index);
    }

    void setAddCriteriaHandler(@Nonnull AddCriteriaHandler handler);

    void setRemoveCriteriaHandler(@Nonnull RemoveCriteriaHandler handler);

    void setMatchTextPrefix(@Nonnull String prefix);

    MultiMatchType getMultiMatchType();

    int getCriteriaCount();

    void addCriteriaView(@Nonnull CriteriaListCriteriaViewContainer viewContainer);
}

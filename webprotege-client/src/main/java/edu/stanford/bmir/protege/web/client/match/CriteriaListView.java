package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public interface CriteriaListView extends IsWidget {

    void removeCriteriaView(int index);

    interface AddCriteriaHandler {
        void handleAddCriteria();
    }

    interface RemoveCriteriaHandler {
        void handleRemoveCriteria(int index);
    }

    void setAddCriteriaHandler(@Nonnull AddCriteriaHandler handler);

    void setRemoveCriteriaHandler(@Nonnull RemoveCriteriaHandler handler);

    void setMatchTextPrefix(@Nonnull String prefix);

    int getCriteriaCount();

    void addCriteriaView(@Nonnull CriteriaListCriteriaViewContainer viewContainer);
}

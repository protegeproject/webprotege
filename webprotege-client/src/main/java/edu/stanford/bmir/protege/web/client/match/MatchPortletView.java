package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public interface MatchPortletView extends IsWidget, HasPagination {

    interface ExecuteQueryHandler {
        void handleExecute();
    }

    void setExecuteHandler(@Nonnull ExecuteQueryHandler handler);

    @Nonnull
    AcceptsOneWidget getCriteriaContainer();

    void clearResults();

    void setResult(@Nonnull Page<OWLEntityData> result);
}

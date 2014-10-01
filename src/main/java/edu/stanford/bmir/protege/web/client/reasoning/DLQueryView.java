package edu.stanford.bmir.protege.web.client.reasoning;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.reasoning.DLQueryResult;
import edu.stanford.bmir.protege.web.shared.reasoning.ReasonerError;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import java.util.Collection;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/09/2014
 */
public interface DLQueryView extends IsWidget {

    void setQueryString(String queryString);

    void setMode(DLQueryViewMode mode);

    void setExecuteDLQueryHandler(ExecuteDLQueryHandler handler);

    void clearResults();

    void setResult(DLQueryResult result, RevisionNumber revisionNumber);

    void setReasoningUnavailable();

    void setReasonerBusy();

    void setProjectInconsistent();

    void setReasonerError(String reasonerErrorMessage);

    Optional<String> getFilter();

}

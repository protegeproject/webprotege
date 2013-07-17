package edu.stanford.bmir.protege.web.shared.usage;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.actions.AbstractHasProjectIdAndSubject;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class GetUsageAction extends AbstractHasProjectIdAndSubject<OWLEntity> implements Action<GetUsageResult> {

    private static final int DEFAULT_PAGE_SIZE = 500;

    private UsageFilter usageFilter;

    private GetUsageAction() {
    }

    public GetUsageAction(OWLEntity subject, ProjectId projectId, Optional<UsageFilter> usageFilter) {
        super(subject, projectId);
        this.usageFilter = usageFilter.orNull();
    }

    public UsageFilter getUsageFilter() {
        return usageFilter == null ? new UsageFilter() : usageFilter;
    }

    public int getPageSize() {
        return DEFAULT_PAGE_SIZE;
    }
}

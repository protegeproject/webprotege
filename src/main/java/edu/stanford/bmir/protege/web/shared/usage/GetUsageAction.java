package edu.stanford.bmir.protege.web.shared.usage;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectIdAndSubject;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class GetUsageAction extends AbstractHasProjectIdAndSubject<OWLEntity> implements ProjectAction<GetUsageResult> {

    private static final int DEFAULT_PAGE_SIZE = 500;

    @Nullable
    private UsageFilter usageFilter;

    @GwtSerializationConstructor
    private GetUsageAction() {
    }

    public GetUsageAction(OWLEntity subject, ProjectId projectId, Optional<UsageFilter> usageFilter) {
        super(subject, projectId);
        this.usageFilter = usageFilter.orElse(null);
    }

    public UsageFilter getUsageFilter() {
        return usageFilter == null ? new UsageFilter() : usageFilter;
    }

    public int getPageSize() {
        return DEFAULT_PAGE_SIZE;
    }
}

package edu.stanford.bmir.protege.web.shared.usage;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class GetUsageResult implements Result, HasSignature, HasProjectId {

    private ProjectId projectId;

    private Collection<UsageReference> usageReferences;

    private int totalUsageCount;

    private GetUsageResult() {
    }

    public GetUsageResult(ProjectId projectId, Collection<UsageReference> usageReferences, int totalUsageCount) {
        this.projectId = projectId;
        this.usageReferences = usageReferences;
        this.totalUsageCount = totalUsageCount;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public int getTotalUsageCount() {
        return totalUsageCount;
    }

    public Collection<UsageReference> getUsageReferences() {
        return new ArrayList<UsageReference>(usageReferences);
    }

    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> result = new HashSet<OWLEntity>();
        for(UsageReference usageReference : usageReferences) {
            final Optional<OWLEntity> axiomSubject = usageReference.getAxiomSubject();
            if (axiomSubject.isPresent()) {
                result.add(axiomSubject.get());
            }
        }
        return result;
    }
}

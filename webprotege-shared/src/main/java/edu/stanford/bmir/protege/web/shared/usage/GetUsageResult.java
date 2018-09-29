package edu.stanford.bmir.protege.web.shared.usage;

import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class GetUsageResult implements Result, HasSignature, HasProjectId {

    private ProjectId projectId;

    private EntityNode entityNode;

    private Collection<UsageReference> usageReferences;

    private int totalUsageCount;

    private GetUsageResult() {
    }

    public GetUsageResult(ProjectId projectId, EntityNode entityNode, Collection<UsageReference> usageReferences, int totalUsageCount) {
        this.projectId = checkNotNull(projectId);
        this.entityNode = checkNotNull(entityNode);
        this.usageReferences = checkNotNull(usageReferences);
        this.totalUsageCount = checkNotNull(totalUsageCount);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public EntityNode getEntityNode() {
        return entityNode;
    }

    public int getTotalUsageCount() {
        return totalUsageCount;
    }

    @Nonnull
    public Collection<UsageReference> getUsageReferences() {
        return new ArrayList<UsageReference>(usageReferences);
    }

    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> result = new HashSet<OWLEntity>();
        for(UsageReference usageReference : usageReferences) {
            final Optional<OWLEntity> axiomSubject = usageReference.getAxiomSubject();
            axiomSubject.ifPresent(result::add);
        }
        return result;
    }
}

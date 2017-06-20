package edu.stanford.bmir.protege.web.shared.renderer;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/02/15
 */
public class GetEntityDataAction implements ProjectAction<GetEntityDataResult> {

    private ProjectId projectId;

    private ImmutableSet<OWLEntity> entities;


    /**
     * For serialization purposes only
     */
    private GetEntityDataAction() {
    }

    public GetEntityDataAction(ProjectId projectId, Set<OWLEntity> entities) {
        this.projectId = checkNotNull(projectId);
        this.entities = ImmutableSet.copyOf(checkNotNull(entities));
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public Set<OWLEntity> getEntities() {
        return entities;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entities);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetEntityDataAction)) {
            return false;
        }
        GetEntityDataAction other = (GetEntityDataAction) obj;
        return this.projectId.equals(other.projectId) && this.entities.equals(other.entities);
    }


    @Override
    public String toString() {
        return toStringHelper("GetEntityDataAction")
                .addValue(projectId)
                .addValue(entities)
                .toString();
    }


}

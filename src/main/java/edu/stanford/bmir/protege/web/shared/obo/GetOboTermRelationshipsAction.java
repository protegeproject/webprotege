package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermRelationshipsAction implements ProjectAction<GetOboTermRelationshipsResult> {

    private ProjectId projectId;

    private OWLClass entity;

    public GetOboTermRelationshipsAction(@Nonnull ProjectId projectId,
                                         @Nonnull OWLClass entity) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
    }

    @GwtSerializationConstructor
    private GetOboTermRelationshipsAction() {
    }

    @Override
    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLClass getEntity() {
        return entity;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermRelationshipsAction)) {
            return false;
        }
        GetOboTermRelationshipsAction other = (GetOboTermRelationshipsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermRelationshipsAction")
                .addValue(projectId)
                .addValue(entity)
                .toString();
    }
}

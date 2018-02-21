package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class SetOboTermRelationshipsAction implements ProjectAction<SetOboTermRelationshipsResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private OBOTermRelationships oboTermRelationships;

    public SetOboTermRelationshipsAction(@Nonnull ProjectId projectId,
                                         @Nonnull OWLEntity entity,
                                         @Nonnull OBOTermRelationships oboTermRelationships) {
        this.projectId = projectId;
        this.entity = entity;
        this.oboTermRelationships = oboTermRelationships;
    }

    @GwtSerializationConstructor
    private SetOboTermRelationshipsAction() {
    }

    @Override
    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public OBOTermRelationships getOboTermRelationships() {
        return oboTermRelationships;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity, oboTermRelationships);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetOboTermRelationshipsAction)) {
            return false;
        }
        SetOboTermRelationshipsAction other = (SetOboTermRelationshipsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity)
                && this.oboTermRelationships.equals(other.oboTermRelationships);
    }


    @Override
    public String toString() {
        return toStringHelper("SetOboTermRelationshipsAction")
                .addValue(projectId)
                .addValue(entity)
                .addValue(oboTermRelationships)
                .toString();
    }
}

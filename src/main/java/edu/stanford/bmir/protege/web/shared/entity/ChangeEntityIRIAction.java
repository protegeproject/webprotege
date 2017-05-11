package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 May 2017
 */
public class ChangeEntityIRIAction implements Action<ChangeEntityIRIResult>, HasProjectId {

    private ProjectId projectId;

    private OWLEntity entity;

    private IRI theNewIri;

    @GwtSerializationConstructor
    private ChangeEntityIRIAction() {
    }

    public ChangeEntityIRIAction(@Nonnull ProjectId projectId,
                                 @Nonnull OWLEntity entity,
                                 @Nonnull IRI theNewIri) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.theNewIri = checkNotNull(theNewIri);
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public IRI getTheNewIri() {
        return theNewIri;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity, theNewIri);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ChangeEntityIRIAction)) {
            return false;
        }
        ChangeEntityIRIAction other = (ChangeEntityIRIAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity)
                && this.theNewIri.equals(other.theNewIri);
    }


    @Override
    public String toString() {
        return toStringHelper("ChangeEntityIRIAction")
                .addValue(projectId)
                .addValue(entity)
                .addValue(theNewIri)
                .toString();
    }
}

package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermCrossProductAction implements ProjectAction<GetOboTermCrossProductResult> {

    private ProjectId projectId;

    private OWLClass entity;

    public GetOboTermCrossProductAction(@Nonnull ProjectId projectId,
                                        @Nonnull OWLClass entity) {
        this.projectId = projectId;
        this.entity = entity;
    }

    @GwtSerializationConstructor
    private GetOboTermCrossProductAction() {
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
        if (!(obj instanceof GetOboTermCrossProductAction)) {
            return false;
        }
        GetOboTermCrossProductAction other = (GetOboTermCrossProductAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermCrossProductAction")
                .addValue(projectId)
                .addValue(entity)
                .toString();
    }
}

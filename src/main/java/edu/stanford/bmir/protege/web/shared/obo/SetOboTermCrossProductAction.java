package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class SetOboTermCrossProductAction implements ProjectAction<SetOboTermCrossProductResult> {

    private ProjectId projectId;

    private OWLClass entity;

    private OBOTermCrossProduct crossProduct;

    public SetOboTermCrossProductAction(@Nonnull ProjectId projectId,
                                        @Nonnull OWLClass entity,
                                        @Nonnull OBOTermCrossProduct crossProduct) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.crossProduct = checkNotNull(crossProduct);
    }

    @GwtSerializationConstructor
    private SetOboTermCrossProductAction() {
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLClass getEntity() {
        return entity;
    }

    @Nonnull
    public OBOTermCrossProduct getCrossProduct() {
        return crossProduct;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity, crossProduct);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetOboTermCrossProductAction)) {
            return false;
        }
        SetOboTermCrossProductAction other = (SetOboTermCrossProductAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity)
                && this.crossProduct.equals(other.crossProduct);
    }


    @Override
    public String toString() {
        return toStringHelper("SetOboTermCrossProductAction")
                .addValue(projectId)
                .addValue(entity)
                .addValue(crossProduct)
                .toString();
    }
}

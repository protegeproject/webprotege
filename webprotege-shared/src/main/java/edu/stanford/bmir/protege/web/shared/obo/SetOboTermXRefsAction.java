package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class SetOboTermXRefsAction implements ProjectAction<SetOboTermXRefsResult> {


    private ProjectId projectId;

    private OWLEntity entity;

    private List<OBOXRef> xrefs;

    public SetOboTermXRefsAction(@Nonnull ProjectId projectId,
                                 @Nonnull OWLEntity entity,
                                 @Nonnull List<OBOXRef> xrefs) {
        this.projectId = projectId;
        this.entity = entity;
        this.xrefs = xrefs;
    }

    @GwtSerializationConstructor
    private SetOboTermXRefsAction() {
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
    public List<OBOXRef> getXrefs() {
        return xrefs;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity, xrefs);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetOboTermXRefsAction)) {
            return false;
        }
        SetOboTermXRefsAction other = (SetOboTermXRefsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity)
                && this.xrefs.equals(other.xrefs);
    }


    @Override
    public String toString() {
        return toStringHelper("SetOboTermXRefsAction")
                .addValue(projectId)
                .addValue(entity)
                .addValue(xrefs)
                .toString();
    }
}

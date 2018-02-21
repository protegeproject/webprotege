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
public class SetOboTermDefinitionAction implements ProjectAction<SetOboTermDefinitionResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private OBOTermDefinition def;

    public SetOboTermDefinitionAction(@Nonnull ProjectId projectId,
                                      @Nonnull OWLEntity entity,
                                      @Nonnull OBOTermDefinition def) {
        this.projectId = projectId;
        this.entity = entity;
        this.def = def;
    }

    @GwtSerializationConstructor
    private SetOboTermDefinitionAction() {
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public OBOTermDefinition getDefinition() {
        return def;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity, def);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetOboTermDefinitionAction)) {
            return false;
        }
        SetOboTermDefinitionAction other = (SetOboTermDefinitionAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity)
                && this.def.equals(other.def);
    }


    @Override
    public String toString() {
        return toStringHelper("SetOboTermDefinitionAction")
                .addValue(projectId)
                .addValue(entity)
                .addValue(def)
                .toString();
    }
}

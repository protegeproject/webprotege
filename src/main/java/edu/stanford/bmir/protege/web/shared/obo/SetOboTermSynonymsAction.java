package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
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
public class SetOboTermSynonymsAction implements ProjectAction<SetOboTermSynonymsResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private ImmutableList<OBOTermSynonym> synonyms;

    public SetOboTermSynonymsAction(@Nonnull ProjectId projectId,
                                    @Nonnull OWLEntity entity,
                                    @Nonnull List<OBOTermSynonym> synonyms) {
        this.projectId = projectId;
        this.entity = entity;
        this.synonyms = ImmutableList.copyOf(synonyms);
    }

    @GwtSerializationConstructor
    private SetOboTermSynonymsAction() {
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
    public List<OBOTermSynonym> getSynonyms() {
        return synonyms;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity, synonyms);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetOboTermSynonymsAction)) {
            return false;
        }
        SetOboTermSynonymsAction other = (SetOboTermSynonymsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.synonyms.equals(other.synonyms)
                && this.entity.equals(other.entity);
    }


    @Override
    public String toString() {
        return toStringHelper("SetOboTermSynonymsAction")
                .addValue(projectId)
                .addValue(entity)
                .addValue(synonyms)
                .toString();
    }
}

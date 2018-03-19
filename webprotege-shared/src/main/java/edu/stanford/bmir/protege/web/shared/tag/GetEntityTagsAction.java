package edu.stanford.bmir.protege.web.shared.tag;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
public class GetEntityTagsAction implements ProjectAction<GetEntityTagsResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    public GetEntityTagsAction(@Nonnull ProjectId projectId,
                               @Nonnull OWLEntity entity) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
    }

    @GwtSerializationConstructor
    private GetEntityTagsAction() {
    }

    /**
     * Gets an action that gets the tags for an entity in a project.
     * @param projectId The project id.
     * @param entity The entity.
     */
    @Nonnull
    public static GetEntityTagsAction getEntityTags(@Nonnull ProjectId projectId,
                                                    @Nonnull OWLEntity entity) {
        return new GetEntityTagsAction(projectId, entity);
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
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
        if (!(obj instanceof GetEntityTagsAction)) {
            return false;
        }
        GetEntityTagsAction other = (GetEntityTagsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity);
    }


    @Override
    public String toString() {
        return toStringHelper("GetEntityTagsAction")
                .addValue(projectId)
                .addValue(entity)
                .toString();
    }

}

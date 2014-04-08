package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/11/2013
 */
public class LookupEntitiesAction implements HasProjectAction<LookupEntitiesResult> {

    private ProjectId projectId;

    private EntityLookupRequest entityLookupRequest;

    @SuppressWarnings("unused")
    private LookupEntitiesAction() {
    }

    /**
     * Creates a LookupEntitiesAction to perform the specified lookup in the specified project.
     * @param projectId The {@link ProjectId} that identifies the project which entities should be looked up in.  Not {@code null}.
     * @param entityLookupRequest The lookup request. Not {@code null}.
     * @throws  NullPointerException if any parameters are {@code null}.
     */
    public LookupEntitiesAction(ProjectId projectId, EntityLookupRequest entityLookupRequest) {
        this.projectId = checkNotNull(projectId);
        this.entityLookupRequest = checkNotNull(entityLookupRequest);
    }

    /**
     * Gets the {@link ProjectId} that identifies the project in which the lookup will be performed.
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the {@link EntityLookupRequest} that describes the lookup to be performed.
     * @return The {@link EntityLookupRequest}. Not {@code null}.
     */
    public EntityLookupRequest getEntityLookupRequest() {
        return entityLookupRequest;
    }


    @Override
    public int hashCode() {
        return "LookupEntitiesAction".hashCode() + projectId.hashCode() + entityLookupRequest.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof LookupEntitiesAction)) {
            return false;
        }
        LookupEntitiesAction other = (LookupEntitiesAction) o;
        return this.projectId.equals(other.projectId) && this.entityLookupRequest.equals(other.entityLookupRequest);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("LookupEntitiesAction")
                .addValue(projectId)
                .addValue(entityLookupRequest).toString();
    }
}

package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/11/2013
 */
public class EntityLookupChoiceRecord implements IsSerializable, HasProjectId, HasUserId {

    private ProjectId projectId;

    private UserId userId;

    private EntityLookupRequest lookupRequest;

    private OWLEntityData entityData;

    /**
     * For serialization purposes only
     */
    private EntityLookupChoiceRecord() {

    }

    public EntityLookupChoiceRecord(ProjectId projectId, UserId userId, EntityLookupRequest lookupRequest, OWLEntityData entityData) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.lookupRequest = checkNotNull(lookupRequest);
        this.entityData = checkNotNull(entityData);
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public UserId getUserId() {
        return userId;
    }

    public EntityLookupRequest getLookupRequest() {
        return lookupRequest;
    }

    public OWLEntityData getEntityData() {
        return entityData;
    }

    @Override
    public int hashCode() {
        return "EntityLookupChoiceRecord".hashCode() + projectId.hashCode() + userId.hashCode() + lookupRequest.hashCode() + entityData.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof EntityLookupChoiceRecord)) {
            return false;
        }
        EntityLookupChoiceRecord other = (EntityLookupChoiceRecord) o;
        return this.projectId.equals(other.projectId) && this.userId.equals(other.userId) && this.lookupRequest.equals(other.lookupRequest) && this.entityData.equals(other.entityData);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("EntityLookupChoiceRecord")
                .addValue(projectId)
                .addValue(userId)
                .addValue(lookupRequest)
                .addValue(entityData).toString();
    }
}

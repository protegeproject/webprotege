package edu.stanford.bmir.protege.web.server.project;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.server.persistence.Indexable;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2016
 */
public class ProjectDetailsConverter implements DocumentConverter<ProjectDetails>, Indexable {

    public static final String PROJECT_ID = "_id";

    public static final String DISPLAY_NAME = "displayName";

    public static final String DESCRIPTION = "description";

    public static final String OWNER = "owner";

    public static final String IN_TRASH = "inTrash";

    @Inject
    public ProjectDetailsConverter() {
    }

    @Override
    public void ensureIndexes(@Nonnull MongoCollection<Document> collection) {
        collection.createIndex(new Document(PROJECT_ID, 1).append(DISPLAY_NAME, 1));
    }

    @Override
    public Document toDocument(@Nonnull ProjectDetails object) {
        Document document = new Document();
        document.append(PROJECT_ID, object.getProjectId().getId());
        document.append(DISPLAY_NAME, object.getDisplayName());
        if (!object.getDescription().isEmpty()) {
            document.append(DESCRIPTION, object.getDescription());
        }
        document.append(OWNER, object.getOwner().getUserName());
        document.append(IN_TRASH, object.isInTrash());
        return document;
    }

    @Override
    public ProjectDetails fromDocument(@Nonnull Document document) {
        ProjectId projectId = ProjectId.get(document.getString(PROJECT_ID));
        String displayName = document.getString(DISPLAY_NAME);
        String description = Optional.ofNullable(document.getString(DESCRIPTION)).orElse("");
        UserId owner = UserId.getUserId(document.getString(OWNER));
        boolean inTrash = document.getBoolean(IN_TRASH, false);
        return new ProjectDetails(projectId, displayName, description, owner, inTrash);
    }

    @Nonnull
    public static Bson withProjectId(@Nonnull  ProjectId projectId) {
        return new Document(PROJECT_ID, projectId.getId());
    }

    @Nonnull
    public static Bson withOwner(@Nonnull UserId userId) {
        return new Document(OWNER, userId.getUserName());
    }

    @Nonnull
    public static Bson withProjectIdAndWithOwner(@Nonnull ProjectId projectId, @Nonnull UserId owner) {
        return new Document(PROJECT_ID, projectId.getId()).append(OWNER, owner.getUserName());
    }

    public static Bson updateInTrash(boolean inTrash) {
        return Updates.set(IN_TRASH, inTrash);
    }
}

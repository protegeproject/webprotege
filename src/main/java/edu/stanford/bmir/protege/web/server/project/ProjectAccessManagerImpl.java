package edu.stanford.bmir.protege.web.server.project;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Date;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Mar 2017
 */
public class ProjectAccessManagerImpl implements ProjectAccessManager {

    public static final String COLLECTION_NAME = "ProjectAccessRecords";

    public static final String PROJECT_ID = "projectId";

    public static final String USER_ID = "userId";

    public static final String ACCESSED = "accessed";

    private final MongoCollection<Document> collection;

    @Inject
    public ProjectAccessManagerImpl(@Nonnull MongoDatabase database) {
        this.collection = database.getCollection(COLLECTION_NAME);
        collection.createIndex(new Document()
                                       .append(PROJECT_ID, 1)
                                       .append(USER_ID, 1));
    }

    @Override
    public void logProjectAccess(ProjectId projectId, UserId userId, long timestamp) {
        collection.updateOne(
                and(eq(PROJECT_ID, projectId.getId()),
                    eq(USER_ID, userId.getUserName())),
                new Document()
                        .append("$inc", new Document("count", 1))
                        .append("$set", new Document(ACCESSED, new Date(timestamp))),
                new UpdateOptions().upsert(true)
        );
    }
}

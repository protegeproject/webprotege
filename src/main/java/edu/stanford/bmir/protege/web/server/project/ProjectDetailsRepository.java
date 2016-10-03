package edu.stanford.bmir.protege.web.server.project;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.server.project.ProjectDetailsConverter.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
public class ProjectDetailsRepository implements Repository {

    public static final String COLLECTION_NAME = "ProjectDetails";

    @Nonnull
    private final ProjectDetailsConverter converter;

    private final MongoCollection<Document> collection;

    @Inject
    public ProjectDetailsRepository(@Nonnull MongoDatabase database, @Nonnull ProjectDetailsConverter converter) {
        this.collection = database.getCollection(COLLECTION_NAME);
        this.converter = converter;
    }

    @Override
    public void ensureIndexes() {
        converter.ensureIndexes(collection);
    }

    public boolean containsProject(@Nonnull ProjectId projectId) {
        return collection
                .find(withProjectId(projectId))
                .projection(new Document())
                .limit(1)
                .first() != null;
    }

    public boolean containsProjectWithOwner(@Nonnull ProjectId projectId, @Nonnull UserId owner) {
        return collection
                .find(withProjectIdAndWithOwner(projectId, owner))
                .projection(new Document())
                .limit(1)
                .first() != null;
    }

    public void setInTrash(ProjectId projectId, boolean inTrash) {
        collection.updateOne(withProjectId(projectId), updateInTrash(inTrash));
    }

    public Optional<ProjectDetails> findOne(@Nonnull ProjectId projectId) {
        return Optional.ofNullable(
                collection
                        .find(withProjectId(projectId))
                        .limit(1)
                        .first())
                .map(d -> converter.fromDocument(d));
    }

    public List<ProjectDetails> findByOwner(UserId owner) {
        ArrayList<ProjectDetails> result = new ArrayList<>();
        collection.find(withOwner(owner))
                .map(d -> converter.fromDocument(d))
                .into(result);
        return result;
    }

    public void save(@Nonnull ProjectDetails projectRecord) {
        Document document = converter.toDocument(projectRecord);
        collection.replaceOne(withProjectId(projectRecord.getProjectId()),
                             document,
                             new UpdateOptions().upsert(true));
    }

    public void delete(@Nonnull ProjectId projectId) {
        collection.deleteOne(withProjectId(projectId));
    }
}

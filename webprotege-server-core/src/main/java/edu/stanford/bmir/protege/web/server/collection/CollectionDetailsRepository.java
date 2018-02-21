package edu.stanford.bmir.protege.web.server.collection;

import edu.stanford.bmir.protege.web.shared.collection.CollectionDetails;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jul 2017
 */
public class CollectionDetailsRepository {

    public Optional<CollectionDetails> findCollectionDetails(ProjectId projectId, CollectionId collectionId) {
        return Optional.empty();
    }

    public List<CollectionDetails> findCollectionDetails(ProjectId projectId) {
        return Collections.emptyList();
    }

    public void removeCollectionDetails(ProjectId projectId, CollectionId collectionId) {

    }

    public void addCollectionDetails(CollectionDetails collectionDetails) {

    }
}

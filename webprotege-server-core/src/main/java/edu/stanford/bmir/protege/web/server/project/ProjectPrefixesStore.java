package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectPrefixes;
import org.mongodb.morphia.Datastore;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.project.ProjectPrefixes.PROJECT_ID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Feb 2018
 */
public class ProjectPrefixesStore {

    @Nonnull
    private final Datastore datastore;


    @Inject
    public ProjectPrefixesStore(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    public void save(@Nonnull ProjectPrefixes projectPrefixes) {
        checkNotNull(projectPrefixes);
        datastore.save(projectPrefixes);
    }

    @Nonnull
    public ProjectPrefixes find(@Nonnull ProjectId projectId) {
        ProjectPrefixes projectPrefixes = datastore.find(ProjectPrefixes.class)
                                                   .field(PROJECT_ID).equal(projectId)
                                                   .get();
        if(projectPrefixes == null) {
            return ProjectPrefixes.get(projectId);
        }
        else {
            return projectPrefixes;
        }
    }
}

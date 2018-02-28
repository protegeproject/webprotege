package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.PrefixDeclarations;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.mongodb.morphia.Datastore;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.project.PrefixDeclarations.PROJECT_ID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Feb 2018
 */
public class PrefixDeclarationsStore {

    @Nonnull
    private final Datastore datastore;


    @Inject
    public PrefixDeclarationsStore(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    public void save(@Nonnull PrefixDeclarations prefixDeclarations) {
        checkNotNull(prefixDeclarations);
        datastore.save(prefixDeclarations);
    }

    @Nonnull
    public PrefixDeclarations find(@Nonnull ProjectId projectId) {
        PrefixDeclarations prefixDeclarations = datastore.find(PrefixDeclarations.class)
                                                         .field(PROJECT_ID).equal(projectId)
                                                         .get();
        if(prefixDeclarations == null) {
            return PrefixDeclarations.get(projectId);
        }
        else {
            return prefixDeclarations;
        }
    }
}

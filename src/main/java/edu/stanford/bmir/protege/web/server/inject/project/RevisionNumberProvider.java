package edu.stanford.bmir.protege.web.server.inject.project;

import com.google.inject.Provider;
import edu.stanford.bmir.protege.web.server.owlapi.change.RevisionManager;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class RevisionNumberProvider implements Provider<RevisionNumber> {

    private final RevisionManager revisionManager;

    @Inject
    public RevisionNumberProvider(RevisionManager revisionManager) {
        this.revisionManager = revisionManager;
    }

    @Override
    public RevisionNumber get() {
        return revisionManager.getCurrentRevision();
    }
}

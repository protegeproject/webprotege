package edu.stanford.bmir.protege.web.server.owlapi.change;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/05/15
 */
public interface RevisionStore {

    /**
     * Gets all of the revisions.
     * @return The revisions in an immutable list.
     */
    ImmutableList<Revision> getRevisions();

    /**
     * Gets the revision that has the specified revision number.
     * @param revisionNumber The revision number.  Not {@code null}.
     * @return The Revision.  If a revision with the specified revision number does not exist
     * then an absent value will be returned.  Not {@code null}.
     */
    Optional<Revision> getRevision(RevisionNumber revisionNumber);

    /**
     * Add the specified revision to this revision store.  The revision must have a number that is beyond the revision
     * number of the current revision otherwise an IllegalArgumentException will be thrown.
     * @param revision The revision to be added.  Not {@code null}.
     */
    void addRevision(Revision revision);

    /**
     * Gets the revision number of the latest revision.
     * @return The revision number of the latest revision.  If there are no revisions then a revision number
     * of zero is returned.
     */
    RevisionNumber getCurrentRevisionNumber();
}

package edu.stanford.bmir.protege.web.server.owlapi.change;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.change.HasGetRevisionSummary;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public interface RevisionManager extends HasGetRevisionSummary {

    /**
     * Gets the revision number of the current revision.  A revision number corresponding to zero indicated that
     * there are no revisions in this manager.
     * @return The current revision number.  Not {@code null}.
     */
    RevisionNumber getCurrentRevision();


    /**
     * Gets the revision that has the specified revision number.
     * @param revisionNumber The revision number. Not {@code null}.
     * @return The Revision that has the specified revision number.  An absent value will be returned if this manager
     * does not contain a revision that has the specified revision number.
     */
    Optional<Revision> getRevision(RevisionNumber revisionNumber);

    /**
     * Gets a revision summary of the specified revision.
     * @param revisionNumber The revision number for which to retrieve a summary.
     * @return A revision summary of the specified revision.  An absent value will be returned if there is no
     * revision with the specified revision number.
     */
    @Override
    Optional<RevisionSummary> getRevisionSummary(RevisionNumber revisionNumber);


    /**
     * Gets a list of revisions held by this manager.
     * @return The list of revisions.  Modifications to the returned list will not affect the revisions contained
     * within this manager.
     */
    List<Revision> getRevisions();


    OWLOntologyManager getOntologyManagerForRevision(RevisionNumber revision);

    List<RevisionSummary> getRevisionSummaries();


    /**
     * Adds a new revision that is based on the specified details.  If the list of changes is empty then no revision
     * will be added.
     * @param userId The user id of the user who generated the revision. Not {@code null}.
     * @param changes The changes contained in the revision.  Not {@code null}.
     * @param desc A description of the revision.  Not {@code null}.
     */
    Revision addRevision(UserId userId, List<? extends OWLOntologyChangeRecord> changes, String desc);

}

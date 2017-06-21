package edu.stanford.bmir.protege.web.server.revision;

import edu.stanford.bmir.protege.web.server.change.HasGetRevisionSummary;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
@ProjectSingleton
public interface RevisionManager extends HasGetRevisionSummary {

    /**
     * Gets the revision number of the current revision.  A revision number corresponding to zero indicated that
     * there are no revisions in this manager.
     * @return The current revision number.  Not {@code null}.
     */
    @Nonnull
    RevisionNumber getCurrentRevision();


    /**
     * Gets the revision that has the specified revision number.
     * @param revisionNumber The revision number. Not {@code null}.
     * @return The Revision that has the specified revision number.  An absent value will be returned if this manager
     * does not contain a revision that has the specified revision number.
     */
    @Nonnull
    Optional<Revision> getRevision(@Nonnull RevisionNumber revisionNumber);

    /**
     * Gets a revision summary of the specified revision.
     * @param revisionNumber The revision number for which to retrieve a summary.
     * @return A revision summary of the specified revision.  An absent value will be returned if there is no
     * revision with the specified revision number.
     */
    @Nonnull
    @Override
    Optional<RevisionSummary> getRevisionSummary(@Nonnull RevisionNumber revisionNumber);


    /**
     * Gets a list of revisions held by this manager.
     * @return The list of revisions.  Modifications to the returned list will not affect the revisions contained
     * within this manager.
     */
    @Nonnull
    List<Revision> getRevisions();

    @Nonnull
    OWLOntologyManager getOntologyManagerForRevision(@Nonnull RevisionNumber revision);

    @Nonnull
    List<RevisionSummary> getRevisionSummaries();


    /**
     * Adds a new revision that is based on the specified details.  If the list of changes is empty then no revision
     * will be added.
     * @param userId The user id of the user who generated the revision. Not {@code null}.
     * @param changes The changes contained in the revision.  Not {@code null}.
     * @param desc A description of the revision.  Not {@code null}.
     */
    @Nonnull
    Revision addRevision(@Nonnull UserId userId,
                         @Nonnull List<? extends OWLOntologyChangeRecord> changes,
                         @Nonnull String desc);

}

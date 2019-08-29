package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.axiom.AxiomSubjectProvider;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeVisitorEx;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 */
public class RevisionDetailsExtractor {

    private final AxiomSubjectProvider axiomSubjectProvider;

    @Inject
    public RevisionDetailsExtractor(AxiomSubjectProvider axiomSubjectProvider) {
        this.axiomSubjectProvider = axiomSubjectProvider;
    }

    public RevisionDetails extractRevisionDetails(Revision revision) {
        ImmutableList.Builder<RevisionDetails.ChangeDetails> builder = ImmutableList.builder();
        ImmutableSet.Builder<IRI> subjectsBuilder = ImmutableSet.builder();
        revision.getChanges().stream()
                .filter(OntologyChange::isAxiomChange)
                .peek(chg -> {
                    axiomSubjectProvider.getSubject(chg.getAxiomOrThrow())
                    .ifPresent(subject -> {
                        if(subject instanceof IRI) {
                            subjectsBuilder.add((IRI) subject);
                        }
                        else if(subject instanceof HasIRI) {
                            subjectsBuilder.add(((HasIRI) subject).getIRI());
                        }
                    });
                })
                .map(RevisionDetailsExtractor::toChangeDetails)
                .filter(Objects::nonNull)
                .forEach(builder::add);

        return new RevisionDetails(revision.getRevisionNumber().getValueAsInt(),
                                   revision.getTimestamp(),
                                   revision.getUserId(),
                                   revision.getHighLevelDescription(),
                                   subjectsBuilder.build(),
                                   builder.build());
    }


    private static RevisionDetails.ChangeDetails toChangeDetails(@Nonnull OntologyChange change) {
        return change.accept(MAPPING_VISITOR);
    }

    private static final OntologyChangeVisitorEx<RevisionDetails.ChangeDetails> MAPPING_VISITOR = new OntologyChangeVisitorEx<>() {

        @Override
        public RevisionDetails.ChangeDetails getDefaultReturnValue() {
            return null;
        }

        @Nonnull
        @Override
        public RevisionDetails.ChangeDetails visit(@Nonnull AddAxiomChange change) {
            return new RevisionDetails.ChangeDetails(RevisionDetails.ChangeOperation.ADD, change.getAxiom());
        }

        @Override
        public RevisionDetails.ChangeDetails visit(@Nonnull RemoveAxiomChange change) {
            return new RevisionDetails.ChangeDetails(RevisionDetails.ChangeOperation.REMOVE, change.getAxiom());
        }
    };

}

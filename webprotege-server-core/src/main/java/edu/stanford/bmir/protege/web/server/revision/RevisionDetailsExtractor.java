package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.axiom.AxiomSubjectProvider;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;

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
                .filter(chg -> chg.getData() instanceof AxiomChangeData)
                .peek(chg -> {
                    axiomSubjectProvider.getSubject(((AxiomChangeData) chg.getData()).getAxiom())
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


    private static RevisionDetails.ChangeDetails toChangeDetails(@Nonnull OWLOntologyChangeRecord record) {
        return record.getData().accept(MAPPING_VISITOR);
    }

    private static final OWLOntologyChangeDataVisitor<RevisionDetails.ChangeDetails, RuntimeException> MAPPING_VISITOR = new OWLOntologyChangeDataVisitor<RevisionDetails.ChangeDetails, RuntimeException>() {
        @Nonnull
        @Override
        public RevisionDetails.ChangeDetails visit(AddAxiomData data) {
            return new RevisionDetails.ChangeDetails(RevisionDetails.ChangeOperation.ADD, data.getAxiom());
        }

        @Override
        public RevisionDetails.ChangeDetails visit(RemoveAxiomData data) {
            return new RevisionDetails.ChangeDetails(RevisionDetails.ChangeOperation.REMOVE, data.getAxiom());
        }

        @Override
        public RevisionDetails.ChangeDetails visit(AddOntologyAnnotationData data) {
            return null;
        }

        @Override
        public RevisionDetails.ChangeDetails visit(RemoveOntologyAnnotationData data) {
            return null;
        }

        @Override
        public RevisionDetails.ChangeDetails visit(SetOntologyIDData data) {
            return null;
        }

        @Override
        public RevisionDetails.ChangeDetails visit(AddImportData data) {
            return null;
        }

        @Override
        public RevisionDetails.ChangeDetails visit(RemoveImportData data) {
            return null;
        }
    };

}

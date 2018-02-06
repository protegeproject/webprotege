package edu.stanford.bmir.protege.web.server.diff;

import edu.stanford.bmir.protege.web.server.axiom.AxiomIRISubjectProvider;
import edu.stanford.bmir.protege.web.shared.Filter;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class SameSubjectFilter implements Filter<OWLOntologyChangeRecord> {

    private final AxiomIRISubjectProvider provider;

    private final Optional<IRI> subject;

    public SameSubjectFilter(AxiomIRISubjectProvider provider, Optional<IRI> subject) {
        this.provider = provider;
        this.subject = subject;
    }

    @Override
    public boolean isIncluded(final OWLOntologyChangeRecord object) {
        return object.getData().accept(new OWLOntologyChangeDataVisitor<Boolean, RuntimeException>() {
            @Nonnull
            @Override
            public Boolean visit(AddAxiomData data) throws RuntimeException {
                return provider.getSubject(data.getAxiom()).equals(subject);
            }

            @Override
            public Boolean visit(RemoveAxiomData data) throws RuntimeException {
                return provider.getSubject(data.getAxiom()).equals(subject);
            }

            @Override
            public Boolean visit(AddOntologyAnnotationData data) throws RuntimeException {
                OWLOntologyID ontologyID = object.getOntologyID();
                return isSameAsSubject(ontologyID);
            }

            @Override
            public Boolean visit(RemoveOntologyAnnotationData data) throws RuntimeException {
                OWLOntologyID ontologyID = object.getOntologyID();
                return isSameAsSubject(ontologyID);
            }

            @Override
            public Boolean visit(SetOntologyIDData data) throws RuntimeException {
                OWLOntologyID ontologyID = object.getOntologyID();
                return isSameAsSubject(ontologyID);
            }

            @Override
            public Boolean visit(AddImportData data) throws RuntimeException {
                OWLOntologyID ontologyID = object.getOntologyID();
                return isSameAsSubject(ontologyID);
            }

            @Override
            public Boolean visit(RemoveImportData data) throws RuntimeException {
                OWLOntologyID ontologyID = object.getOntologyID();
                return isSameAsSubject(ontologyID);
            }
        });
    }

    private Boolean isSameAsSubject(OWLOntologyID ontologyID) {
        if(ontologyID.isAnonymous()) {
            return false;
        }
        IRI ontologyIRI = ontologyID.getOntologyIRI().get();
        return !ontologyID.isAnonymous() && Optional.of(ontologyIRI).equals(subject);
    }
}

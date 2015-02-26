package edu.stanford.bmir.protege.web.server.render;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
public class DeprecatedEntityCheckerImpl implements DeprecatedEntityChecker {

    private OWLOntology rootOntology;


    public DeprecatedEntityCheckerImpl(OWLOntology rootOntology) {
        this.rootOntology = checkNotNull(rootOntology);
    }

    @Override
    public boolean isDeprecated(OWLEntity entity) {
        if (!rootOntology.containsAnnotationPropertyInSignature(OWLRDFVocabulary.OWL_DEPRECATED.getIRI(), true)) {
            return false;
        }
        // TODO: Cache
        for (OWLOntology ont : rootOntology.getImportsClosure()) {
            for (OWLAnnotationAssertionAxiom ax : ont.getAnnotationAssertionAxioms(entity.getIRI())) {
                if (ax.isDeprecatedIRIAssertion()) {
                    return true;
                }
            }
        }
        return false;
    }
}

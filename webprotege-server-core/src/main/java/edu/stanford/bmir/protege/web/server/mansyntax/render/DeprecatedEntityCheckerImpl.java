package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
@ProjectSingleton
public class DeprecatedEntityCheckerImpl implements DeprecatedEntityChecker {

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public DeprecatedEntityCheckerImpl(@RootOntology OWLOntology rootOntology) {
        this.rootOntology = checkNotNull(rootOntology);
    }

    @Override
    public boolean isDeprecated(OWLEntity entity) {
        if (!rootOntology.containsAnnotationPropertyInSignature(OWLRDFVocabulary.OWL_DEPRECATED.getIRI(), Imports.INCLUDED)) {
            return false;
        }
        var entityIRI = entity.getIRI();
        return rootOntology.getImportsClosure()
                .stream()
                .flatMap(ont -> ont.getAnnotationAssertionAxioms(entityIRI).stream())
                .anyMatch(OWLAnnotationAssertionAxiom::isDeprecatedIRIAssertion);
    }
}

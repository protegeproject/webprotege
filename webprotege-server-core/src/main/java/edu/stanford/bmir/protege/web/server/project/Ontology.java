package edu.stanford.bmir.protege.web.server.project;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 *
 * A lightweight ontology object that contains an id, annotations and axiom.
 */
@AutoValue
public abstract class Ontology {

    public static Ontology get(@Nonnull OntologyDocumentId ontologyDocumentId,
                               @Nonnull OWLOntologyID ontologyID,
                               @Nonnull Set<OWLImportsDeclaration> importsDeclarations,
                               @Nonnull Set<OWLAnnotation> ontologyAnnotations,
                               @Nonnull Set<OWLAxiom> ontologyAxioms) {
        return new AutoValue_Ontology(ontologyDocumentId,
                                      ontologyID,
                                      ImmutableSet.copyOf(importsDeclarations),
                                      ImmutableSet.copyOf(ontologyAnnotations),
                                      ImmutableSet.copyOf(ontologyAxioms));
    }

    @Nonnull
    public abstract OntologyDocumentId getOntologyDocumentId();

    @Nonnull
    public abstract OWLOntologyID getOntologyID();

    @Nonnull
    public abstract ImmutableSet<OWLImportsDeclaration> getImportsDeclarations();

    @Nonnull
    public abstract ImmutableSet<OWLAnnotation> getAnnotations();

    @Nonnull
    public abstract ImmutableSet<OWLAxiom> getAxioms();
}

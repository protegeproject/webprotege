package edu.stanford.bmir.protege.web.server.util;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.index.AxiomsByReferenceIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsIndex;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
public class ReferenceFinder {

    @Nonnull
    private final AxiomsByReferenceIndex axiomsIndex;

    @Nonnull
    private final OntologyAnnotationsIndex ontologyAnnotationsIndex;


    @Inject
    public ReferenceFinder(@Nonnull AxiomsByReferenceIndex axiomsIndex,
                           @Nonnull OntologyAnnotationsIndex ontologyAnnotationsIndex) {
        this.axiomsIndex = checkNotNull(axiomsIndex);
        this.ontologyAnnotationsIndex = checkNotNull(ontologyAnnotationsIndex);
    }

    /**
     * Gets the references set for the specified entities in the specified ontology.
     *
     * @param entities   The entities whose references are to be retrieved. Not {@code null}.
     * @param ontologyId The ontology.  Not {@code null}.
     * @return The ReferenceSet that contains axioms that reference the specified entities and ontology annotations
     * that reference the specified entities.  Note that, since annotation assertions have subjects that may be IRIs and
     * values that may be IRIs, and ontology annotation have values that may be IRIs, the reference set includes these
     * axioms where the IRI is the IRI of one or more of the specified entities.
     */
    public ReferenceSet getReferenceSet(@Nonnull Collection<OWLEntity> entities,
                                        @Nonnull OWLOntologyID ontologyId) {

        checkNotNull(entities);
        checkNotNull(ontologyId);

        ImmutableSet.Builder<OWLAxiom> axiomSetBuilder = ImmutableSet.builder();

        ImmutableSet.Builder<OWLAnnotation> ontologyAnnotationSetBuilder = ImmutableSet.builder();


        final Set<IRI> entityIRIs = new HashSet<>(entities.size());
        axiomsIndex.getReferencingAxioms(entities, ontologyId)
                .forEach(axiomSetBuilder::add);
        entities.stream().map(OWLNamedObject::getIRI).forEach(entityIRIs::add);

        ontologyAnnotationsIndex.getOntologyAnnotations(ontologyId)
                .forEach(annotation -> {
                    processAnnotation(entities, ontologyAnnotationSetBuilder, entityIRIs, annotation, annotation);
                });
        return new ReferenceSet(ontologyId, axiomSetBuilder.build(), ontologyAnnotationSetBuilder.build());

    }

    private void processAnnotation(@Nonnull Collection<OWLEntity> entities,
                                   ImmutableSet.Builder<OWLAnnotation> ontologyAnnotationSetBuilder,
                                   Set<IRI> entityIRIs,
                                   OWLAnnotation rootAnnotation,
                                   OWLAnnotation annotation) {
        var value = annotation.getValue();
        if(value instanceof IRI && entityIRIs.contains(value)) {
            ontologyAnnotationSetBuilder.add(rootAnnotation);
        }
        else {
            if(entities.contains(annotation.getProperty())) {
                ontologyAnnotationSetBuilder.add(rootAnnotation);
            }
        }
        annotation.getAnnotations().forEach(annoAnno -> processAnnotation(entities,
                                                                          ontologyAnnotationSetBuilder,
                                                                          entityIRIs,
                                                                          rootAnnotation,
                                                                          annoAnno));
    }


    public static class ReferenceSet {

        private final OWLOntologyID ontologyId;

        private final ImmutableCollection<OWLAxiom> referencingAxioms;

        private final ImmutableCollection<OWLAnnotation> referencingOntologyAnnotations;

        public ReferenceSet(OWLOntologyID ontologyId,
                            ImmutableCollection<OWLAxiom> referencingAxioms,
                            ImmutableCollection<OWLAnnotation> referencingOntologyAnnotations) {
            this.ontologyId = checkNotNull(ontologyId);
            this.referencingAxioms = checkNotNull(referencingAxioms);
            this.referencingOntologyAnnotations = checkNotNull(referencingOntologyAnnotations);
        }

        public OWLOntologyID getOntologyId() {
            return ontologyId;
        }

        public ImmutableCollection<OWLAxiom> getReferencingAxioms() {
            return referencingAxioms;
        }

        public ImmutableCollection<OWLAnnotation> getReferencingOntologyAnnotations() {
            return referencingOntologyAnnotations;
        }
    }
}

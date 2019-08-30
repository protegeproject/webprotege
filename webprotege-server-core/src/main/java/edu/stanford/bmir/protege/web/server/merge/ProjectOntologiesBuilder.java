package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.project.Ontology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
public class ProjectOntologiesBuilder {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final OntologyAnnotationsIndex annotationsIndex;

    @Nonnull
    private final OntologyAxiomsIndex axiomsIndex;

    @Inject
    public ProjectOntologiesBuilder(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                    @Nonnull OntologyAnnotationsIndex annotationsIndex,
                                    @Nonnull OntologyAxiomsIndex axiomsIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.annotationsIndex = checkNotNull(annotationsIndex);
        this.axiomsIndex = checkNotNull(axiomsIndex);
    }

    public Collection<Ontology> buildProjectOntologies() {
        return projectOntologiesIndex.getOntologyIds()
                                     .map(this::toOntology)
                                     .collect(Collectors.toList());
    }

    /**
     * Create an Ontology wrapper for the specified ontology Id
     *
     * @param ontId The ontology Id
     */
    private Ontology toOntology(@Nonnull OWLOntologyID ontId) {
        var annotations = annotationsIndex.getOntologyAnnotations(ontId)
                                          .collect(toImmutableSet());
        var axioms = axiomsIndex.getAxioms(ontId)
                                .collect(toImmutableSet());
        return Ontology.get(ontId,
                            ImmutableSet.of(),
                            annotations, axioms);
    }
}

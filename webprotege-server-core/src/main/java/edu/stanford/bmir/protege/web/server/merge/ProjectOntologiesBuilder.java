package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyIdIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.project.Ontology;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;

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

    @Nonnull
    private final OntologyIdIndex ontologyIdIndex;

    @Inject
    public ProjectOntologiesBuilder(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                    @Nonnull OntologyAnnotationsIndex annotationsIndex,
                                    @Nonnull OntologyAxiomsIndex axiomsIndex,
                                    @Nonnull OntologyIdIndex ontologyIdIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.annotationsIndex = checkNotNull(annotationsIndex);
        this.axiomsIndex = checkNotNull(axiomsIndex);
        this.ontologyIdIndex = checkNotNull(ontologyIdIndex);
    }

    public Collection<Ontology> buildProjectOntologies() {
        return projectOntologiesIndex.getOntologyDocumentIds()
                                     .map(this::toOntology)
                                     .collect(Collectors.toList());
    }

    /**
     * Create an Ontology wrapper for the specified ontology Id
     *
     * @param ontId The ontology Id
     */
    private Ontology toOntology(@Nonnull OntologyDocumentId ontId) {
        var annotations = annotationsIndex.getOntologyAnnotations(ontId)
                                          .collect(toImmutableSet());
        var axioms = axiomsIndex.getAxioms(ontId)
                                .collect(toImmutableSet());
        var ontologyId = ontologyIdIndex.getOntologyId(ontId);
        return Ontology.get(ontId,
                            ontologyId,
                            ImmutableSet.of(),
                            annotations, axioms);
    }
}

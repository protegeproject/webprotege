package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-17
 */
public class SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl implements SubAnnotationPropertyAxiomsBySuperPropertyIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLSubAnnotationPropertyOfAxiom> getAxiomsForSuperProperty(@Nonnull OWLAnnotationProperty property,
                                                                             @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        checkNotNull(property);
        return ontologyIndex.getOntology(ontologyId)
                .stream()
                .flatMap(ont -> ont.getAxioms(AxiomType.SUB_ANNOTATION_PROPERTY_OF).stream())
                .filter(ax -> ax.getSuperProperty().equals(property));
    }
}

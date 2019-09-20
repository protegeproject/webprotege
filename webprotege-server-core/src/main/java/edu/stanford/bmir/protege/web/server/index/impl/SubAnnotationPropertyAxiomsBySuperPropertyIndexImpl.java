package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.SubAnnotationPropertyAxiomsBySuperPropertyIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
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
@ProjectSingleton
public class SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl implements SubAnnotationPropertyAxiomsBySuperPropertyIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLAnnotationProperty, OWLSubAnnotationPropertyOfAxiom> index;

    @Inject
    public SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl() {
        index = AxiomMultimapIndex.create(OWLSubAnnotationPropertyOfAxiom.class,
                                          OWLSubAnnotationPropertyOfAxiom::getSuperProperty);
    }

    @Nonnull
    @Override
    public Stream<OWLSubAnnotationPropertyOfAxiom> getAxiomsForSuperProperty(@Nonnull OWLAnnotationProperty property,
                                                                             @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        checkNotNull(property);
        return index.getAxioms(property, ontologyId);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}

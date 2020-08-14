package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsByValueIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-13
 */
@ProjectSingleton
public class AnnotationAssertionAxiomsByValueIndexImpl implements AnnotationAssertionAxiomsByValueIndex, UpdatableIndex {

    private final AxiomMultimapIndex<OWLAnnotationValue, OWLAnnotationAssertionAxiom> index;

    @Inject
    public AnnotationAssertionAxiomsByValueIndexImpl() {
        index = AxiomMultimapIndex.create(OWLAnnotationAssertionAxiom.class,
                                          AnnotationAssertionAxiomsByValueIndexImpl::extractAnnotationValue);
    }

    @Nullable
    private static OWLAnnotationValue extractAnnotationValue(@Nonnull OWLAnnotationAssertionAxiom axiom) {
        var value = axiom.getValue();
        if(value instanceof OWLLiteral) {
            return null;
        }
        else {
            return value;
        }
    }

    @Nonnull
    @Override
    public Stream<OWLAnnotationAssertionAxiom> getAxiomsByValue(@Nonnull OWLAnnotationValue value,
                                                                @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(value);
        checkNotNull(ontologyId);
        return index.getAxioms(value, ontologyId);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}

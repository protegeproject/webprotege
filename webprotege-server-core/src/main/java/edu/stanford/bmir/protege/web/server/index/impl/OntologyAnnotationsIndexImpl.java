package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsSignatureIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-05
 */
@ProjectSingleton
public class OntologyAnnotationsIndexImpl implements OntologyAnnotationsSignatureIndex, OntologyAnnotationsIndex, UpdatableIndex {

    private Multimap<OWLOntologyID, OWLAnnotation> annotationsMap = ArrayListMultimap.create();

    @Inject
    public OntologyAnnotationsIndexImpl() {
    }

    @Nonnull
    @Override
    public Stream<OWLAnnotation> getOntologyAnnotations(@Nonnull OWLOntologyID ontologyID) {
        return ImmutableList.copyOf(annotationsMap.get(ontologyID)).stream();
    }

    @Nonnull
    @Override
    public Stream<OWLAnnotationProperty> getOntologyAnnotationsSignature(@Nonnull OWLOntologyID ontologyId) {
        var resultBuilder = ImmutableList.<OWLAnnotationProperty>builder();
        annotationsMap.get(ontologyId)
                      .forEach(anno -> collectAnnotationProperties(anno, resultBuilder));
        return resultBuilder.build().stream().distinct();
    }

    private void collectAnnotationProperties(OWLAnnotation anno,
                                             ImmutableCollection.Builder<OWLAnnotationProperty> resultBuilder) {
        resultBuilder.add(anno.getProperty());
        anno.getAnnotations().forEach(a -> collectAnnotationProperties(a, resultBuilder));
    }

    @Override
    public boolean containsEntityInOntologyAnnotationsSignature(@Nonnull OWLEntity entity, @Nonnull OWLOntologyID ontologyId) {
        if(!entity.isOWLAnnotationProperty()) {
            return false;
        }
        return contains(entity.asOWLAnnotationProperty(),
                        ontologyId);
    }

    private boolean contains(@Nonnull OWLAnnotationProperty property,
                             @Nonnull OWLOntologyID ontologyId) {
        return annotationsMap.get(ontologyId)
                      .stream()
                      .anyMatch(anno -> contains(property, anno));
    }

    private boolean contains(@Nonnull OWLAnnotationProperty property,
                             @Nonnull OWLAnnotation annotation) {
        if(annotation.getProperty()
                     .equals(property)) {
            return true;
        }
        return annotation.getAnnotations()
                         .stream()
                         .anyMatch(anno -> contains(property, anno));
    }

    @Override
    public boolean containsAnnotation(@Nonnull OWLAnnotation annotation, @Nonnull OWLOntologyID ontologyId) {
        return annotationsMap.containsEntry(ontologyId, annotation);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        changes.forEach(change -> change.accept(new OntologyChangeVisitor() {
            @Override
            public void visit(@Nonnull AddOntologyAnnotationChange addOntologyAnnotationChange) {
                handleOntologyAnnotationChange(addOntologyAnnotationChange);
            }

            @Override
            public void visit(@Nonnull RemoveOntologyAnnotationChange removeOntologyAnnotationChange) {
                handleOntologyAnnotationChange(removeOntologyAnnotationChange);
            }
        }));
    }

    private void handleOntologyAnnotationChange(@Nonnull OntologyAnnotationChange change) {
        if(change.isAddOntologyAnnotation()) {
            annotationsMap.put(change.getOntologyId(), change.getAnnotation());
        }
        else {
            annotationsMap.remove(change.getOntologyId(), change.getAnnotation());
        }
    }
}

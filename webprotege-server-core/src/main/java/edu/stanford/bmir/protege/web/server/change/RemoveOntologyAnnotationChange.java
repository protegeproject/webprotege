package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.value.AutoValue;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.change.RemoveOntologyAnnotationData;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-26
 */
@AutoValue
public abstract class RemoveOntologyAnnotationChange implements OntologyAnnotationChange {

    public static RemoveOntologyAnnotationChange of(@Nonnull OWLOntologyID ontologyId,
                                                    @Nonnull OWLAnnotation annotation) {
        return new AutoValue_RemoveOntologyAnnotationChange(ontologyId, annotation);
    }

    @Nonnull
    @Override
    public RemoveOntologyAnnotationChange replaceIris(@Nonnull OWLObjectDuplicator duplicator) {
        OWLAnnotation duplicatedAnnotation = duplicator.duplicateObject(getAnnotation());
        return RemoveOntologyAnnotationChange.of(getOntologyId(),
                                                 duplicatedAnnotation);
    }

    @Nonnull
    @Override
    public OWLOntologyChangeRecord toOwlOntologyChangeRecord() {
        return new OWLOntologyChangeRecord(getOntologyId(), new RemoveOntologyAnnotationData(getAnnotation()));
    }

    @Override
    public boolean isRemoveOntologyAnnotation() {
        return true;
    }

    @Override
    public void accept(@Nonnull OntologyChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R> R accept(@Nonnull OntologyChangeVisitorEx<R> visitorEx) {
        return visitorEx.visit(this);
    }
}

package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.util.IriReplacer;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.change.RemoveOntologyAnnotationData;
import org.semanticweb.owlapi.model.OWLAnnotation;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-26
 */
@AutoValue
public abstract class RemoveOntologyAnnotationChange implements OntologyAnnotationChange {

    public static RemoveOntologyAnnotationChange of(@Nonnull OntologyDocumentId ontologyId,
                                                    @Nonnull OWLAnnotation annotation) {
        return new AutoValue_RemoveOntologyAnnotationChange(ontologyId, annotation);
    }

    @Nonnull
    @Override
    public RemoveOntologyAnnotationChange replaceIris(@Nonnull IriReplacer iriReplacer) {
        OWLAnnotation duplicatedAnnotation = iriReplacer.replaceIris(getAnnotation());
        return RemoveOntologyAnnotationChange.of(getOntologyDocumentId(),
                                                 duplicatedAnnotation);
    }

    @Nonnull
    @Override
    public RemoveOntologyAnnotationChange replaceOntologyId(@Nonnull OntologyDocumentId ontologyId) {
        if(getOntologyDocumentId().equals(ontologyId)) {
            return this;
        }
        else {
            return RemoveOntologyAnnotationChange.of(ontologyId, getAnnotation());
        }
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

    @Nonnull
    @Override
    public AddOntologyAnnotationChange getInverseChange() {
        return AddOntologyAnnotationChange.of(getOntologyDocumentId(), getAnnotation());
    }
}

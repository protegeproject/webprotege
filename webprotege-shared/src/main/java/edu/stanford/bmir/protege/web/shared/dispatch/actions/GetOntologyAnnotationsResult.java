package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetOntologyAnnotationsResult implements Result {

    private ImmutableListMultimap<OntologyDocumentId, PropertyAnnotationValue> annotations;


    @GwtSerializationConstructor
    private GetOntologyAnnotationsResult() {
    }

    public GetOntologyAnnotationsResult(ImmutableListMultimap<OntologyDocumentId, PropertyAnnotationValue> annotations) {
        this.annotations = checkNotNull(annotations);
    }

    @Nonnull
    public ImmutableListMultimap<OntologyDocumentId, PropertyAnnotationValue> getAnnotations() {
        return annotations;
    }
}

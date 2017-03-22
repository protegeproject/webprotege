package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetOntologyAnnotationsResult implements Result {

    private ImmutableList<PropertyAnnotationValue> annotations;


    @GwtSerializationConstructor
    private GetOntologyAnnotationsResult() {
    }

    public GetOntologyAnnotationsResult(ImmutableList<PropertyAnnotationValue> annotations) {
        this.annotations = checkNotNull(annotations);
    }

    @Nonnull
    public ImmutableList<PropertyAnnotationValue> getAnnotations() {
        return annotations;
    }
}

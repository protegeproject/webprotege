package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.form.data.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class OWLEntity2FormControlDataVisitor implements OWLEntityVisitorEx<PrimitiveFormControlData> {

    @Inject
    public OWLEntity2FormControlDataVisitor() {
    }

    @Nonnull
    @Override
    public PrimitiveFormControlData visit(@Nonnull OWLClass data) {
        return EntityFormControlData.get(data);
    }

    @Nonnull
    @Override
    public PrimitiveFormControlData visit(@Nonnull OWLObjectProperty data) {
        return EntityFormControlData.get(data);
    }

    @Nonnull
    @Override
    public PrimitiveFormControlData visit(@Nonnull OWLDataProperty data) {
        return EntityFormControlData.get(data);
    }

    @Nonnull
    @Override
    public PrimitiveFormControlData visit(@Nonnull OWLAnnotationProperty data) {
        return EntityFormControlData.get(data);
    }

    @Nonnull
    @Override
    public PrimitiveFormControlData visit(@Nonnull OWLNamedIndividual data) {
        return EntityFormControlData.get(data);
    }

    @Nonnull
    @Override
    public PrimitiveFormControlData visit(@Nonnull OWLDatatype data) {
        return EntityFormControlData.get(data);
    }
}

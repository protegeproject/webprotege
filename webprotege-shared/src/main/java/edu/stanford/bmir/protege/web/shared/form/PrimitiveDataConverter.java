package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.form.data.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class PrimitiveDataConverter implements OWLPrimitiveDataVisitor<PrimitiveFormControlData, RuntimeException> {

    @Override
    public PrimitiveFormControlData visit(OWLClassData data) {
        return EntityFormControlData.get(data.getEntity());
    }

    @Override
    public PrimitiveFormControlData visit(OWLObjectPropertyData data) {
        return EntityFormControlData.get(data.getEntity());
    }

    @Override
    public PrimitiveFormControlData visit(OWLDataPropertyData data) {
        return EntityFormControlData.get(data.getEntity());
    }

    @Override
    public PrimitiveFormControlData visit(OWLAnnotationPropertyData data) {
        return EntityFormControlData.get(data.getEntity());
    }

    @Override
    public PrimitiveFormControlData visit(OWLNamedIndividualData data) {
        return EntityFormControlData.get(data.getEntity());
    }

    @Override
    public PrimitiveFormControlData visit(OWLDatatypeData data) {
        return EntityFormControlData.get(data.getEntity());
    }

    @Override
    public PrimitiveFormControlData visit(OWLLiteralData data) {
        return LiteralFormControlValue.get(data.getLiteral());
    }

    @Override
    public PrimitiveFormControlData visit(IRIData data) {
        return IriFormControlValue.get(data.getObject());
    }
}

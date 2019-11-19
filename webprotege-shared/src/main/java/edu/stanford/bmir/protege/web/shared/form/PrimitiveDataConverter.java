package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class PrimitiveDataConverter implements OWLPrimitiveDataVisitor<FormDataValue, RuntimeException> {

    @Override
    public FormDataValue visit(OWLClassData data) {
        return FormDataPrimitive.get(data.getEntity());
    }

    @Override
    public FormDataValue visit(OWLObjectPropertyData data) {
        return FormDataPrimitive.get(data.getEntity());
    }

    @Override
    public FormDataValue visit(OWLDataPropertyData data) {
        return FormDataPrimitive.get(data.getEntity());
    }

    @Override
    public FormDataValue visit(OWLAnnotationPropertyData data) {
        return FormDataPrimitive.get(data.getEntity());
    }

    @Override
    public FormDataValue visit(OWLNamedIndividualData data) {
        return FormDataPrimitive.get(data.getEntity());
    }

    @Override
    public FormDataValue visit(OWLDatatypeData data) {
        return FormDataPrimitive.get(data.getEntity());
    }

    @Override
    public FormDataValue visit(OWLLiteralData data) {
        return FormDataPrimitive.get(data.getLiteral());
    }

    @Override
    public FormDataValue visit(IRIData data) {
        return FormDataPrimitive.get(data.getObject());
    }
}

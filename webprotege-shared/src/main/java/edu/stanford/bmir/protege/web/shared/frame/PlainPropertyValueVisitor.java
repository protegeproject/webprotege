package edu.stanford.bmir.protege.web.shared.frame;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
public interface PlainPropertyValueVisitor<R> {

    R visit(PlainPropertyClassValue propertyValue);

    R visit(PlainPropertyAnnotationValue propertyValue);

    R visit(PlainPropertyDatatypeValue propertyValue);

    R visit(PlainPropertyIndividualValue propertyValue);

    R visit(PlainPropertyLiteralValue propertyValue);
}

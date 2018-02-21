package edu.stanford.bmir.protege.web.shared.frame;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public interface PropertyValueVisitor<R, E extends Throwable> {

    R visit(PropertyClassValue propertyValue) throws E;

    R visit(PropertyIndividualValue propertyValue) throws E;

    R visit(PropertyDatatypeValue propertyValue) throws E;

    R visit(PropertyLiteralValue propertyValue) throws E;

    R visit(PropertyAnnotationValue propertyValue) throws E;
}

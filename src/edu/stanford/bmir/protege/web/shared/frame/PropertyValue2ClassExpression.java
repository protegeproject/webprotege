package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.OWLClassExpression;

import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/11/2012
 */
public class PropertyValue2ClassExpression implements PropertyValueVisitor<Set<OWLClassExpression>, RuntimeException> {

    private static Set<OWLClassExpression> toSet(OWLClassExpression ce) {
        return Collections.singleton(ce);
    }

    @Override
    public Set<OWLClassExpression> visit(PropertyClassValue propertyValue) throws RuntimeException {
        return toSet(DataFactory.get().getOWLObjectSomeValuesFrom(propertyValue.getProperty(), propertyValue.getValue()));
    }

    @Override
    public Set<OWLClassExpression> visit(PropertyIndividualValue propertyValue) throws RuntimeException {
        return toSet(DataFactory.get().getOWLObjectHasValue(propertyValue.getProperty(), propertyValue.getValue()));
    }

    @Override
    public Set<OWLClassExpression> visit(PropertyDatatypeValue propertyValue) throws RuntimeException {
        return toSet(DataFactory.get().getOWLDataSomeValuesFrom(propertyValue.getProperty(), propertyValue.getValue()));
    }

    @Override
    public Set<OWLClassExpression> visit(PropertyLiteralValue propertyValue) throws RuntimeException {
        return toSet(DataFactory.get().getOWLDataHasValue(propertyValue.getProperty(), propertyValue.getValue()));
    }

    @Override
    public Set<OWLClassExpression> visit(PropertyAnnotationValue propertyValue) throws RuntimeException {
        return Collections.emptySet();
    }
}

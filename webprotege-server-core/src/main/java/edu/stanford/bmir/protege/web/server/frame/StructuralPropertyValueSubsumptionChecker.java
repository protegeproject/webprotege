package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.hierarchy.HasHasAncestor;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.regex.Pattern;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class StructuralPropertyValueSubsumptionChecker implements PropertyValueSubsumptionChecker {


    private final HasHasAncestor<OWLClass, OWLClass> classAncestorChecker;

    private final HasHasAncestor<OWLObjectProperty, OWLObjectProperty> objectPropertyAncestorChecker;

    private final HasHasAncestor<OWLDataProperty, OWLDataProperty> dataPropertyAncestorChecker;

    private final HasHasAncestor<OWLNamedIndividual, OWLClass> individualClassAncestorChecker;

    @Inject
    public StructuralPropertyValueSubsumptionChecker(HasHasAncestor<OWLClass, OWLClass> classAncestorChecker,
                                                     HasHasAncestor<OWLObjectProperty, OWLObjectProperty> objectPropertyAncestorChecker,
                                                     HasHasAncestor<OWLDataProperty, OWLDataProperty> dataPropertyAncestorChecker,
                                                     HasHasAncestor<OWLNamedIndividual, OWLClass> individualClassAncestorChecker) {
        this.classAncestorChecker = classAncestorChecker;
        this.objectPropertyAncestorChecker = objectPropertyAncestorChecker;
        this.dataPropertyAncestorChecker = dataPropertyAncestorChecker;
        this.individualClassAncestorChecker = individualClassAncestorChecker;
    }

    @Override
    public boolean isSubsumedBy(PropertyValue propertyValueA, PropertyValue propertyValueB) {
        if (isSubsumedBy(propertyValueA.getProperty(), propertyValueB.getProperty())) {
            if (isSubsumedBy(propertyValueA.getValue(), propertyValueB.getValue())) {
                return true;
            }
        }
        return false;
    }

    private boolean isSubsumedBy(Object entityA, Object entityB) {
        if (entityA.equals(entityB)) {
            return true;
        }
        if (entityA instanceof OWLNamedIndividual && entityB instanceof OWLClass) {
            OWLNamedIndividual indA = (OWLNamedIndividual) entityA;
            OWLClass clsB = (OWLClass) entityB;
            return individualClassAncestorChecker.hasAncestor(indA, clsB);
        }
        else if (entityA instanceof OWLClass && entityB instanceof OWLClass) {
            OWLClass clsA = (OWLClass) entityA;
            OWLClass clsB = (OWLClass) entityB;
            return classAncestorChecker.hasAncestor(clsA, clsB);
        }
        else if (entityA instanceof OWLObjectProperty && entityB instanceof OWLObjectProperty) {
            OWLObjectProperty propertyA = (OWLObjectProperty) entityA;
            OWLObjectProperty propertyB = (OWLObjectProperty) entityB;
            return objectPropertyAncestorChecker.hasAncestor(propertyA, propertyB);
        }
        else if (entityA instanceof OWLDataProperty && entityB instanceof OWLDataProperty) {
            OWLDataProperty propertyA = (OWLDataProperty) entityA;
            OWLDataProperty propertyB = (OWLDataProperty) entityB;
            return dataPropertyAncestorChecker.hasAncestor(propertyA, propertyB);
        }
        else if (entityA instanceof OWLLiteral && entityB instanceof OWLDatatype) {
            OWLLiteral litA = (OWLLiteral) entityA;
            OWLDatatype dtB = (OWLDatatype) entityB;
            if(litA.getDatatype().equals(dtB)) {
                if(dtB.isBuiltIn()) {
                    Pattern pattern = dtB.getBuiltInDatatype().getPattern();
                    return pattern.matcher(litA.getLiteral()).matches();
                }
                else {
                    return true;
                }
            }
            else {
                // Should check datatype subsumption hierarchy!
                return false;
            }
        }
        else {
            return false;
        }
    }
}

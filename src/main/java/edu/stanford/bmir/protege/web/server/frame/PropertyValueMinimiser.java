package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassClassAncestorChecker;
import edu.stanford.bmir.protege.web.server.hierarchy.DataPropertyDataPropertyAncestorChecker;
import edu.stanford.bmir.protege.web.server.hierarchy.NamedIndividualClassAncestorChecker;
import edu.stanford.bmir.protege.web.server.hierarchy.ObjectPropertyObjectPropertyAncestorChecker;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Iterator;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class PropertyValueMinimiser {

    public List<PropertyValue> minimisePropertyValues(List<PropertyValue> propertyValues,
                                                    OWLOntology ontology,
                                                    OWLAPIProject project) {
        List<PropertyValue> result = Lists.newArrayList(propertyValues);
        for (int i = 0; i < propertyValues.size(); i++) {
            for (int j = 0; j < propertyValues.size(); j++) {
                if (i != j && result.get(i) != null && result.get(j) != null) {
                    PropertyValue propertyValueA = propertyValues.get(i);
                    PropertyValue propertyValueB = propertyValues.get(j);
                    PropertyValueSubsumptionChecker subsumptionChecker = getPropertyValueSubsumptionChecker(ontology,
                            project);
                    if (subsumptionChecker.isSubsumedBy(propertyValueA, propertyValueB)) {
                        // Don't show B because this is more specific!
                        result.set(j, null);
                    }
                }
            }
        }
        for (Iterator<PropertyValue> it = result.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                it.remove();
            }
        }
        return result;
    }

    private PropertyValueSubsumptionChecker getPropertyValueSubsumptionChecker(OWLOntology ontology,
                                                                               OWLAPIProject project) {
        ClassClassAncestorChecker classAncestorChecker = new ClassClassAncestorChecker(project
                .getClassHierarchyProvider());
        ObjectPropertyObjectPropertyAncestorChecker objectPropertyAncestorChecker = new
                ObjectPropertyObjectPropertyAncestorChecker(
                project.getObjectPropertyHierarchyProvider());
        DataPropertyDataPropertyAncestorChecker dataPropertyAncestorChecker = new
                DataPropertyDataPropertyAncestorChecker(
                project.getDataPropertyHierarchyProvider());
        NamedIndividualClassAncestorChecker namedIndividualClassAncestorChecker = new
                NamedIndividualClassAncestorChecker(
                ontology,
                classAncestorChecker);
        return new StructuralPropertyValueSubsumptionChecker(classAncestorChecker,
                objectPropertyAncestorChecker,
                dataPropertyAncestorChecker,
                namedIndividualClassAncestorChecker);
    }
}

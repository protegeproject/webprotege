package edu.stanford.bmir.protege.web.server.comparator;

import edu.stanford.bmir.protege.web.server.frame.PropertyValueComparator;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueState;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class OntologyAnnotationsComparator implements Comparator<OWLAnnotation> {


    private static Map<OWLAnnotationProperty, Integer> indexMap = new HashMap<OWLAnnotationProperty, Integer>();

    static {
        addToIndexMap(DataFactory.get().getRDFSLabel());
        addToIndexMap(DublinCoreVocabulary.TITLE);
        addToIndexMap(DataFactory.get().getOWLVersionInfo());
        addToIndexMap(DataFactory.get().getRDFSComment());
        addToIndexMap(DublinCoreVocabulary.DESCRIPTION);
        addToIndexMap(DublinCoreVocabulary.PUBLISHER);
        addToIndexMap(DublinCoreVocabulary.CREATOR);
        addToIndexMap(DublinCoreVocabulary.CONTRIBUTOR);
        addToIndexMap(DublinCoreVocabulary.SOURCE);
        addToIndexMap(DataFactory.get().getRDFSSeeAlso());

    }

    private static void addToIndexMap(OWLAnnotationProperty property) {
        indexMap.put(property, indexMap.size());
    }

    private static void addToIndexMap(DublinCoreVocabulary vocabulary) {
        addToIndexMap(DataFactory.getOWLAnnotationProperty(vocabulary.getIRI()));
    }

    private OWLAPIProject project;

    public OntologyAnnotationsComparator(OWLAPIProject project) {
        this.project = project;
    }

    private int getPropertyIndex(OWLAnnotationProperty property) {
        Integer index = indexMap.get(property);
        if(index == null) {
            return Integer.MAX_VALUE;
        }
        return index;
    }


    @Override
    public int compare(OWLAnnotation o1, OWLAnnotation o2) {
        Integer index1 = getPropertyIndex(o1.getProperty());
        Integer index2 = getPropertyIndex(o2.getProperty());
        if(!index1.equals(index2)) {
            return index1 - index2;
        }
        PropertyValueComparator propertyValueComparator = new PropertyValueComparator(project);
        PropertyValue propertyValue1 = new PropertyAnnotationValue(o1.getProperty(), o1.getValue(), PropertyValueState.ASSERTED);
        PropertyValue propertyValue2 = new PropertyAnnotationValue(o2.getProperty(), o2.getValue(), PropertyValueState.ASSERTED);
        return propertyValueComparator.compare(propertyValue1, propertyValue2);
    }
}

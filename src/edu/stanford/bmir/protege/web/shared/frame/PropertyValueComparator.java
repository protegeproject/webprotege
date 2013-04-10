package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Comparator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/12/2012
 */
public class PropertyValueComparator implements Comparator<PropertyValue> {

    private OWLAnnotationProperty labelProperty;

    private String lang;

    public PropertyValueComparator(OWLAnnotationProperty labelProperty, String lang) {
        this.labelProperty = labelProperty;
        this.lang = lang;
    }

    @Override
    public int compare(PropertyValue o1, PropertyValue o2) {
        if(o1.isAnnotation()) {
            if(o2.isAnnotation()) {
                return compareAnnos((PropertyAnnotationValue) o1, (PropertyAnnotationValue) o2);
            }
            else {
                return -1;
            }
        }
        else {
            if(o2.isAnnotation()) {
                return 1;
            }
        }
        return o1.compareTo(o2);
    }

    private int compareAnnos(PropertyAnnotationValue o1, PropertyAnnotationValue o2) {
        if(o1.getProperty().equals(labelProperty)) {
            if(o2.getProperty().equals(labelProperty)) {
                if (o1.getValue() instanceof OWLLiteral && o2.getValue() instanceof OWLLiteral) {
                    return compareLiterals((OWLLiteral) o1.getValue(), (OWLLiteral) o2.getValue());
                }
                else {
                    return o1.getValue().compareTo(o2.getValue());
                }
            }
            else {
                return -1;
            }
        }
        else {
            if(o2.getProperty().equals(labelProperty)) {
                return 1;
            }
        }
        if(o1.getValue() instanceof OWLLiteral && o2.getValue() instanceof OWLLiteral) {
            int litDiff = compareLiterals((OWLLiteral) o1.getValue(), (OWLLiteral) o2.getValue());
            if(litDiff != 0) {
                return litDiff;
            }
            return o1.compareTo(o2);
        }
        return o1.compareTo(o2);
    }

    private int compareLiterals(OWLLiteral o1, OWLLiteral o2) {
        if(lang.equals(o1.getLang())) {
            if(lang.equals(o2.getLang())) {
                return o1.compareTo(o2);
            }
            else {
                return -1;
            }
        }
        else {
            if(lang.equals(o2.getLang())) {
                return 1;
            }
        }

        return o1.compareTo(o2);

    }
}

package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.HasLang;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Comparator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/03/2013
 */
public class PropertyValueComparator implements Comparator<PropertyValue> {

    public static final int AFTER = 1;

    public static final int BEFORE = -AFTER;

    @Nonnull
    private final Comparator<? super OWLAnnotationProperty> annotationPropertyComparator;

    @Nonnull
    private final HasLang hasLang;

    @Inject
    public PropertyValueComparator(@Nonnull Comparator<? super OWLAnnotationProperty> annotationPropertyComparator,
                                   @Nonnull HasLang hasLang) {
        this.annotationPropertyComparator = annotationPropertyComparator;
        this.hasLang = hasLang;
    }

    @Override
    public int compare(PropertyValue o1, PropertyValue o2) {

//        if(o1.getState() == State.DERIVED) {
//            if(o2.getState() != State.DERIVED) {
//                return -1;
//            }
//        }
//        else if(o2.getState() == State.DERIVED) {
//            return 1;
//        }

        // Sort property values by language, property rendering and then value rendering.
        // rdfs:label and then rdfs:comment get priority over other properties.

        OWLPrimitiveData val1 = o1.getValue();
        OWLPrimitiveData val2 = o2.getValue();
        if(val1 instanceof OWLLiteralData && val2 instanceof OWLLiteralData) {
            OWLLiteralData lit1 = (OWLLiteralData) val1;
            OWLLiteralData lit2 = (OWLLiteralData) val2;
            if(lit1.hasLang() && lit2.hasLang()) {
                if(isDefaultLanguage(lit1)) {
                    if(!isDefaultLanguage(lit2)) {
                        return BEFORE;
                    }
                }
                else {
                    if(isDefaultLanguage(lit2)) {
                        return AFTER;
                    }
                }
                int langDelta = lit1.getLang().compareToIgnoreCase(lit2.getLang());
                if(langDelta != 0) {
                    return langDelta;
                }
            }
        }



        OWLPropertyData property1 = o1.getProperty();
        OWLPropertyData property2 = o2.getProperty();
        if(property1.isOWLAnnotationProperty()) {
            if (property2.isOWLAnnotationProperty()) {
                OWLAnnotationPropertyData annoProp1 = (OWLAnnotationPropertyData) property1;
                OWLAnnotationPropertyData annoProp2 = (OWLAnnotationPropertyData) property2;
                return annotationPropertyComparator.compare(annoProp1.getEntity(), annoProp2.getEntity());
            }
            else {
                return BEFORE;
            }
        }
        else if(property2.isOWLAnnotationProperty()) {
            return AFTER;
        }
        String prop1BrowserText = property1.getBrowserText();
        String prop2BrowserText = property2.getBrowserText();
        int delta = prop1BrowserText.compareToIgnoreCase(prop2BrowserText);
        if(delta != 0) {
            return delta;
        }

        String val1Rendering = val1.getBrowserText();
        String val2Rendering = val2.getBrowserText();
        return val1Rendering.compareToIgnoreCase(val2Rendering);
    }

    private boolean isDefaultLanguage(OWLLiteralData lit) {
        return lit.hasLang() && hasLang.getLang().equals(lit.getLang());
    }
}

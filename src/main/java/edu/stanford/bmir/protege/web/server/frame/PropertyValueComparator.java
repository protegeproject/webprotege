package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.server.render.DefaultAnnotationPropertyComparator;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueState;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;

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

    private OWLAPIProject project;
    private DefaultAnnotationPropertyComparator annotationPropertyComparator;

    public PropertyValueComparator(OWLAPIProject project) {
        this.project = project;
        this.annotationPropertyComparator = new DefaultAnnotationPropertyComparator(
                project.getRenderingManager().getShortFormProvider()
        );
    }

    @Override
    public int compare(PropertyValue o1, PropertyValue o2) {

//        if(o1.getState() == PropertyValueState.DERIVED) {
//            if(o2.getState() != PropertyValueState.DERIVED) {
//                return -1;
//            }
//        }
//        else if(o2.getState() == PropertyValueState.DERIVED) {
//            return 1;
//        }

        // Sort property values by language, property rendering and then value rendering.
        // rdfs:label and then rdfs:comment get priority over other properties.

        OWLObject val1 = o1.getValue();
        OWLObject val2 = o2.getValue();
        if(val1 instanceof OWLLiteral && val2 instanceof OWLLiteral) {
            OWLLiteral lit1 = (OWLLiteral) val1;
            OWLLiteral lit2 = (OWLLiteral) val2;
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



        OWLEntity property1 = o1.getProperty();
        OWLEntity property2 = o2.getProperty();
        if(property1.isOWLAnnotationProperty()) {
            if (property2.isOWLAnnotationProperty()) {
                OWLAnnotationProperty annoProp1 = (OWLAnnotationProperty) property1;
                OWLAnnotationProperty annoProp2 = (OWLAnnotationProperty) property2;
                return annotationPropertyComparator.compare(annoProp1, annoProp2);
            }
            else {
                return BEFORE;
            }
        }
        else if(property2.isOWLAnnotationProperty()) {
            return AFTER;
        }
        final RenderingManager rm = project.getRenderingManager();
        String prop1BrowserText = rm.getBrowserText(property1);
        String prop2BrowserText = rm.getBrowserText(property2);
        int delta = prop1BrowserText.compareToIgnoreCase(prop2BrowserText);
        if(delta != 0) {
            return delta;
        }

        String val1Rendering = rm.getBrowserText(val1);
        String val2Rendering = rm.getBrowserText(val2);
        return val1Rendering.compareToIgnoreCase(val2Rendering);
    }

    private boolean isDefaultLanguage(OWLLiteral lit) {
        return lit.hasLang() && project.getDefaultLanguage().equals(lit.getLang());
    }
}

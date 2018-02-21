package edu.stanford.bmir.protege.web.shared.renderer;

import org.semanticweb.owlapi.model.AxiomType;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class AxiomTypeRenderer {

    private static Map<AxiomType, String> renderingMap = new HashMap<AxiomType, String>();


    static {
        renderingMap.put(AxiomType.OBJECT_PROPERTY_DOMAIN, "Domain");
        renderingMap.put(AxiomType.OBJECT_PROPERTY_RANGE, "Range");
        renderingMap.put(AxiomType.DATA_PROPERTY_DOMAIN, "Domain");
        renderingMap.put(AxiomType.DATA_PROPERTY_RANGE, "Range");

        renderingMap.put(AxiomType.OBJECT_PROPERTY_ASSERTION, "Property Assertion");
        renderingMap.put(AxiomType.DATA_PROPERTY_ASSERTION, "Property Assertion");
        renderingMap.put(AxiomType.CLASS_ASSERTION, "Class Assertion");
        renderingMap.put(AxiomType.DIFFERENT_INDIVIDUALS, "Different Individuals");
        renderingMap.put(AxiomType.SAME_INDIVIDUAL, "Same Individual");
    }

    public static String getRendering(AxiomType axiomType) {
        String rendering = renderingMap.get(axiomType);
        if(rendering != null) {
            return rendering;
        }
        return axiomType.getName();
    }
}

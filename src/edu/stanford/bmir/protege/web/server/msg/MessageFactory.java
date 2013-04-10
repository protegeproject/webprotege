package edu.stanford.bmir.protege.web.server.msg;

import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */
public class MessageFactory {

    public static StructuredOWLMessage createdClass(String browserText, OWLClass superClass) {
        return new StructuredOWLMessage("Created {0} as a subclass of {1}", browserText, superClass);
    }
}

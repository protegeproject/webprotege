package edu.stanford.bmir.protege.web.server.owlapi;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public interface OWLAPIProjectAttributesListener {

    void attributeRemoved(OWLAPIProjectAttributes attributes, String attributeName);
    
    void attributeChanged(OWLAPIProjectAttributes attributes, String attributeName);

    void attributesRemoved(OWLAPIProjectAttributes attributes);
}

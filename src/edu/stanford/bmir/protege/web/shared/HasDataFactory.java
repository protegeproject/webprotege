package edu.stanford.bmir.protege.web.shared;

import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public interface HasDataFactory {

    OWLDataFactory getDataFactory();

}

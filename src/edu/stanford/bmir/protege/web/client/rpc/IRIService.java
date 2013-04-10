package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/10/2012
 */
@RemoteServiceRelativePath("iriservice")
public interface IRIService extends RemoteService {

    IRI getNextIRI(String fragment);

    OWLClass getNextOWLClass(String fragment);

    OWLObjectProperty getNextOWLObjectProperty(String fragment);
}

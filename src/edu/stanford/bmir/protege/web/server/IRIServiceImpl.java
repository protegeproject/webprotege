package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.stanford.bmir.protege.web.client.rpc.IRIService;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/10/2012
 */
public class IRIServiceImpl extends RemoteServiceServlet implements IRIService {

    public synchronized IRI getNextIRI(String fragment) {
        return IRI.create("http://stuff.com#" + fragment);
    }

    @Override
    public synchronized OWLClass getNextOWLClass(String fragment) {
        return new OWLClassImpl(getNextIRI(fragment));
    }

    @Override
    public synchronized OWLObjectProperty getNextOWLObjectProperty(String fragment) {
        IRI iri = getNextIRI(fragment);
        return new OWLObjectPropertyImpl(iri);
    }
}

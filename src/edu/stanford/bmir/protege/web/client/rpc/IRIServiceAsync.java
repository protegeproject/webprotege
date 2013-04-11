package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public interface IRIServiceAsync {

    void getNextIRI(String fragment, AsyncCallback<IRI> async);

    void getNextOWLClass(String fragment, AsyncCallback<OWLClass> async);

    void getNextOWLObjectProperty(String fragment, AsyncCallback<OWLObjectProperty> async);
}

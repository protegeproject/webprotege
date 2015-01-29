package edu.stanford.bmir.protege.web.shared.axiom;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.io.Serializable;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class OWLAxiomData implements IsSerializable, Serializable {

    private OWLAxiom axiom;

    private String htmlRendering;

    private OWLAxiomData() {
    }

    public OWLAxiomData(OWLAxiom axiom, String htmlRendering) {
        this.axiom = axiom;
        this.htmlRendering = htmlRendering;
    }

    public OWLAxiom getAxiom() {
        return axiom;
    }

    public String getHtmlRendering() {
        return htmlRendering;
    }

}

package edu.stanford.bmir.protege.web.shared.axiom;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class OWLAxiomData implements IsSerializable, Serializable {

    private String htmlRendering;

    private OWLAxiomData() {
    }

    public OWLAxiomData(String htmlRendering) {
        this.htmlRendering = checkNotNull(htmlRendering);
    }

    public String getHtmlRendering() {
        return htmlRendering;
    }

}

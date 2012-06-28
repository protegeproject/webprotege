package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 * <p>
 *     Represents an IRI.
 * </p>
 */
public class WebProtegeIRI extends Primitive implements Serializable {

    private String iri;

    private WebProtegeIRI() {
    }

    public String getShortForm() {
        int hashSep = iri.lastIndexOf("#");
        if(hashSep != -1) {
            return iri.substring(hashSep);
        }
        int slashSep = iri.lastIndexOf("/");
        if(slashSep != -1) {
            return iri.substring(slashSep);
        }
        return iri;
    }
    
    public WebProtegeIRI(String iri) {
        this.iri = iri;
    }

    public String getIRI() {
        return iri;
    }
}

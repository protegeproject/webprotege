package edu.stanford.bmir.protege.web.server.bioportal;

import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyFormat;

import java.lang.reflect.Constructor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 * <p>
 *     An enum that describes the "formats" that BioPortal accepts. (These seem to be a mixture of formats and languages).
 * </p>
 */
public enum BioPortalOntologyFormat {

    /**
     * Ontologies that are OWL compatible.  We assume this means RDF/XML
     */
    OWL("OWL");

    private String formatName;

    private BioPortalOntologyFormat(String formatName) {
        this.formatName = formatName;
    }

    public String getFormatName() {
        return formatName;
    }
    

}

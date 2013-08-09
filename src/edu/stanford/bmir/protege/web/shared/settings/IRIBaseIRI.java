package edu.stanford.bmir.protege.web.shared.settings;

import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/07/2013
 */
public class IRIBaseIRI extends SettingName<IRI> {

    public IRIBaseIRI() {
        super("iriBase", IRI.class);
    }
}

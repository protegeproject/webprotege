package edu.stanford.bmir.protege.web.server.bioportal;

import edu.stanford.bmir.protege.web.server.rest.BioPortalRestCall;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalGetOntologiesRestCall extends BioPortalRestCall<BioPortalOntologiesList> {

    public BioPortalGetOntologiesRestCall(String restServiceBase) {
        super(restServiceBase, "ontologies", BioPortalOntologiesList.class);
    }
}

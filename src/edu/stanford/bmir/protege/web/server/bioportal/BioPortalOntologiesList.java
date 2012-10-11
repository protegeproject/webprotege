package edu.stanford.bmir.protege.web.server.bioportal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
@XmlRootElement(name = "list")
public class BioPortalOntologiesList {

    @XmlElement(name = "ontologyBean")
    private List<BioPortalOntologyInfoBean> ontologies = new ArrayList<BioPortalOntologyInfoBean>();

    public BioPortalOntologiesList() {
    }

    public List<BioPortalOntologyInfoBean> getOntologies() {
        return ontologies;
    }
}

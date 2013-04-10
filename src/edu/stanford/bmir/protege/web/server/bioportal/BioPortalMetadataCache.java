package edu.stanford.bmir.protege.web.server.bioportal;

import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalOntologyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalMetadataCache {

    private static final BioPortalMetadataCache instance = new BioPortalMetadataCache();
    
    private List<BioPortalOntologyInfo> ontologies = new ArrayList<BioPortalOntologyInfo>();

    private BioPortalMetadataCache() {
        refresh();
    }
    
    public static BioPortalMetadataCache getCache() {
        return instance;
    }

    public void refresh() {
        BioPortalRestAPI api = new BioPortalRestAPI(BioPortalConfigurationManager.getManager().getRestBase());
        ontologies.clear();
        ontologies.addAll(api.getOntologies());
    }

    public List<BioPortalOntologyInfo> getOntologies() {
        return new ArrayList<BioPortalOntologyInfo>(ontologies);
    }

    public BioPortalOntologyInfo getOntologyWithDisplayName(String displayName) {
        for(BioPortalOntologyInfo info : ontologies) {
            String displayLabel = info.getDisplayLabel();
            if(displayLabel != null && displayLabel.equals(displayName)) {
                return info;
            }
        }
        return null;
    }

}

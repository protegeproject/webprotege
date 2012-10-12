package edu.stanford.bmir.protege.web.client.ui.bioportal.publish;

import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalOntologyInfo;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/10/2012
 */
public class BioPortalOntologyInfoSuggestion implements SuggestOracle.Suggestion {

    private BioPortalOntologyInfo ontologyInfo;
    
    private String displayString;

    public BioPortalOntologyInfoSuggestion(BioPortalOntologyInfo ontologyInfo, String displayString) {
        this.ontologyInfo = ontologyInfo;
        this.displayString = displayString;
    }

    /**
     * Gets the BioPortalOntologyInfo to which this suggestion pertains.
     * @return The BioPortalOntologyInfo.  Not <code>null</code>.
     */
    public BioPortalOntologyInfo getOntologyInfo() {
        return ontologyInfo;
    }

    public String getDisplayString() {
        return displayString;
    }

    public String getReplacementString() {
        return ontologyInfo.getDisplayLabel();
    }
}

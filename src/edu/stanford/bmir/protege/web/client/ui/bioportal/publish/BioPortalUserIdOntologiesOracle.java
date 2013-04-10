package edu.stanford.bmir.protege.web.client.ui.bioportal.publish;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/10/2012
 */
public class BioPortalUserIdOntologiesOracle extends SuggestOracle {

    private BioPortalUserId bioPortalUserId;

    private boolean enabled = true;

    public BioPortalUserIdOntologiesOracle() {
    }

    public void setBioPortalUserId(BioPortalUserId bioPortalUserId) {
        this.bioPortalUserId = bioPortalUserId;
    }

    /**
     * Gets the BioPortalUserId that this oracle uses for retrieving ontologies.
     * @return The BioPortalUserId. Not <code>null</code>.
     */
    public BioPortalUserId getBioPortalUserId() {
        return bioPortalUserId;
    }

    /**
     * Specifies whether the oracle is enabled or not.  If the oracle is not enabled then it will not generate
     * any suggestion.
     * @param b <code>true</code> to enable, and <code>false</code> to disable.
     */
    public void setEnabled(boolean b) {
        enabled = b;
    }

    @Override
    public boolean isDisplayStringHTML() {
        return true;
    }

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        if(!enabled) {
            return;
        }
        if(bioPortalUserId == null) {
            return;
        }
        BioPortalAPIServiceAsync serviceAsync = BioPortalAPIServiceManager.getServiceAsync();
        serviceAsync.getOwnedOntologies(bioPortalUserId, new AsyncCallback<List<BioPortalOntologyInfo>>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(List<BioPortalOntologyInfo> result) {
                Collection<Suggestion> suggestions = new HashSet<Suggestion>();
                String query = request.getQuery();
                for(BioPortalOntologyInfo info : result) {
                    if (getMatchIndexForOntologyInfoAndDisplayLabelQuery(info, query) != -1) {
                        String displayString = getDisplayStringForBioPortalOntologyInfo(info, query);
                        suggestions.add(new BioPortalOntologyInfoSuggestion(info, displayString));
                        if(suggestions.size() == request.getLimit()) {
                            break;
                        }
                    }
                }
                if (!suggestions.isEmpty()) {
                    callback.onSuggestionsReady(request, new Response(suggestions));
                }
            }
        });
    }

    /**
     * Gets the display string for a BioPortalOntologyInfo object.
     * @param info The info whose display string is to be returned.
     * @param query The query in the suggest box.
     * @return The display string for the specified BioPortalOntologyInfo and query
     */
    private String getDisplayStringForBioPortalOntologyInfo(BioPortalOntologyInfo info, String query) {
        String displayLabel = info.getDisplayLabel();
        int index = getMatchIndexForOntologyInfoAndDisplayLabelQuery(info, query);
        if (index != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append(displayLabel.substring(0, index));
            sb.append("<span class=\"web-protege-entity-match-substring\">");
            sb.append(displayLabel.substring(index, index + query.length()));
            sb.append("</span>");
            sb.append(displayLabel.substring(index + query.length()));
            sb.append("  (");
            sb.append(info.getAbbreviation());
            sb.append("  ");
            sb.append(info.getVersionNumber());
            sb.append(")");
            return sb.toString();
        }
        else {
            return "";
        }
    }

    private int getMatchIndexForOntologyInfoAndDisplayLabelQuery(BioPortalOntologyInfo info, String query) {
        String displayLabel = info.getDisplayLabel();
        String normalisedDisplayLabel = displayLabel.toLowerCase();
        return normalisedDisplayLabel.indexOf(query.toLowerCase());
    }
}

package edu.stanford.bmir.protege.web.client.ui.tab;

import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;

import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/16
 */
public class PortletConfigurationComparator implements Comparator<PortletConfiguration> {

    public int compare(PortletConfiguration pc1, PortletConfiguration pc2) {
        String pc1is = pc1.getIndex();
        String pc2is = pc2.getIndex();

        if ((pc1is == null || pc1is.length() == 0) && (pc2is == null || pc2is.length() == 0)) {
            return 0;
        }
        if (pc1is == null || pc1is.length() == 0) {
            return 1;
        }
        if (pc2is == null || pc2is.length() == 0) {
            return -1;
        }
        return pc2is.compareTo(pc1is); //we could do an int comparison here
    }
}

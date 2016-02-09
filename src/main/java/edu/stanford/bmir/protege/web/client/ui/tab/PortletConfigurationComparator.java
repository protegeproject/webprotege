package edu.stanford.bmir.protege.web.client.ui.tab;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;

import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/16
 */
public class PortletConfigurationComparator implements Comparator<PortletConfiguration> {

    public int compare(PortletConfiguration pc1, PortletConfiguration pc2) {
        Optional<Integer> pc1Index = pc1.getIndexAsInt();
        Optional<Integer> pc2Index = pc2.getIndexAsInt();

        if(pc1Index.isPresent() && pc2Index.isPresent()) {
            return pc1Index.get() - pc2Index.get();
        }
        return -1;
    }
}

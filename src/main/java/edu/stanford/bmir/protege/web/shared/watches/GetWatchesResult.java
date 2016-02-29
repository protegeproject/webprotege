package edu.stanford.bmir.protege.web.shared.watches;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class GetWatchesResult implements Result {

    private Set<EntityBasedWatch> watches;

    /**
     * For serialization purposes only
     */
    private GetWatchesResult() {
    }

    public GetWatchesResult(Set<EntityBasedWatch> watches) {
        this.watches = new HashSet<EntityBasedWatch>(watches);
    }

    public Set<EntityBasedWatch> getWatches() {
        return Collections.unmodifiableSet(watches);
    }
}

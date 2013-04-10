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

    private Set<Watch> watches;

    public GetWatchesResult(Set<Watch> watches) {
        this.watches = new HashSet<Watch>(watches);
    }

    public Set<Watch> getWatches() {
        return Collections.unmodifiableSet(watches);
    }
}

package edu.stanford.bmir.protege.web.server.hierarchy;

import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
public interface HasGetAncestors<N> {

    Set<N> getAncestors(N object);
}

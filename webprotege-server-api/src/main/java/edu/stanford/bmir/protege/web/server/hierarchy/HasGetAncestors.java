package edu.stanford.bmir.protege.web.server.hierarchy;

import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
public interface HasGetAncestors<N> {

    Collection<N> getAncestors(N object);
}

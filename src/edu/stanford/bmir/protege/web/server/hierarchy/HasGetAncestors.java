package edu.stanford.bmir.protege.web.server.hierarchy;

import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public interface HasGetAncestors<N> {

    Set<N> getAncestors(N node);
}

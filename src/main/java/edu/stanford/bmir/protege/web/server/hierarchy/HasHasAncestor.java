package edu.stanford.bmir.protege.web.server.hierarchy;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public interface HasHasAncestor<N, M> {

    boolean hasAncestor(N node, M node2);
}

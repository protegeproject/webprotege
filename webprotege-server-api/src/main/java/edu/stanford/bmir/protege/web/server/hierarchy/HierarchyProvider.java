package edu.stanford.bmir.protege.web.server.hierarchy;

import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public interface HierarchyProvider<N> extends HasGetAncestors<N> {

    /**
     * Gets the objects that represent the roots of the hierarchy.
     */
    Collection<N> getRoots();

    Collection<N> getChildren(N object);

    boolean isLeaf(N object);

    Collection<N> getDescendants(N object);

    Collection<N> getParents(N object);

    Collection<N> getAncestors(N object);

    Collection<List<N>> getPathsToRoot(N object);

    boolean isAncestor(N descendant, N ancestor);

    default boolean isParent(N child, N parent) {
        return getParents(child).contains(parent);
    }
}

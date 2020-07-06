package edu.stanford.bmir.protege.web.server.hierarchy;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 01-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * <p/>
 * A basic partial implementation of a hierarchy provider, which
 * handles listeners and event firing, and also provides basic
 * implementations of method such as getAncestors, getDescendants etc.
 * which use other core methods.
 */
public abstract class AbstractHierarchyProvider<N> implements HierarchyProvider<N> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractHierarchyProvider.class);

    protected AbstractHierarchyProvider() {
    }

    public void dispose() {
    }


    public Collection<N> getAncestors(N object) {
        Set<N> results = new HashSet<>();
        getAncestors(results, object);
        return results;
    }


    private void getAncestors(Set<N> results, N object) {
        for (N parent : getParents(object)) {
            if (results.add(parent)) {
                getAncestors(results, parent);
            }
        }
    }

    @Override
    public boolean isAncestor(N descendant, N ancestor) {
        Set<N> processed = new HashSet<>();
        Deque<N> processingQueue = new ArrayDeque<>();
        processingQueue.push(descendant);
        while(!processingQueue.isEmpty()) {
            var currentNode = processingQueue.pop();
            var parents = getParents(currentNode);
            for(var parent : parents) {
                if(ancestor.equals(parent)) {
                    return true;
                }
                if(!processed.contains(parent)) {
                    processed.add(parent);
                    processingQueue.add(parent);
                }
            }
        }
        return false;
    }


    @Override
    public boolean isLeaf(N object) {
        return getChildren(object).isEmpty();
    }

    public Collection<N> getDescendants(N object) {
        Set<N> results = new HashSet<>();
        getDescendants(results, object);
        return results;
    }


    private void getDescendants(Set<N> results, N object) {
        for (N child : getChildren(object)) {
            if (!results.contains(child)) {
                results.add(child);
                getDescendants(results, child);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Paths to root stuff
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets the paths to the root class for the specified object.
     * @return A <code>Set</code> of <code>List</code>s of <code>N</code>s
     */
    public Collection<List<N>> getPathsToRoot(N obj) {
        return setOfPaths(obj, new HashSet<>());
    }


    private Set<List<N>> setOfPaths(N obj, Set<N> processed) {
        if (getRoots().contains(obj)) {
            return getSingleSetOfLists(obj);
        }
        Set<List<N>> paths = new HashSet<>();
        for (N par : getParents(obj)) {
            if (!processed.contains(par)) {
                processed.add(par);
                paths.addAll(append(obj, setOfPaths(par, processed)));
            }
        }
        return paths;
    }


    private Set<List<N>> getSingleSetOfLists(N obj) {
        Set<List<N>> set = new HashSet<>();
        List<N> list = new ArrayList<>();
        list.add(obj);
        set.add(list);
        return set;
    }


    private Set<List<N>> append(N obj, Set<List<N>> setOfPaths) {
        for (List<N> path : setOfPaths) {
            path.add(obj);
        }
        return setOfPaths;
    }
}

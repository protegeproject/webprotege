package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.inject.project.ProjectSingleton;
import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProviderListener;
import org.semanticweb.owlapi.model.OWLObject;

import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public interface OWLObjectHierarchyProvider<N extends OWLObject> {

    /**
     * Gets the objects that represent the roots of the hierarchy.
     */
    public Set<N> getRoots();


    public Set<N> getChildren(N object);


    public Set<N> getDescendants(N object);


    public Set<N> getParents(N object);


    public Set<N> getAncestors(N object);


    public Set<N> getEquivalents(N object);


    public Set<List<N>> getPathsToRoot(N object);


    public boolean containsReference(N object);


    public void addListener(OWLObjectHierarchyProviderListener<N> listener);


    public void removeListener(OWLObjectHierarchyProviderListener<N> listener);

}

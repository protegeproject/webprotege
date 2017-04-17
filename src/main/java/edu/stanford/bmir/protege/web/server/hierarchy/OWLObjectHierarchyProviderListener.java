package edu.stanford.bmir.protege.web.server.hierarchy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2017
 */
public interface OWLObjectHierarchyProviderListener<O> {

    void nodeChanged(O node);

    void hierarchyChanged();
}

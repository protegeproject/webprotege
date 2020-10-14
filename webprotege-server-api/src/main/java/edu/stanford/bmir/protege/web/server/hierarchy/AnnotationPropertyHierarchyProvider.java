package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-06
 */
public interface AnnotationPropertyHierarchyProvider extends HierarchyProvider<OWLAnnotationProperty> {

  void handleChanges(List<OntologyChange> changes);
}

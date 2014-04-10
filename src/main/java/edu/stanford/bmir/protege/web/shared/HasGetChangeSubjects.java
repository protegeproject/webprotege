package edu.stanford.bmir.protege.web.shared;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.Set;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public interface HasGetChangeSubjects {

    Set<OWLEntity> getChangeSubjects(OWLOntologyChange change);
}

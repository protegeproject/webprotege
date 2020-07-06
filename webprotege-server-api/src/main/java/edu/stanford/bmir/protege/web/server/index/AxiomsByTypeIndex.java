package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@ProjectSingleton
public interface AxiomsByTypeIndex extends Index {

    <T extends OWLAxiom> Stream<T> getAxiomsByType(AxiomType<T> axiomType, OWLOntologyID ontologyId);
}

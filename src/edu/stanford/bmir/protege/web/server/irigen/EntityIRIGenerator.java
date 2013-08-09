package edu.stanford.bmir.protege.web.server.irigen;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.irigen.IRIGeneratorSettings;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public abstract class EntityIRIGenerator {

    public abstract IRI getEntityIRI(IRIGeneratorSettings iriGeneratorSettings, String shortForm, EntityType<?> entityType, OWLAPIProject project);
}

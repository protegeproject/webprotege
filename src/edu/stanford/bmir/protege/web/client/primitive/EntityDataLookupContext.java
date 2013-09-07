package edu.stanford.bmir.protege.web.client.primitive;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/09/2013
 */
public interface EntityDataLookupContext {

    ProjectId getProjectId();

    Set<EntityType<?>> getAllowedEntityTypes();


}

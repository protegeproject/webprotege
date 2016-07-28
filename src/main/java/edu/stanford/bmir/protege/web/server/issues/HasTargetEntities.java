package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public interface HasTargetEntities {

    ImmutableList<OWLEntity> getTargetEntities();
}

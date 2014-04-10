package edu.stanford.bmir.protege.web.server.render;

import org.semanticweb.owlapi.model.OWLObject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public interface HasGetFrameRendering {

    String getFrameRendering(OWLObject subject);
}

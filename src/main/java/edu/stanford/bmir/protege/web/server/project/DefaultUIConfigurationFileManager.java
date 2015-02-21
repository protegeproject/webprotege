package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;

import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public interface DefaultUIConfigurationFileManager {

    File getDefaultConfigurationFile(OWLAPIProjectType projectType);
}

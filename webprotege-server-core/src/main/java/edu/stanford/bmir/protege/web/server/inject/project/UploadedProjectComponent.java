package edu.stanford.bmir.protege.web.server.inject.project;

import dagger.Component;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.io.OWLObjectRenderer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@Component(modules = {UploadedProjectModule.class})
@ProjectSingleton
public interface UploadedProjectComponent {

    OWLObjectRenderer getOwlObjectRenderer();
}

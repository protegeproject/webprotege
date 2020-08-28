package edu.stanford.bmir.protege.web.server.inject.project;

import dagger.Component;
import edu.stanford.bmir.protege.web.server.project.ProjectDisposablesManager;
import edu.stanford.bmir.protege.web.server.shortform.AnnotationAssertionAxiomsModule;
import edu.stanford.bmir.protege.web.server.shortform.LuceneModule;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.io.OWLObjectRenderer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@Component(modules = {UploadedProjectModule.class, LuceneModule.class})
@ProjectSingleton
public interface UploadedProjectComponent {

    OWLObjectRenderer getOwlObjectRenderer();

    ProjectDisposablesManager getProjectDisposablesManager();
}

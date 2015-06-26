package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;

import javax.inject.Inject;
import javax.servlet.ServletContext;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class WarmUpMetaProjectTask implements ConfigurationTask {


    private final ProjectDetailsManager projectDetailsManager;

    private final WebProtegeLogger logger;

    @Inject
    public WarmUpMetaProjectTask(ProjectDetailsManager projectDetailsManager, WebProtegeLogger logger) {
        this.projectDetailsManager = projectDetailsManager;
        this.logger = logger;
    }

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        int projectInstanceCount = projectDetailsManager.getProjectCount();
        logger.info("Loaded meta-project.  There are %d project instances.", projectInstanceCount);
    }
}

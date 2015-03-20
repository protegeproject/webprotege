package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;

import javax.inject.Inject;
import javax.servlet.ServletContext;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class WarmUpMetaProjectTask implements ConfigurationTask {

    private WebProtegeLogger logger;

    @Inject
    public WarmUpMetaProjectTask(WebProtegeLogger logger) {
        this.logger = logger;
    }

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        ProjectDetailsManager metaProjectManager = MetaProjectManager.getManager();
        int projectInstanceCount = metaProjectManager.getProjectCount();
        logger.info("Loaded meta-project.  There are %d project instances.", projectInstanceCount);
    }
}

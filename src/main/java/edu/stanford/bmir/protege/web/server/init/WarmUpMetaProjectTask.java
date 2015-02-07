package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;

import javax.servlet.ServletContext;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class WarmUpMetaProjectTask implements ConfigurationTask {

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        ProjectDetailsManager metaProjectManager = MetaProjectManager.getManager();
        int projectInstanceCount = metaProjectManager.getProjectCount();
        WebProtegeLoggerManager.get(WarmUpMetaProjectTask.class).info("Loaded meta-project.  There are %d project instances.", projectInstanceCount);
    }
}

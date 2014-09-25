package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.reasoning.ReasoningServerManager;
import edu.stanford.protege.reasoning.KbId;
import edu.stanford.protege.reasoning.ReasoningService;
import edu.stanford.protege.reasoning.action.GetKbDigestAction;

import javax.servlet.ServletContext;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/09/2014
 */
public class StartReasoningServerTask implements ConfigurationTask {

    private final WebProtegeLogger logger = WebProtegeLoggerManager.get(StartReasoningServerTask.class);

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        try {
            ReasoningService reasoningService = ReasoningServerManager.get().getReasoningService();
            reasoningService.execute(new GetKbDigestAction(new KbId("test")));
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new WebProtegeConfigurationException("WebProtégé could not connect to the reasoning server.  Please check that the reasoning server is running on the default port (3456).", e);
        }

    }
}

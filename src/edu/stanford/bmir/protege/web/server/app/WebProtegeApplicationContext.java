package edu.stanford.bmir.protege.web.server.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 * <p>
 *     A reference point for the Spring application context.
 * </p>
 */
public class WebProtegeApplicationContext {

    //  I'm not sure whether this is a good idea or not!

    private static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(WebProtegeApplicationConfig.class);

    public static ApplicationContext getContext() {
        return APPLICATION_CONTEXT;
    }
}

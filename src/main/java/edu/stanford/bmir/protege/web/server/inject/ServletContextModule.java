package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;

import javax.servlet.ServletContext;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ServletContextModule extends AbstractModule {

    private ServletContext servletContext;

    public ServletContextModule(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected void configure() {
        bind(ServletContext.class).toInstance(servletContext);
    }
}

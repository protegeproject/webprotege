package edu.stanford.bmir.protege.web.server.app;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerSingleton {
}

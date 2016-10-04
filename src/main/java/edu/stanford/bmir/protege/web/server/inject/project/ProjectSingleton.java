package edu.stanford.bmir.protege.web.server.inject.project;

import javax.inject.Scope;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Oct 2016
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectSingleton {

}

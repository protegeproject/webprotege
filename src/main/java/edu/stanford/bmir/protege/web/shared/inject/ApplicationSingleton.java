package edu.stanford.bmir.protege.web.shared.inject;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationSingleton {

}

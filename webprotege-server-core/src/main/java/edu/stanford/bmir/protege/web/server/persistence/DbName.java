package edu.stanford.bmir.protege.web.server.persistence;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface DbName {

}

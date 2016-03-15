package edu.stanford.bmir.protege.web.client.events;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/03/16
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface EventPollingPeriod {

}

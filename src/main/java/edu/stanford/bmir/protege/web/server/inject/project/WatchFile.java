package edu.stanford.bmir.protege.web.server.inject.project;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface WatchFile {
}

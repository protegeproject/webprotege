package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.BindingAnnotation;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationHost {
}

package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.BindingAnnotation;

import javax.inject.Qualifier;
import java.lang.annotation.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@Qualifier
@Documented
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface DbHost {
}

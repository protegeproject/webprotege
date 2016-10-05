package edu.stanford.bmir.protege.web.client.primitive;

import com.google.inject.BindingAnnotation;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2014
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface EntitySuggestOracleSuggestLimit {

}

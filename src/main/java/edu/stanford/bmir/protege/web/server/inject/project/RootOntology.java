package edu.stanford.bmir.protege.web.server.inject.project;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface RootOntology {
}

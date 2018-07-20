package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableList;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 */
public interface HasAnnotationPropertyValues {

    ImmutableList<PropertyAnnotationValue> getAnnotationPropertyValues();
}

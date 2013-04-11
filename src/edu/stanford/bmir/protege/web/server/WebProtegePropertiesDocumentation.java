package edu.stanford.bmir.protege.web.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface WebProtegePropertiesDocumentation {

    String description();

    String example() default "";
}

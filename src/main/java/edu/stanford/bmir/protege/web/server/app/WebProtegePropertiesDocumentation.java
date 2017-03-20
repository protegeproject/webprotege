package edu.stanford.bmir.protege.web.server.app;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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

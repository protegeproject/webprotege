package edu.stanford.bmir.protege.web.shared.portlet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/04/16
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Portlet {

    String id();

    String displayName();
}

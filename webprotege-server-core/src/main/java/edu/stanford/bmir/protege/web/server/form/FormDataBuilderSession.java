package edu.stanford.bmir.protege.web.server.form;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Scope
public @interface FormDataBuilderSession {
}

package edu.stanford.bmir.protege.web.server.inject;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Neo4jHost {
}

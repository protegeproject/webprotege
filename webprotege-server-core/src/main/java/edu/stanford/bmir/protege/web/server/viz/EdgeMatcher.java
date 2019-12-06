package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.shared.viz.Edge;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-05
 */
public interface EdgeMatcher {

    boolean matches(@Nonnull Edge edge);
}

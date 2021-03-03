package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-05
 */
public interface HighLevelProjectEventProxy {

    @Nonnull
    ProjectEvent<?> asProjectEvent();

}

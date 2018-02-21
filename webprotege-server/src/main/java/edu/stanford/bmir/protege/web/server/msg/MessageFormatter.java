package edu.stanford.bmir.protege.web.server.msg;

import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 19 Dec 2017
 */
@ProjectSingleton
public class MessageFormatter {

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public MessageFormatter(@Nonnull RenderingManager renderingManager) {
        this.renderingManager = checkNotNull(renderingManager);
    }

    public String format(@Nonnull String template,
                         @Nonnull Object ... objects) {
        return OWLMessageFormatter.formatMessage(checkNotNull(template), renderingManager, checkNotNull(objects));
    }
}

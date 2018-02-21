package edu.stanford.bmir.protege.web.client.perspective;

import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/16
 */
public class PerspectiveFactory {

    @Nonnull
    private final PortletWidgetMapper portletWidgetMapper;

    @Inject
    public PerspectiveFactory(@Nonnull PortletWidgetMapper portletWidgetMapper) {
        this.portletWidgetMapper = portletWidgetMapper;
    }

    /**
     * Create a perspective for the specified perspectiveId.
     * @param perspectiveId The perspectiveId.  Not {@code null}.
     * @return The perspective.
     */
    @Nonnull
    public Perspective createPerspective(@Nonnull PerspectiveId perspectiveId) {
        return new PerspectiveImpl(perspectiveId, portletWidgetMapper);
    }
}

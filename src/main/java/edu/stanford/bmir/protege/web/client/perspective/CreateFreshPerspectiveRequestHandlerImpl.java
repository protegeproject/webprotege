package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.ui.CreateFreshPerspectiveRequestHandler;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public class CreateFreshPerspectiveRequestHandlerImpl implements CreateFreshPerspectiveRequestHandler {

    private static int counter;

    @Override
    public Optional<PerspectiveId> createFreshPerspective() {
        counter++;
        return Optional.of(new PerspectiveId("Perspective-" + counter));
    }
}

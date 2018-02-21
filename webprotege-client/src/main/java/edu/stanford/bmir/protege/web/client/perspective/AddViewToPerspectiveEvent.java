package edu.stanford.bmir.protege.web.client.perspective;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class AddViewToPerspectiveEvent extends Event<AddViewToPerspectiveHandler> {

    private static final Type<AddViewToPerspectiveHandler> TYPE = new Type<>();

    private final PerspectiveId perspectiveId;

    public AddViewToPerspectiveEvent(PerspectiveId perspectiveId) {
        this.perspectiveId = checkNotNull(perspectiveId);
    }

    public static Type<AddViewToPerspectiveHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<AddViewToPerspectiveHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddViewToPerspectiveHandler handler) {
        handler.handleAddViewToPerspective(perspectiveId);
    }
}

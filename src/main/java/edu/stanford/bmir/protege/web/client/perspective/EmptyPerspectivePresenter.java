package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class EmptyPerspectivePresenter {

    private final EmptyPerspectiveView view;

    private final EventBus eventBus;

    private final PerspectiveId perspectiveId;

    @Inject
    public EmptyPerspectivePresenter(@Assisted PerspectiveId perspectiveId, EmptyPerspectiveView view, EventBus eventBus) {
        this.perspectiveId = perspectiveId;
        this.view = view;
        this.eventBus = eventBus;
        view.asWidget().addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                handleAddContent();
            }
        }, ClickEvent.getType());
    }

    public EmptyPerspectiveView getView() {
        return view;
    }

    private void handleAddContent() {
        eventBus.fireEvent(new AddViewToPerspectiveEvent(perspectiveId));
    }
}

package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Mar 2017
 */
public interface Presenter {

    /**
     * Starts this presenter.
     * @param container A container for the view that is associated with the presenter.
     * @param eventBus An event bus that handlers can be attached to for listening to events.
     *                 Any handlers added to this event bus will automatically be removed by
     *                 the caller so it is not necessary for presenters to remove any handlers
     *                 that they add.
     */
    void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus);
}

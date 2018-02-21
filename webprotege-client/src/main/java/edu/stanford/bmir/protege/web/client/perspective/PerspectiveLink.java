package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.HasLabel;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.annotation.Nonnull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/05/2014
 */
public interface PerspectiveLink extends IsWidget, HasLabel, HasClickHandlers {

    PerspectiveId getPerspectiveId();

    void setLabel(@Nonnull String label);

    void addActionHandler(@Nonnull String text, @Nonnull Runnable runnable);
}
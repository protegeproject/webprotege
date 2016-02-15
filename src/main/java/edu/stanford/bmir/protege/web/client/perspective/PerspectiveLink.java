package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasLabel;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/05/2014
 */
public interface PerspectiveLink extends IsWidget, HasLabel, HasClickHandlers {

    PerspectiveId getPerspectiveId();

    void setLabel(String label);

    void addActionHandler(String text, ClickHandler clickHandler);
}
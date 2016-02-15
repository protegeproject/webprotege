package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import edu.stanford.bmir.protege.web.client.perspective.Perspective;
import edu.stanford.bmir.protege.web.shared.HasDispose;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/04/2013
 * <p>
 *     An interface to objects that provide the means to display a project.
 * </p>
 */
public interface ProjectView extends IsWidget, HasDispose, RequiresResize, ProvidesResize, HasSelectionHandlers<Perspective> {

    AcceptsOneWidget getTopBarContainer();

    AcceptsOneWidget getPerspectiveLinkBarViewContainer();

    AcceptsOneWidget getPerspectiveViewContainer();
}

package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public interface MoveToParentView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getHierarchyFieldContainer();

}

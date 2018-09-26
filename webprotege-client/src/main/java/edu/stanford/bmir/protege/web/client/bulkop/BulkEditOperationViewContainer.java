package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public interface BulkEditOperationViewContainer extends IsWidget, HasRequestFocus {

    void setHelpText(@Nonnull String helpText);

    @Nonnull
    AcceptsOneWidget getContainer();
}

package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Dec 2017
 */
public interface CreateEntityDialogView extends IsWidget, HasInitialFocusable {

    @Nonnull
    String getText();

    void clear();
}

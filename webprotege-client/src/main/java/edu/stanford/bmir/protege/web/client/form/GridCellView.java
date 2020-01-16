package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public interface GridCellView extends IsWidget, HasRequestFocus {

    @Nonnull
    AcceptsOneWidget getEditorContainer();

    void requestFocus();

    void setRequiredValueNotPresentVisible(boolean requiredValueNotPresent);
}

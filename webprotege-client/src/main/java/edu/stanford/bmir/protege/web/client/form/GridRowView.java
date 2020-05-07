package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public interface GridRowView extends IsWidget, HasRequestFocus {

    @Nonnull
    GridCellContainer addCell();

    void clear();
}

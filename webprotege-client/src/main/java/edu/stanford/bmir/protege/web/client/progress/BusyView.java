package edu.stanford.bmir.protege.web.client.progress;

import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Mar 2017
 */
public interface BusyView extends IsWidget, HasVisibility {


    void setMessage(@Nonnull String message);

    void clearMessage();
}

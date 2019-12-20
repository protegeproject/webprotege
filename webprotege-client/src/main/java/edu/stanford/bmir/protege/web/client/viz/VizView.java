package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public interface VizView extends IsWidget, AcceptsOneWidget {

    void setDisplaySettingsHandler(Runnable displaySettingsHandler);

}

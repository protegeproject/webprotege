package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent.SelectionChangeHandler;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public interface VizView extends IsWidget {

    interface SettingsChangedHandler {
        void handleSettingsChanged();
    }

    void setRendering(@Nonnull String rendering);

    double getRankSpacing();

    void setSettingsChangedHandler(@Nonnull SettingsChangedHandler handler);
}

package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.graphlib.Graph;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent.SelectionChangeHandler;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public interface VizView extends IsWidget {

    void setGraph(Graph graph);

    void setLoadHandler(Runnable handler);

    boolean isVisible();

    interface SettingsChangedHandler {
        void handleSettingsChanged();
    }

    double getRankSpacing();

    void setSettingsChangedHandler(@Nonnull SettingsChangedHandler handler);

    @Nonnull
    TextMeasurer getTextMeasurer();

    double getScaleFactor();

    void setScaleFactor(double scaleFactor);
}

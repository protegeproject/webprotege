package edu.stanford.bmir.protege.web.client.watches;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
public interface WatchTypeSelectorView extends IsWidget {

    WatchType getSelectedType();

    void setSelectedType(WatchType watchType);

}

package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
public interface ChoiceListSourceDescriptorView extends IsWidget {

    interface SourceTypeChangedHandler {
        void handleSourceTypeChanged();
    }

    void setFixedList(boolean fixedList);

    boolean isFixedList();

    void setSourceTypeChangedHandler(@Nonnull SourceTypeChangedHandler handler);

    @Nonnull
    AcceptsOneWidget getDescriptorContainer();
}

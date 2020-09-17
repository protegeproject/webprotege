package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
public interface EntitySearchFilterTokenFieldView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getTokenFieldContainer();
}

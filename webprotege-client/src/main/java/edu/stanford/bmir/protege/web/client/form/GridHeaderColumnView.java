package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public interface GridHeaderColumnView extends IsWidget {

    void setLabel(@Nonnull String label);

    @Nonnull
    AcceptsOneWidget getSubHeaderContainer();
}

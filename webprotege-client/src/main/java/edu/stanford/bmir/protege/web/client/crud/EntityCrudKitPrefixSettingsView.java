package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public interface EntityCrudKitPrefixSettingsView extends IsWidget {

    void setFallbackPrefix(@Nonnull String fallbackPrefix);

    @Nonnull
    String getFallbackPrefix();

    @Nonnull
    AcceptsOneWidget getCriteriaListContainer();
}

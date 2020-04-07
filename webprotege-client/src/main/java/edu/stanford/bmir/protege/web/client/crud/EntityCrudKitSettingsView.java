package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public interface EntityCrudKitSettingsView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getPrefixSettingsViewContainer();

    @Nonnull
    AcceptsOneWidget getSuffixSettingsViewContainer();

}

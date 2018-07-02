package edu.stanford.bmir.protege.web.client.settings;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Jul 2018
 */
public interface SettingsSectionViewContainer extends IsWidget, AcceptsOneWidget {

    void setSectionName(@Nonnull String sectionName);
}

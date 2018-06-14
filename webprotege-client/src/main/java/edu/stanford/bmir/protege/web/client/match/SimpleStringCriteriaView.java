package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public interface SimpleStringCriteriaView extends IsWidget {

    @Nonnull
    String getValue();

    void setValue(@Nonnull String value);

    boolean isIgnoreCase();

    void setIgnoreCase(boolean ignoreCase);
}

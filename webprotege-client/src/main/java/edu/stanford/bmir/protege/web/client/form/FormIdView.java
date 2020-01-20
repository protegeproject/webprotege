package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-17
 */
public interface FormIdView extends IsWidget {

    void setFormDisplayName(@Nonnull String displayName);
}

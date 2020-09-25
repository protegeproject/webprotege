package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-23
 */
public interface CreateEntitiesFormView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getFormContainer();
}

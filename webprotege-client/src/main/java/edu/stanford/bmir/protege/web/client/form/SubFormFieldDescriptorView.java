package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public interface SubFormFieldDescriptorView extends IsWidget {

    void clear();

    @Nonnull
    AcceptsOneWidget getSubFormContainer();
}

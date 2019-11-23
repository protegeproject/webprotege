package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface FormElementDescriptorViewHolder extends IsWidget, AcceptsOneWidget {

    void setMoveDownHandler(@Nonnull Runnable handler);

    void setMoveUpHandler(@Nonnull Runnable handler);

    void setRemoveHandler(@Nonnull Runnable handler);

    void setNumber(int number);

    void scrollIntoView();
}

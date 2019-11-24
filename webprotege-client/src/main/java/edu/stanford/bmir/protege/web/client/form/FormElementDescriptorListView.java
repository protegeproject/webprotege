package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public interface FormElementDescriptorListView extends IsWidget {

    void clear();

    void addView(@Nonnull FormElementDescriptorViewHolder view);

    void performDeleteElementConfirmation(FormElementId formElementId,
                                          @Nonnull Runnable deleteRunnable);

    void removeView(@Nonnull FormElementDescriptorViewHolder viewHolder);

    void moveUp(@Nonnull FormElementDescriptorViewHolder viewHolder);

    void moveDown(@Nonnull FormElementDescriptorViewHolder viewHolder);
}

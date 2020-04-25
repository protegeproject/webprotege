package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.HasFormRegionPagedChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FormControl extends HasRequestFocus, ValueEditor<FormControlData>, HasFormRegionPagedChangedHandler, HasEnabled {

    void setEnabled(boolean enabled);

    default void setRegionPosition(@Nonnull FormRegionPosition position) {

    }

    @Nonnull
    default ImmutableList<FormRegionPageRequest> getPageRequests(@Nonnull FormSubject formSubject,
                                                                 @Nonnull FormRegionId formRegionId) {
        return ImmutableList.of();
    }

    @Override
    default void setFormRegionPageChangedHandler(@Nonnull FormRegionPageChangedHandler handler) {

    }
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.RegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.HasFormRegionPagedChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.FormRegionFilter;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FormControl extends HasRequestFocus, HasFormRegionPagedChangedHandler, HasFormRegionFilterChangedHandler, HasEnabled, HasValueChangeHandlers<Optional<FormControlData>>, IsWidget {

    void setEnabled(boolean enabled);

    default void setPosition(@Nonnull FormRegionPosition position) {

    }

    @Nonnull
    default ImmutableList<FormRegionPageRequest> getPageRequests(@Nonnull FormSubject formSubject,
                                                                 @Nonnull FormRegionId formRegionId) {
        return ImmutableList.of();
    }

    @Override
    default void setRegionPageChangedHandler(@Nonnull RegionPageChangedHandler handler) {

    }

    void setValue(@Nonnull FormControlDataDto value);

    Optional<FormControlData> getValue();

    void clearValue();

    @Nonnull
    ImmutableSet<FormRegionFilter> getFilters();
}

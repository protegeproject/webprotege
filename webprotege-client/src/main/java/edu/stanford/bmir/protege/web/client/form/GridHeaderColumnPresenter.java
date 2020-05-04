package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderColumnPresenter implements HasVisibility {

    @Nonnull
    private final GridHeaderColumnView view;

    @Nonnull
    private final LanguageMapCurrentLocaleMapper localeMapper;

    @Inject
    public GridHeaderColumnPresenter(@Nonnull GridHeaderColumnView view,
                                     @Nonnull LanguageMapCurrentLocaleMapper localeMapper) {
        this.view = checkNotNull(view);
        this.localeMapper = checkNotNull(localeMapper);
    }

    public void setColumnDescriptor(@Nonnull GridColumnDescriptor columnDescriptor) {
        String label = localeMapper.getValueForCurrentLocale(columnDescriptor.getLabel());
        view.setLabel(label);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public boolean isVisible() {
        return view.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        view.setVisible(visible);
    }
}

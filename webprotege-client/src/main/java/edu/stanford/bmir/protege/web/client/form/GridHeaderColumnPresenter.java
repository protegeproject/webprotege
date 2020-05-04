package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasVisibility;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderColumnPresenter implements HasVisibility {

    @Nonnull
    private final GridHeaderCellView view;

    @Nonnull
    private final LanguageMapCurrentLocaleMapper localeMapper;

    @Inject
    public GridHeaderColumnPresenter(@Nonnull GridHeaderCellView view,
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

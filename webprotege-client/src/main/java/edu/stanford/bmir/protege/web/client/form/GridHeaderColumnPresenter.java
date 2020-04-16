package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderColumnPresenter {

    @Nonnull
    private final GridHeaderColumnView view;

    @Nonnull
    private final Provider<GridHeaderPresenter> gridHeaderPresenterProvider;

    @Inject
    public GridHeaderColumnPresenter(@Nonnull GridHeaderColumnView view,
                                     @Nonnull Provider<GridHeaderPresenter> gridHeaderPresenterProvider) {
        this.view = checkNotNull(view);
        this.gridHeaderPresenterProvider = checkNotNull(gridHeaderPresenterProvider);
    }
    
    public void setColumnDescriptor(@Nonnull GridColumnDescriptor columnDescriptor) {
        FormControlDescriptor formControlDescriptor = columnDescriptor.getFormControlDescriptor();
        if(formControlDescriptor instanceof GridControlDescriptor) {
            ImmutableList<GridColumnDescriptor> columnDescriptors = ((GridControlDescriptor) formControlDescriptor).getColumns();
            // Hoist up?
            GridHeaderPresenter gridHeaderPresenter = gridHeaderPresenterProvider.get();
            gridHeaderPresenter.start(view.getSubHeaderContainer());
            gridHeaderPresenter.setColumns(columnDescriptors);
        }
        else {
            LocaleInfo currentLocale = LocaleInfo.getCurrentLocale();
            String localeName = currentLocale.getLocaleName();
            view.setLabel(columnDescriptor.getLabel().get(localeName));
        }
        // TODO: Width etc
    }

    @Nonnull
    public IsWidget getView() {
        return view;
    }
}

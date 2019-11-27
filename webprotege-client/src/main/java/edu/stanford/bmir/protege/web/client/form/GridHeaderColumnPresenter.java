package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderColumnPresenter {

    @Nonnull
    private final GridHeaderColumnView view;

    @Inject
    public GridHeaderColumnPresenter(@Nonnull GridHeaderColumnView view) {
        this.view = checkNotNull(view);
    }

    public void setColumnDescriptor(@Nonnull GridColumnDescriptor columnDescriptor) {
        LocaleInfo currentLocale = LocaleInfo.getCurrentLocale();
        String localeName = currentLocale.getLocaleName();
        view.setLabel(columnDescriptor.getLabel().get(localeName));
        // TODO: Width etc
    }

    @Nonnull
    public IsWidget getView() {
        return view;
    }
}

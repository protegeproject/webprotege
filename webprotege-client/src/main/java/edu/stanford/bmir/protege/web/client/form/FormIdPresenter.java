package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-17
 */
public class FormIdPresenter {

    @Nonnull
    private final FormIdView view;

    @Nonnull
    private Optional<FormDescriptor> currentDescriptor = Optional.empty();

    @Inject
    public FormIdPresenter(@Nonnull FormIdView view) {
        this.view = checkNotNull(view);
    }

    public IsWidget getView() {
        return view;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setFormDescriptor(@Nonnull FormDescriptor descriptor) {
        this.currentDescriptor = Optional.of(descriptor);
        String displayName = getDisplayName(descriptor);
        if(displayName.isEmpty()) {
            view.setFormDisplayName(descriptor.getFormId().getId());
        }
        else {
            view.setFormDisplayName(displayName);
        }
    }

    private String getDisplayName(@Nonnull FormDescriptor descriptor) {
        LanguageMap label = descriptor.getLabel();
        LocaleInfo currentLocale = LocaleInfo.getCurrentLocale();
        String lang = currentLocale.getLocaleName();
        return label.get(lang);
    }

    public String getDisplayName() {
        return currentDescriptor.map(this::getDisplayName).orElse("");
    }

    public Optional<FormId> getFormId() {
        return currentDescriptor.map(FormDescriptor::getFormId);
    }
}

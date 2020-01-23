package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-20
 */
public class FormStackPresenter {

    @Nonnull
    private final FormStackView view;

    private final NoFormView noFormView;

    private final List<FormPresenter> formPresenters = new ArrayList<>();

    @Nonnull
    private final Provider<FormPresenter> formPresenterProvider;

    private Optional<AcceptsOneWidget> container = Optional.empty();

    @Inject
    public FormStackPresenter(@Nonnull FormStackView view,
                              @Nonnull NoFormView noFormView,
                              @Nonnull Provider<FormPresenter> formPresenterProvider) {
        this.view = checkNotNull(view);
        this.noFormView = checkNotNull(noFormView);
        this.formPresenterProvider = checkNotNull(formPresenterProvider);
    }

    public void clearForms() {
        GWT.log("[FormStackPresenter] CLEAR");
        formPresenters.clear();
        updateView();
    }

    @Nonnull
    public ImmutableList<FormData> getForms() {
        return formPresenters.stream()
                             .map(FormPresenter::getFormData)
                             .filter(Optional::isPresent)
                             .map(Optional::get)
                             .collect(toImmutableList());
    }

    public void setForms(@Nonnull ImmutableList<FormData> forms) {
        List<FormDescriptor> currentFormDescriptors = formPresenters.stream()
                                                     .map(p -> p.getFormData()
                                                                .map(FormData::getFormDescriptor))
                                                     .filter(Optional::isPresent)
                                                     .map(Optional::get)
                                                     .collect(Collectors.toList());
        List<FormDescriptor> nextFormDescriptors = forms.stream()
                .map(FormData::getFormDescriptor)
                .collect(Collectors.toList());
        if(currentFormDescriptors.equals(nextFormDescriptors)) {
            for(int i = 0; i < forms.size(); i++) {
                formPresenters.get(i).displayForm(forms.get(i));
            }
        }
        else {
            formPresenters.clear();
            view.clear();
            forms.forEach(formData -> {
                FormPresenter formPresenter = formPresenterProvider.get();
                formPresenter.start(view.addContainer());
                formPresenter.displayForm(formData);
                // TODO : Changes?
                formPresenters.add(formPresenter);
            });
        }
        updateView();
    }

    private void updateView() {
        if(formPresenters.isEmpty()) {
            container.ifPresent(c -> c.setWidget(noFormView));
        }
        else {
            container.ifPresent(c -> c.setWidget(view));
        }
    }

    public boolean isDirty() {
        return formPresenters.stream()
                             .anyMatch(FormPresenter::isDirty);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        this.container = Optional.of(container);
        container.setWidget(view);
        updateView();
    }
}

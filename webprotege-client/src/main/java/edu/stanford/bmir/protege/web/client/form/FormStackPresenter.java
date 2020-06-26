package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataDto;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlOrdering;

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
    private final FormTabBarPresenter formTabBarPresenter;

    @Nonnull
    private final FormStackView view;

    private final NoFormView noFormView;

    private final List<FormPresenter> formPresenters = new ArrayList<>();

    @Nonnull
    private final Provider<FormPresenter> formPresenterProvider;

    private Optional<AcceptsOneWidget> container = Optional.empty();

    @Nonnull
    private FormRegionPageChangedHandler formRegionPageChangedHandler = () -> {};

    private boolean enabled = true;

    @Nonnull
    private GridOrderByChangedHandler orderByChangedHandler = () -> {};

    @Inject
    public FormStackPresenter(@Nonnull FormTabBarPresenter formTabBarPresenter,
                              @Nonnull FormStackView view,
                              @Nonnull NoFormView noFormView,
                              @Nonnull Provider<FormPresenter> formPresenterProvider) {
        this.formTabBarPresenter = checkNotNull(formTabBarPresenter);
        this.view = checkNotNull(view);
        this.noFormView = checkNotNull(noFormView);
        this.formPresenterProvider = checkNotNull(formPresenterProvider);
    }

    public void clearForms() {
        formPresenters.clear();
        formTabBarPresenter.clear();
        updateView();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        formPresenters.forEach(formPresenter -> formPresenter.setEnabled(enabled));
    }

    public void setFormRegionPageChangedHandler(@Nonnull FormRegionPageChangedHandler handler) {
        this.formRegionPageChangedHandler = checkNotNull(handler);
        formPresenters.forEach(formPresenter -> formPresenter.setFormRegionPageChangedHandler(handler));
    }

    public void setGridOrderByChangedHandler(@Nonnull GridOrderByChangedHandler handler) {
        this.orderByChangedHandler = checkNotNull(handler);
        formPresenters.forEach(p -> p.setGridOrderByChangedHandler(handler));
    }

    @Nonnull
    public ImmutableList<FormPageRequest> getPageRequests() {
        return formPresenters.stream()
                      .map(FormPresenter::getPageRequest)
                      .flatMap(ImmutableList::stream)
                      .collect(toImmutableList());
    }

    @Nonnull
    public ImmutableList<FormData> getForms() {
        return formPresenters.stream()
                             .map(FormPresenter::getFormData)
                             .filter(Optional::isPresent)
                             .map(Optional::get)
                             .collect(toImmutableList());
    }

    public void expandAllFields() {
        formPresenters.forEach(FormPresenter::expandAll);
    }

    public void collapseAllFields() {
        formPresenters.forEach(FormPresenter::collapseAll);
    }

    public void setForms(@Nonnull ImmutableList<FormDataDto> forms) {
        List<FormDescriptor> currentFormDescriptors = formPresenters.stream()
                                                     .map(p -> p.getFormData()
                                                                .map(FormData::getFormDescriptor))
                                                     .filter(Optional::isPresent)
                                                     .map(Optional::get)
                                                     .collect(Collectors.toList());
        List<FormDescriptor> nextFormDescriptors = forms.stream()
                .map(FormDataDto::getFormDescriptor)
                .collect(Collectors.toList());
        if(currentFormDescriptors.equals(nextFormDescriptors)) {
            for(int i = 0; i < forms.size(); i++) {
                formPresenters.get(i).displayForm(forms.get(i));
            }
        }
        else {
            formPresenters.clear();
            formTabBarPresenter.clear();
            view.clear();
            forms.forEach(formData -> {
                FormPresenter formPresenter = formPresenterProvider.get();
                FormDescriptor formDescriptor = formData.getFormDescriptor();
                FormContainer formContainer = view.addContainer(formDescriptor.getLabel());
                formPresenter.start(formContainer);
                formPresenter.setFormRegionPageChangedHandler(formRegionPageChangedHandler);
                formPresenter.setGridOrderByChangedHandler(orderByChangedHandler);
                formPresenter.displayForm(formData);
                formPresenter.setEnabled(enabled);
                formPresenters.add(formPresenter);
                formTabBarPresenter.addForm(formDescriptor.getFormId(),
                                            formDescriptor.getLabel(),
                                            formContainer);
            });
            formTabBarPresenter.restoreSelection();
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
        return false;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        this.container = Optional.of(container);
        container.setWidget(view);
        formTabBarPresenter.start(view.getSelectorContainer());
        updateView();
    }

    public void setSelectedFormIdStash(@Nonnull SelectedFormIdStash formIdStash) {
        formTabBarPresenter.setSelectedFormIdStash(formIdStash);
    }

    @Nonnull
    public ImmutableList<GridControlOrdering> getGridControlOrderings() {
        return formPresenters.stream()
                      .flatMap(FormPresenter::getGridControlOrderings)
                      .collect(toImmutableList());
    }
}

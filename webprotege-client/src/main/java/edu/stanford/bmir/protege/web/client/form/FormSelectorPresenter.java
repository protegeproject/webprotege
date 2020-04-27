package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class FormSelectorPresenter {

    @Nonnull
    private final List<FormSelectorItemPresenter> itemPresenters = new ArrayList<>();

    @Nonnull
    private final FormSelectorView view;

    private final FormSelectorItemPresenterFactory formSelectorItemPresenterFactory;

    @Nonnull
    private Optional<SelectedFormIdStash> selectedFormIdStash = Optional.empty();

    @Inject
    public FormSelectorPresenter(@Nonnull FormSelectorView view,
                                 FormSelectorItemPresenterFactory formSelectorItemPresenterFactory) {
        this.view = checkNotNull(view);
        this.formSelectorItemPresenterFactory = checkNotNull(formSelectorItemPresenterFactory);
    }

    public void clear() {
        itemPresenters.clear();
        view.clear();
    }

    public void setSelectedFormIdStash(@Nonnull SelectedFormIdStash selectedFormIdStash) {
        this.selectedFormIdStash = Optional.of(selectedFormIdStash);
        selectedFormIdStash.getSelectedForm().ifPresent(this::restoreFormSelection);
    }

    private void restoreFormSelection(FormId formId) {
        setSelected(formId);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        restoreSelection();
    }

    public void addForm(@Nonnull FormId formId,
                        @Nonnull LanguageMap label,
                        @Nonnull FormContainer formContainer) {
        FormSelectorItemPresenter itemPresenter = formSelectorItemPresenterFactory.create(formId);
        itemPresenters.add(itemPresenter);
        AcceptsOneWidget itemContainer = view.addFormSelectorItem();
        itemPresenter.start(itemContainer);
        itemPresenter.setLabel(label);
        itemPresenter.setFormContainer(formContainer);
        itemPresenter.setClickHandler(event -> selectFormAndStashId(formId));
    }

    public void selectFormAndStashId(@Nonnull FormId formId) {
        setSelected(formId);
        selectedFormIdStash.ifPresent(stash -> stash.stashSelectedForm(formId));
    }

    private void setSelected(FormId formId) {
        for(FormSelectorItemPresenter ip : itemPresenters) {
            boolean selected = ip.getFormId().equals(formId);
            ip.setSelected(selected);
        }
    }

    public void restoreSelection() {
        this.selectedFormIdStash.ifPresent(stash -> {
            Optional<FormId> selectedForm = stash.getSelectedForm();
            selectedForm.ifPresent(this::restoreFormSelection);
            if(!selectedForm.isPresent()) {
                restoreSelection();
            }
        });
        if(!selectedFormIdStash.isPresent()) {
            setFirstFormSelected();
        }
    }

    public void setFirstFormSelected() {
        itemPresenters.stream()
                      .findFirst()
                      .map(FormSelectorItemPresenter::getFormId)
                      .ifPresent(this::setSelected);
    }
}

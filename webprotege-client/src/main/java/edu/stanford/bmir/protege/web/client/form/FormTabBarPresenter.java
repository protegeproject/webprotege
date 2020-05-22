package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class FormTabBarPresenter {

    @Nonnull
    private final List<FormTabPresenter> itemPresenters = new ArrayList<>();

    @Nonnull
    private final FormTabBarView view;

    private final FormTabPresenterFactory tabPresenterFactory;

    @Nonnull
    private Optional<SelectedFormIdStash> selectedFormIdStash = Optional.empty();

    @Inject
    public FormTabBarPresenter(@Nonnull FormTabBarView view,
                               FormTabPresenterFactory tabPresenterFactory) {
        this.view = checkNotNull(view);
        this.tabPresenterFactory = checkNotNull(tabPresenterFactory);
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
        FormTabPresenter tabPresenter = tabPresenterFactory.create(formId);
        itemPresenters.add(tabPresenter);
        view.addView(tabPresenter.getView());
        tabPresenter.setLabel(label);
        tabPresenter.setFormContainer(formContainer);
        tabPresenter.setClickHandler(event -> selectFormAndStashId(formId));
    }

    public void selectFormAndStashId(@Nonnull FormId formId) {
        setSelected(formId);
        selectedFormIdStash.ifPresent(stash -> stash.stashSelectedForm(formId));
    }

    private void setSelected(FormId formId) {
        for(FormTabPresenter ip : itemPresenters) {
            boolean selected = ip.getFormId().equals(formId);
            ip.setSelected(selected);
        }
    }

    public void restoreSelection() {
        this.selectedFormIdStash.ifPresent(stash -> {
            Optional<FormId> selectedForm = stash.getSelectedForm();
            selectedForm.ifPresent(this::restoreFormSelection);
            if(!selectedForm.isPresent()) {
                setFirstFormSelected();
            }
        });
        if(!selectedFormIdStash.isPresent()) {
            setFirstFormSelected();
        }
    }

    public void setFirstFormSelected() {
        itemPresenters.stream()
                      .findFirst()
                      .map(FormTabPresenter::getFormId)
                      .ifPresent(this::setSelected);
    }
}

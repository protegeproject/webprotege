package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

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

    private final Provider<FormSelectorItemPresenter> formSelectorItemPresenterProvider;

    @Inject
    public FormSelectorPresenter(@Nonnull FormSelectorView view,
                                 Provider<FormSelectorItemPresenter> formSelectorItemPresenterProvider) {
        this.view = checkNotNull(view);
        this.formSelectorItemPresenterProvider = checkNotNull(formSelectorItemPresenterProvider);
    }

    public void clear() {
        itemPresenters.clear();
        view.clear();
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void addForm(@Nonnull FormId formId,
                        @Nonnull LanguageMap label,
                        @Nonnull FormContainer formContainer) {
        FormSelectorItemPresenter itemPresenter = formSelectorItemPresenterProvider.get();
        itemPresenters.add(itemPresenter);
        AcceptsOneWidget itemContainer = view.addFormSelectorItem();
        itemPresenter.start(itemContainer);
        itemPresenter.setLabel(label);
        itemPresenter.setFormContainer(formContainer);
        itemPresenter.setClickHandler(event -> {
            setSelected(itemPresenter);
        });
    }

    private void setSelected(FormSelectorItemPresenter itemPresenter) {
        for(FormSelectorItemPresenter ip : itemPresenters) {
            boolean selected = ip == itemPresenter;
            ip.setSelected(selected);
        }
    }

    public void setFirstFormSelected() {
        itemPresenters.stream()
                      .findFirst()
                      .ifPresent(this::setSelected);
    }
}

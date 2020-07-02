package edu.stanford.bmir.protege.web.client.form;


import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 *
 * Presents a form
 */
public class FormTabPresenter {

    @Nonnull
    private final FormId formId;

    @Nonnull
    private final FormTabView view;

    private boolean selected = false;

    @Nonnull
    private Optional<FormContainer> formContainer = Optional.empty();

    @AutoFactory
    @Inject
    public FormTabPresenter(@Nonnull FormId formId,
                            @Provided @Nonnull FormTabView view) {
        this.formId = checkNotNull(formId);
        this.view = checkNotNull(view);
    }

    @Nonnull
    public FormId getFormId() {
        return formId;
    }

    public void setFormContainer(@Nonnull FormContainer formContainer) {
        this.formContainer = Optional.of(checkNotNull(formContainer));
    }

    public void setLabel(LanguageMap label) {
        view.setLabel(label);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        view.setSelected(selected);
        formContainer.ifPresent(c -> c.setVisible(selected));
    }

    public boolean isSelected() {
        return selected;
    }

    public void setClickHandler(@Nonnull ClickHandler clickHandler) {
        view.setClickHandler(clickHandler);
    }

    @Nonnull
    public FormTabView getView() {
        return view;
    }
}

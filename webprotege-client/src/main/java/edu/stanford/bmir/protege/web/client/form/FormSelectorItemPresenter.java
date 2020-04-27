package edu.stanford.bmir.protege.web.client.form;


import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class FormSelectorItemPresenter {

    @Nonnull
    private final FormSelectorItemView view;

    private boolean selected = false;

    @Nonnull
    private Optional<FormContainer> formContainer = Optional.empty();

    @Inject
    public FormSelectorItemPresenter(@Nonnull FormSelectorItemView view) {
        this.view = checkNotNull(view);
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

    public void start(AcceptsOneWidget itemContainer) {
        itemContainer.setWidget(view);
    }
}

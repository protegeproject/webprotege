package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.ObjectPresenter;
import edu.stanford.bmir.protege.web.shared.crud.ConditionalIriPrefix;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-07
 */
public class ConditionalIriPrefixPresenter implements ObjectPresenter<ConditionalIriPrefix> {

    @Nonnull
    private final ConditionalIriPrefixView view;

    @Inject
    public ConditionalIriPrefixPresenter(@Nonnull ConditionalIriPrefixView view) {
        this.view = checkNotNull(view);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void setValue(@Nonnull ConditionalIriPrefix value) {
        view.setIriPrefix(value.getIriPrefix());
    }

    @Nonnull
    @Override
    public Optional<ConditionalIriPrefix> getValue() {
        return Optional.of(ConditionalIriPrefix.get(view.getIriPrefix()));
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        return "Conditions";
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {

    }
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public interface ObjectPresenter<T> {

    void start(@Nonnull AcceptsOneWidget container);

    void setValue(@Nonnull T value);

    @Nonnull
    Optional<T> getValue();

    @Nonnull
    String getHeaderLabel();

    void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler);
}

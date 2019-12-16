package edu.stanford.bmir.protege.web.client.library.tokenfield;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public class TokenPresenter<T> {

    @Nonnull
    private final T tokenObject;

    @Nonnull
    private final TokenView view;


    public TokenPresenter(@Nonnull T tokenObject,
                          @Nonnull TokenView view) {
        this.tokenObject = checkNotNull(tokenObject);
        this.view = checkNotNull(view);
    }

    @Nonnull
    public IsWidget getView() {
        return view;
    }

    @Nonnull
    public T getTokenObject() {
        return tokenObject;
    }

    public void setLabel(@Nonnull String label) {
        view.setLabel(label);
    }

    public void setRemoveTokenRequestHandler(@Nonnull TokenView.RemoveTokenRequestHandler handler) {
        view.setRemoveTokenRequestHandler(handler);
    }
}

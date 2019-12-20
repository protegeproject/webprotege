package edu.stanford.bmir.protege.web.client.library.tokenfield;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public class TokenPresenterFactory {

    @Nonnull
    private final Provider<TokenView> tokenViewProvider;

    @Inject
    public TokenPresenterFactory(@Nonnull Provider<TokenView> tokenViewProvider) {
        this.tokenViewProvider = checkNotNull(tokenViewProvider);
    }

    @Nonnull
    public <T> TokenPresenter<T> create(@Nonnull T tokenObject) {
        return new TokenPresenter<>(tokenObject, tokenViewProvider.get());
    }
}

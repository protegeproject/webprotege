package edu.stanford.bmir.protege.web.client.library.tokenfield;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public class TokenFieldPresenter<T> {

    public interface TokensChangedHandler<T> {
        void handleTokensChanged(@Nonnull List<T> tokens);
    }

    private final TokenFieldView view;

    @Nonnull
    private final TokenPresenterFactory tokenPresenterFactory;

    private final List<TokenPresenter<T>> tokenPresenters = new ArrayList<>();

    private TokensChangedHandler<T> tokensChangedHandler = (list) -> {};

    private AddTokenPrompt<T> addTokenPrompt = (event, callback) -> {};


    @Inject
    public TokenFieldPresenter(@Nonnull TokenFieldView view,
                               @Nonnull TokenPresenterFactory tokenPresenterFactory) {
        this.view = checkNotNull(view);
        this.tokenPresenterFactory = tokenPresenterFactory;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setAddTokenHandler((event) -> {
            updatePlaceholder();
            addTokenPrompt.displayAddTokenPrompt(event, this::handleAddToken);
        });
    }

    public void setTokensChangedHandler(@Nonnull TokensChangedHandler<T> tokensChangedHandler) {
        this.tokensChangedHandler = checkNotNull(tokensChangedHandler);
    }

    public void setPlaceholder(String placeholder) {
        view.setPlaceholder(checkNotNull(placeholder));
    }

    public void clear() {
        view.clear();
        tokenPresenters.clear();
        updatePlaceholder();
    }

    public void setAddTokenPrompt(@Nonnull AddTokenPrompt<T> addTokenPrompt) {
        this.addTokenPrompt = checkNotNull(addTokenPrompt);
    }

    @Nonnull
    public List<T> getTokenObjects() {
        return tokenPresenters.stream()
                              .map(TokenPresenter::getTokenObject)
                              .collect(toImmutableList());
    }


    public void addToken(T token, String label) {
        TokenPresenter<T> tokenPresenter = tokenPresenterFactory.create(token);
        tokenPresenter.setLabel(label);
        tokenPresenter.setRemoveTokenRequestHandler(() -> {
            view.remove(tokenPresenter.getView());
            tokenPresenters.remove(tokenPresenter);
            updatePlaceholder();
            tokensChangedHandler.handleTokensChanged(getTokenObjects());
        });
        tokenPresenters.add(tokenPresenter);
        view.add(tokenPresenter.getView());
        updatePlaceholder();
    }

    private void handleAddToken(T tokenObject, String label) {
        addToken(tokenObject, label);
        tokensChangedHandler.handleTokensChanged(getTokenObjects());
        updatePlaceholder();
    }

    private void updatePlaceholder() {
        view.setPlaceholderVisible(getTokenObjects().isEmpty());
    }


}

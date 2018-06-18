package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralLexicalValueNotInDatatypeLexicalSpaceCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class LexicalValueIsNotInLiteralLexcialSpaceCriteriaPresenterFactory implements CriteriaPresenterFactory<LiteralLexicalValueNotInDatatypeLexicalSpaceCriteria> {

    @Nonnull
    private final Provider<LexicalValueIsNotInLiteralLexcialSpaceCriteriaPresenter> presenterProvider;

    @Inject
    public LexicalValueIsNotInLiteralLexcialSpaceCriteriaPresenterFactory(@Nonnull Provider<LexicalValueIsNotInLiteralLexcialSpaceCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "is malformed";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<LiteralLexicalValueNotInDatatypeLexicalSpaceCriteria> createPresenter() {
        return presenterProvider.get();
    }
}

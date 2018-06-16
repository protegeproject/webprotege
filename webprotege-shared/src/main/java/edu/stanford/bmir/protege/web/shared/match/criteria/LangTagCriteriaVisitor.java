package edu.stanford.bmir.protege.web.shared.match.criteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
public interface LangTagCriteriaVisitor<R> {

    R visit(@Nonnull LangTagMatchesCriteria criteria);

    R visit(@Nonnull LangTagIsEmptyCriteria criteria);

    R visit(@Nonnull AnyLangTagOrEmptyLangTagCriteria criteria);
}

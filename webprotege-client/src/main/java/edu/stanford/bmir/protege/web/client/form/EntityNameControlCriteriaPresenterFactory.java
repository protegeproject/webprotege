package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataMatchCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-18
 */
public class EntityNameControlCriteriaPresenterFactory implements CriteriaPresenterFactory<PrimitiveFormControlDataMatchCriteria> {

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Name is ";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends PrimitiveFormControlDataMatchCriteria> createPresenter() {
        return null;
    }
}

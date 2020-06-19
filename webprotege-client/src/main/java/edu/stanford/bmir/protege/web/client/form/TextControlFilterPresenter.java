package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.AnnotationValueCriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.LiteralCriteriaListPresenter;
import edu.stanford.bmir.protege.web.shared.form.data.CompositePrimitiveFormControlDataMatchCriteria;
import edu.stanford.bmir.protege.web.shared.form.data.LiteralFormControlDataMatchCriteria;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataMatchCriteria;
import edu.stanford.bmir.protege.web.shared.form.field.TextControlDescriptorDto;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeLiteralCriteria;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
public class TextControlFilterPresenter implements FormControlFilterPresenter {

    @Nonnull
    private final TextControlFilterView view;

    @Nonnull
    private TextControlDescriptorDto descriptor;

    @Nonnull
    private final LiteralCriteriaListPresenter criteriaPresenter;


    @AutoFactory
    public TextControlFilterPresenter(@Provided @Nonnull TextControlFilterView view,
                                      @Nonnull TextControlDescriptorDto descriptor,
                                      @Provided @Nonnull LiteralCriteriaListPresenter criteriaPresenter) {
        this.view = checkNotNull(view);
        this.descriptor = descriptor;
        this.criteriaPresenter = criteriaPresenter;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        criteriaPresenter.start(view.getContainer());
    }

    @Nonnull
    @Override
    public Optional<PrimitiveFormControlDataMatchCriteria> getFilter() {
        criteriaPresenter.getCriteria()
                         .map(c -> {
                             ImmutableList<LiteralFormControlDataMatchCriteria> criteria = c.getCriteria()
                                                                                           .stream()
                                                                                           .map(LiteralFormControlDataMatchCriteria::get)
                                                                                           .collect(toImmutableList());
                             return CompositePrimitiveFormControlDataMatchCriteria.get(criteria, c.getMultiMatchType());
                         });
        return Optional.empty();
    }
}

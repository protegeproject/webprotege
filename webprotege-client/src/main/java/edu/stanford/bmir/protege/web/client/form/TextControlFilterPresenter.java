package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.AnnotationValueCriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.LiteralCriteriaListPresenter;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.TextControlDescriptorDto;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeLiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteriaVisitor;

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
        return criteriaPresenter.getCriteria()
                         .map(LiteralFormControlDataMatchCriteria::get);
    }

    @Override
    public void setFilter(@Nonnull FormRegionFilter filter) {
        PrimitiveFormControlDataMatchCriteria matchCriteria = filter.getMatchCriteria();
        matchCriteria.accept(new PrimitiveFormControlDataMatchCriteriaVisitor<Object>() {
            @Override
            public Object visit(EntityFormControlDataMatchCriteria entityFormControlDataMatchCriteria) {
                return null;
            }

            @Override
            public Object visit(LiteralFormControlDataMatchCriteria literalFormControlDataMatchCriteria) {
                LiteralCriteria lexicalValueCriteria = literalFormControlDataMatchCriteria.getLexicalValueCriteria();
                lexicalValueCriteria.accept(new LiteralCriteriaVisitor<Object>() {
                    @Override
                    public Object visit(CompositeLiteralCriteria compositeLiteralCriteria) {
                        criteriaPresenter.setCriteria(compositeLiteralCriteria);
                        return null;
                    }
                });
                return null;
            }

            @Override
            public Object visit(CompositePrimitiveFormControlDataMatchCriteria compositePrimitiveFormControlDataMatchCriteria) {
                return null;
            }
        });
    }

    @Override
    public void clear() {
        criteriaPresenter.clear();
    }
}

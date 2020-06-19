package edu.stanford.bmir.protege.web.shared.form.data;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
public interface PrimitiveFormControlDataMatchCriteriaVisitor<R> {

    R visit(EntityFormControlDataMatchCriteria entityFormControlDataMatchCriteria);

    R visit(LiteralFormControlDataMatchCriteria literalFormControlDataMatchCriteria);

    R visit(CompositePrimitiveFormControlDataMatchCriteria compositePrimitiveFormControlDataMatchCriteria);
}

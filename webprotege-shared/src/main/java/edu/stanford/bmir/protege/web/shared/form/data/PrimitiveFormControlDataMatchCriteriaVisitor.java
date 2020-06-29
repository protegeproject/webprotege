package edu.stanford.bmir.protege.web.shared.form.data;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
public interface PrimitiveFormControlDataMatchCriteriaVisitor<R> {

    default R visit(EntityFormControlDataMatchCriteria entityFormControlDataMatchCriteria) {
        return null;
    };

    default R visit(LiteralFormControlDataMatchCriteria literalFormControlDataMatchCriteria) {
        return null;
    };

    default R visit(CompositePrimitiveFormControlDataMatchCriteria compositePrimitiveFormControlDataMatchCriteria) {
        return null;
    };
}

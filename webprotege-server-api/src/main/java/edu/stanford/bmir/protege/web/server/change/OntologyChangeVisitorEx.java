package edu.stanford.bmir.protege.web.server.change;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-27
 */
public interface OntologyChangeVisitorEx<R> {

    default R getDefaultReturnValue() {
        return null;
    }

    default R visit(@Nonnull AddAxiomChange addAxiomChange) {
        return getDefaultReturnValue();
    }

    default R visit(@Nonnull RemoveAxiomChange removeAxiomChange) {
        return getDefaultReturnValue();
    }

    default R visit(@Nonnull AddOntologyAnnotationChange addOntologyAnnotationChange) {
        return getDefaultReturnValue();
    }

    default R visit(@Nonnull RemoveOntologyAnnotationChange removeOntologyAnnotationChange) {
        return getDefaultReturnValue();
    }

    default R visit(@Nonnull AddImportChange addImportChange) {
        return getDefaultReturnValue();
    }

    default R visit(@Nonnull RemoveImportChange removeImportChange) {
        return getDefaultReturnValue();
    }
}

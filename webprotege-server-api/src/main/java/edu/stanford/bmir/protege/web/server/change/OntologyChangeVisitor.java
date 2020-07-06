package edu.stanford.bmir.protege.web.server.change;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-27
 */
public interface OntologyChangeVisitor {

    default void visit(@Nonnull AddAxiomChange addAxiomChange) {}

    default void visit(@Nonnull RemoveAxiomChange removeAxiomChange) {}

    default void visit(@Nonnull AddOntologyAnnotationChange addOntologyAnnotationChange) {}

    default void visit(@Nonnull RemoveOntologyAnnotationChange removeOntologyAnnotationChange) {}

    default void visit(@Nonnull AddImportChange addImportChange) {}

    default void visit(@Nonnull RemoveImportChange removeImportChange) {}
}

package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class OntologyChangeComparator implements Comparator<OntologyChange> {

    public static final int AXIOM_CHANGE = 0;

    public static final int ONTOLOGY_ANNOTATION_CHANGE = 1;

    public static final int IMPORTS_CHANGE = 2;

    private final OntologyChangeVisitorEx<Integer> changeDataTypeVisitor;


    private final Comparator<OWLAxiom> axiomComparator;

    private final Comparator<? super OWLAnnotation> annotationComparator;

    private final OntologyChangeVisitorEx<Integer> changeTypeVisitor;


    /**
     * Creates a change record comparator that uses the specified comparators to compare axioms an ontology
     * annotations within the records that it compares.
     * @param axiomComparator The axiom comparator.
     * @param annotationComparator The annotation comparator.
     */
    @Inject
    public OntologyChangeComparator(@Nonnull Comparator<OWLAxiom> axiomComparator,
                                    @Nonnull Comparator<? super OWLAnnotation> annotationComparator) {
        this.axiomComparator = axiomComparator;
        this.annotationComparator = annotationComparator;
        changeDataTypeVisitor = new OntologyChangeVisitorEx<>() {
            @Nonnull
            @Override
            public Integer visit(@Nonnull AddAxiomChange change) throws RuntimeException {
                return AXIOM_CHANGE;
            }

            @Override
            public Integer visit(@Nonnull RemoveAxiomChange change) throws RuntimeException {
                return AXIOM_CHANGE;
            }

            @Override
            public Integer visit(@Nonnull AddOntologyAnnotationChange change) throws RuntimeException {
                return ONTOLOGY_ANNOTATION_CHANGE;
            }

            @Override
            public Integer visit(@Nonnull RemoveOntologyAnnotationChange change) throws RuntimeException {
                return ONTOLOGY_ANNOTATION_CHANGE;
            }

            @Override
            public Integer visit(@Nonnull AddImportChange change) throws RuntimeException {
                return IMPORTS_CHANGE;
            }

            @Override
            public Integer visit(@Nonnull RemoveImportChange change) throws RuntimeException {
                return IMPORTS_CHANGE;
            }

            @Override
            public Integer getDefaultReturnValue() {
                return 0;
            }
        };
        changeTypeVisitor = new OntologyChangeVisitorEx<>() {

            @Nonnull
            @Override
            public Integer visit(@Nonnull AddAxiomChange change) throws RuntimeException {
                return 1;
            }

            @Override
            public Integer visit(@Nonnull RemoveAxiomChange change) throws RuntimeException {
                return 0;
            }

            @Override
            public Integer visit(@Nonnull AddOntologyAnnotationChange change) throws RuntimeException {
                return 1;
            }

            @Override
            public Integer visit(@Nonnull RemoveOntologyAnnotationChange data) throws RuntimeException {
                return 0;
            }

            @Override
            public Integer visit(@Nonnull AddImportChange change) throws RuntimeException {
                return 1;
            }

            @Override
            public Integer visit(@Nonnull RemoveImportChange change) throws RuntimeException {
                return 0;
            }

            @Override
            public Integer getDefaultReturnValue() {
                return 0;
            }
        };
    }


    @Override
    public int compare(OntologyChange o1, OntologyChange o2) {
        if(o1 == o2) {
            return 0;
        }
        Integer index1 = o1.accept(changeDataTypeVisitor);
        Integer index2 = o2.accept(changeDataTypeVisitor);
        int typeDiff = index1.compareTo(index2);
        if(typeDiff != 0) {
            return typeDiff;
        }
        int specificTypeDiff = 0;
        if(index1 == AXIOM_CHANGE) {
            specificTypeDiff = axiomComparator.compare(
                    o1.getAxiomOrThrow(),
                    o2.getAxiomOrThrow()
            );
        }
        if(index1 == ONTOLOGY_ANNOTATION_CHANGE) {
            specificTypeDiff = annotationComparator.compare(
                    o1.getAnnotationOrThrow(),
                    o2.getAnnotationOrThrow()
            );
        }
        if(index1 == IMPORTS_CHANGE) {
            IRI iri1 = o1.getImportsDeclarationOrThrow().getIRI();
            IRI iri2 = o2.getImportsDeclarationOrThrow().getIRI();
            specificTypeDiff = iri1.compareTo(iri2);
        }
        if(specificTypeDiff != 0) {
            return specificTypeDiff;
        }
        Integer changeType1 = o1.accept(changeTypeVisitor);
        Integer changeType2 = o2.accept(changeTypeVisitor);
        return changeType1.compareTo(changeType2);
    }


}

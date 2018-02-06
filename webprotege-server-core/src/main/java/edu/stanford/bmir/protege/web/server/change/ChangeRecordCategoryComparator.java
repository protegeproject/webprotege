package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.change.*;

import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class ChangeRecordCategoryComparator implements Comparator<OWLOntologyChangeRecord> {

    public static final int AXIOM_CHANGE = 0;
    public static final int ONTOLOGY_ANNOTATION_CHANGE = 1;
    public static final int IMPORTS_CHANGE = 2;
    public static final int ONTOLOGY_ID_CHANGE = 3;

    private final OWLOntologyChangeDataVisitor<Integer, RuntimeException> changeDataTypeVisitor;

    public ChangeRecordCategoryComparator() {
        changeDataTypeVisitor = new OWLOntologyChangeDataVisitor<Integer, RuntimeException>() {
            @Override
            public Integer visit(AddAxiomData data) throws RuntimeException {
                return AXIOM_CHANGE;
            }

            @Override
            public Integer visit(RemoveAxiomData data) throws RuntimeException {
                return AXIOM_CHANGE;
            }

            @Override
            public Integer visit(AddOntologyAnnotationData data) throws RuntimeException {
                return ONTOLOGY_ANNOTATION_CHANGE;
            }

            @Override
            public Integer visit(RemoveOntologyAnnotationData data) throws RuntimeException {
                return ONTOLOGY_ANNOTATION_CHANGE;
            }

            @Override
            public Integer visit(AddImportData data) throws RuntimeException {
                return IMPORTS_CHANGE;
            }

            @Override
            public Integer visit(RemoveImportData data) throws RuntimeException {
                return IMPORTS_CHANGE;
            }

            @Override
            public Integer visit(SetOntologyIDData data) throws RuntimeException {
                return ONTOLOGY_ID_CHANGE;
            }

        };
    }

    @Override
    public int compare(OWLOntologyChangeRecord o1, OWLOntologyChangeRecord o2) {
        if(o1 == o2) {
            return 0;
        }
        return o1.getData().accept(changeDataTypeVisitor) - o2.getData().accept(changeDataTypeVisitor);
    }
}

package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class ChangeRecordComparator implements Comparator<OWLOntologyChangeRecord> {

    public static final int AXIOM_CHANGE = 0;
    public static final int ONTOLOGY_ANNOTATION_CHANGE = 1;
    public static final int IMPORTS_CHANGE = 2;
    public static final int ONTOLOGY_ID_CHANGE = 3;

    private final OWLOntologyChangeDataVisitor<Integer, RuntimeException> changeDataTypeVisitor;


    private final Comparator<OWLAxiom> axiomComparator;

    private final Comparator<? super OWLAnnotation> annotationComparator;
    private final OWLOntologyChangeDataVisitor<Integer, RuntimeException> changeTypeVisitor;


    public ChangeRecordComparator(Comparator<OWLAxiom> axiomComparator, Comparator<? super OWLAnnotation> annotationComparator) {
        this.axiomComparator = axiomComparator;
        this.annotationComparator = annotationComparator;
        changeDataTypeVisitor = new OWLOntologyChangeDataVisitor<Integer, RuntimeException>() {
            @Nonnull
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
        changeTypeVisitor = new OWLOntologyChangeDataVisitor<Integer, RuntimeException>() {
            @Override
            public Integer visit(AddAxiomData data) throws RuntimeException {
                return 1;
            }

            @Override
            public Integer visit(RemoveAxiomData data) throws RuntimeException {
                return 0;
            }

            @Override
            public Integer visit(AddOntologyAnnotationData data) throws RuntimeException {
                return 1;
            }

            @Override
            public Integer visit(RemoveOntologyAnnotationData data) throws RuntimeException {
                return 0;
            }

            @Override
            public Integer visit(SetOntologyIDData data) throws RuntimeException {
                return 0;
            }

            @Override
            public Integer visit(AddImportData data) throws RuntimeException {
                return 1;
            }

            @Override
            public Integer visit(RemoveImportData data) throws RuntimeException {
                return 0;
            }
        };
    }


    @Override
    public int compare(OWLOntologyChangeRecord o1, OWLOntologyChangeRecord o2) {
        if(o1 == o2) {
            return 0;
        }
        OWLOntologyChangeData changeData1 = o1.getData();
        OWLOntologyChangeData changeData2 = o2.getData();
        Integer index1 = changeData1.accept(changeDataTypeVisitor);
        Integer index2 = changeData2.accept(changeDataTypeVisitor);
        int typeDiff = index1.compareTo(index2);
        if(typeDiff != 0) {
            return typeDiff;
        }
        int specificTypeDiff = 0;
        if(index1 == AXIOM_CHANGE) {
            AxiomChangeData axiomChangeData1 = (AxiomChangeData) changeData1;
            AxiomChangeData axiomChangeData2 = (AxiomChangeData) changeData2;
            specificTypeDiff = axiomComparator.compare(
                    axiomChangeData1.getAxiom(),
                    axiomChangeData2.getAxiom()
            );
        }
        if(index1 == ONTOLOGY_ANNOTATION_CHANGE) {
            OntologyAnnotationChangeData annotationChangeData1 = (OntologyAnnotationChangeData) changeData1;
            OntologyAnnotationChangeData annotationChangeData2 = (OntologyAnnotationChangeData) changeData2;
            specificTypeDiff = annotationComparator.compare(
                    annotationChangeData1.getAnnotation(),
                    annotationChangeData2.getAnnotation()
            );
        }
        if(index1 == IMPORTS_CHANGE) {
            IRI iri1 = ((ImportChangeData) changeData1).getDeclaration().getIRI();
            IRI iri2 = ((ImportChangeData) changeData2).getDeclaration().getIRI();
            specificTypeDiff = iri1.compareTo(iri2);
        }
        if(index1 == ONTOLOGY_ID_CHANGE) {
            OWLOntologyID newId1 = ((SetOntologyIDData) changeData1).getNewId();
            OWLOntologyID newId2 = ((SetOntologyIDData) changeData2).getNewId();
            return newId1.compareTo(newId2);
        }
        if(specificTypeDiff != 0) {
            return specificTypeDiff;
        }
        Integer changeType1 = changeData1.accept(changeTypeVisitor);
        Integer changeType2 = changeData1.accept(changeTypeVisitor);
        return changeType1.compareTo(changeType2);
    }


}

package edu.stanford.bmir.protege.web.server.csv;

import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.change.SilentChangeListGenerator;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.csv.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static org.semanticweb.owlapi.model.EntityType.CLASS;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2013
 */
public class ImportCSVFileChangeListGenerator implements ChangeListGenerator<Integer>, SilentChangeListGenerator {


    @Nonnull
    private final OWLClass importRootClass;

    @Nonnull
    private final CSVGrid csvGrid;

    @Nonnull
    private final CSVImportDescriptor descriptor;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory dataFactory;


    @Inject
    public ImportCSVFileChangeListGenerator(@Nonnull OWLClass importRootClass,
                                            @Nonnull CSVGrid csvGrid,
                                            @Nonnull CSVImportDescriptor descriptor,
                                            @Nonnull OWLOntology rootOntology,
                                            @Nonnull OWLDataFactory dataFactory) {
        this.importRootClass = importRootClass;
        this.csvGrid = csvGrid;
        this.descriptor = descriptor;
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
    }

    @Override
    public OntologyChangeList<Integer> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Integer> changesBuilder = new OntologyChangeList.Builder<Integer>();
        for (CSVRow row : csvGrid.getRows()) {
            for (CSVColumnDescriptor columnDescriptor : descriptor.getColumnDescriptors()) {
                if (columnDescriptor.getColumnIndex() < row.size()) {
                    String value = row.getColumnValue(columnDescriptor.getColumnIndex()).trim();
                    final ColumnType columnType = columnDescriptor.getColumnType();
                    final int displayNameIndex = descriptor.getDisplayNameColumnIndex();
                    final String displayName = row.getColumnValue(displayNameIndex);
                    if (columnDescriptor.getColumnProperty().getEntityType() == EntityType.OBJECT_PROPERTY) {
                        final OWLObjectProperty property = (OWLObjectProperty) columnDescriptor.getColumnProperty();
                        final Optional<OWLClassExpression> superCls = getColumnValueAsClassExpression(value, property, columnType);
                        if (superCls.isPresent()) {
                            changesBuilder.addAxiom(rootOntology, getAxiom(displayName, columnDescriptor, superCls.get()));
                        }
                    }
                    else {
                        if (columnDescriptor.getColumnProperty().getEntityType() == EntityType.DATA_PROPERTY) {
                            final OWLDataProperty property = (OWLDataProperty) columnDescriptor.getColumnProperty();
                            final Optional<OWLLiteral> filler = getColumnValueAsLiteral(value, columnType);
                            if (filler.isPresent()) {
                                final OWLClassExpression superCls = dataFactory.getOWLDataHasValue(property, filler.get());
                                changesBuilder.addAxiom(rootOntology, getAxiom(displayName, columnDescriptor, superCls));
                            }
                        }
                        else if (columnDescriptor.getColumnProperty().getEntityType() == EntityType.ANNOTATION_PROPERTY) {
                            OWLAnnotationProperty property = (OWLAnnotationProperty) columnDescriptor.getColumnProperty();
                            final Optional<? extends OWLAnnotationValue> annotationValue = getColumnValueAsAnnotationValue(value, columnType);
                            if (annotationValue.isPresent()) {
                                final IRI rowIRI = DataFactory.getFreshOWLEntityIRI(displayName);
                                changesBuilder.addAxiom(rootOntology, dataFactory.getOWLAnnotationAssertionAxiom(property, rowIRI, annotationValue.get()));
                            }
                        }
                    }
                    OWLAxiom placementAxiom = getPlacementAxiom(displayName);
                    changesBuilder.addAxiom(rootOntology, placementAxiom);
                }
            }
        }
        return changesBuilder.build(csvGrid.getRowCount());
    }

    private OWLAxiom getPlacementAxiom(String displayName) {
        OWLAxiom placementAxiom;
        if(descriptor.getRowImportType() == CSVRowImportType.CLASS) {
            placementAxiom = dataFactory.getOWLSubClassOfAxiom(DataFactory.getFreshOWLEntity(CLASS, displayName), importRootClass);
        }
        else {
            placementAxiom = dataFactory.getOWLClassAssertionAxiom(importRootClass, DataFactory.getFreshOWLEntity(EntityType.NAMED_INDIVIDUAL, displayName));
        }
        return placementAxiom;
    }

    private Optional<OWLClassExpression> getColumnValueAsClassExpression(String value, OWLObjectProperty columnProperty, ColumnType columnType) {
        if(value.trim().isEmpty()) {
            return Optional.empty();
        }
        OWLClassExpression superCls;
        if (columnType == ColumnType.CLASS) {
            final OWLClass filler = DataFactory.getFreshOWLEntity(CLASS, value);
            superCls = dataFactory.getOWLObjectSomeValuesFrom(columnProperty, filler);
        }
        else if (columnType == ColumnType.NAMED_INDIVIDUAL) {
            final OWLNamedIndividual filler = DataFactory.getFreshOWLEntity(EntityType.NAMED_INDIVIDUAL, value);
            superCls = dataFactory.getOWLObjectHasValue(columnProperty, filler);
        }
        else {
            throw new RuntimeException("Unrecognized ColumnType: " + columnType);
        }
        return Optional.of(superCls);
    }

    private Optional<? extends OWLAnnotationValue> getColumnValueAsAnnotationValue(String value, ColumnType columnType) {
        Optional<? extends OWLAnnotationValue> annotationValue;
        if (columnType == ColumnType.CLASS) {
            annotationValue = Optional.of(DataFactory.getFreshOWLEntityIRI(value));
        }
        else if (columnType == ColumnType.NAMED_INDIVIDUAL) {
            annotationValue = Optional.of(DataFactory.getFreshOWLEntityIRI(value));
        }
        else {
            annotationValue = getColumnValueAsLiteral(value, columnType);
        }
        return annotationValue;
    }

    private Optional<OWLLiteral> getColumnValueAsLiteral(String value, ColumnType columnType) {
        try {
            OWLLiteral filler;
            if (columnType == ColumnType.DOUBLE) {
                filler = dataFactory.getOWLLiteral(Double.parseDouble(value));
            }
            else if (columnType == ColumnType.BOOLEAN) {
                filler = dataFactory.getOWLLiteral(Boolean.parseBoolean(value));
            }
            else if (columnType == ColumnType.INTEGER) {
                filler = dataFactory.getOWLLiteral(Integer.parseInt(value));
            }
            else {
                filler = dataFactory.getOWLLiteral(value);
            }
            return Optional.of(filler);
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }


    private OWLAxiom getAxiom(String rowEntityDisplayName, CSVColumnDescriptor columnDescriptor, OWLClassExpression superCls) {
        if (descriptor.getRowImportType() == CSVRowImportType.CLASS) {
            OWLClass rowCls = DataFactory.getFreshOWLEntity(CLASS, rowEntityDisplayName);
            return dataFactory.getOWLSubClassOfAxiom(rowCls, superCls);
        }
        else {
            OWLNamedIndividual rowInd = DataFactory.getFreshOWLEntity(EntityType.NAMED_INDIVIDUAL, rowEntityDisplayName);
            return dataFactory.getOWLClassAssertionAxiom(superCls, rowInd);
        }
    }

    @Override
    public Integer getRenamedResult(Integer result, RenameMap renameMap) {
        return result;
    }
}

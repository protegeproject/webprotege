package edu.stanford.bmir.protege.web.server.csv;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.csv.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
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
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;


    @AutoFactory
    @Inject
    public ImportCSVFileChangeListGenerator(@Nonnull OWLClass importRootClass,
                                            @Nonnull CSVGrid csvGrid,
                                            @Nonnull CSVImportDescriptor descriptor,
                                            @Provided @Nonnull OWLDataFactory dataFactory,
                                            @Provided @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        this.importRootClass = checkNotNull(importRootClass);
        this.csvGrid = checkNotNull(csvGrid);
        this.descriptor = checkNotNull(descriptor);
        this.dataFactory = checkNotNull(dataFactory);
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }

    @Override
    public OntologyChangeList<Integer> generateChanges(ChangeGenerationContext context) {
        var rootOntology = defaultOntologyIdManager.getDefaultOntologyId();
        OntologyChangeList.Builder<Integer> changesBuilder = new OntologyChangeList.Builder<>();
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
                            var addAxiom = AddAxiomChange.of(rootOntology,
                                                                             getAxiom(displayName,
                                                                                      columnDescriptor,
                                                                                      superCls.get()));
                            changesBuilder.add(addAxiom);
                        }
                    }
                    else {
                        if (columnDescriptor.getColumnProperty().getEntityType() == EntityType.DATA_PROPERTY) {
                            final OWLDataProperty property = (OWLDataProperty) columnDescriptor.getColumnProperty();
                            final Optional<OWLLiteral> filler = getColumnValueAsLiteral(value, columnType);
                            if (filler.isPresent()) {
                                final OWLClassExpression superCls = dataFactory.getOWLDataHasValue(property, filler.get());
                                var addAxiom = AddAxiomChange.of(rootOntology, getAxiom(displayName, columnDescriptor, superCls));
                                changesBuilder.add(addAxiom);
                            }
                        }
                        else if (columnDescriptor.getColumnProperty().getEntityType() == EntityType.ANNOTATION_PROPERTY) {
                            OWLAnnotationProperty property = (OWLAnnotationProperty) columnDescriptor.getColumnProperty();
                            final Optional<? extends OWLAnnotationValue> annotationValue = getColumnValueAsAnnotationValue(value, columnType);
                            if (annotationValue.isPresent()) {
                                final IRI rowIRI = DataFactory.getFreshOWLEntityIRI(displayName);
                                var addAxiom = AddAxiomChange.of(rootOntology, dataFactory.getOWLAnnotationAssertionAxiom(property, rowIRI, annotationValue.get()));
                                changesBuilder.add(addAxiom);
                            }
                        }
                    }
                    OWLAxiom placementAxiom = getPlacementAxiom(displayName);
                    var addAxiom = AddAxiomChange.of(rootOntology, placementAxiom);
                    changesBuilder.add(addAxiom);
                }
            }
        }
        return changesBuilder.build(csvGrid.getRowCount());
    }

    private OWLAxiom getPlacementAxiom(String displayName) {
        OWLAxiom placementAxiom;
        if(descriptor.getRowImportType() == CSVRowImportType.CLASS) {
            placementAxiom = dataFactory.getOWLSubClassOfAxiom(DataFactory.getFreshOWLEntity(CLASS, displayName, Optional.empty()), importRootClass);
        }
        else {
            placementAxiom = dataFactory.getOWLClassAssertionAxiom(importRootClass, DataFactory.getFreshOWLEntity(EntityType.NAMED_INDIVIDUAL, displayName, Optional.empty()));
        }
        return placementAxiom;
    }

    private Optional<OWLClassExpression> getColumnValueAsClassExpression(String value, OWLObjectProperty columnProperty, ColumnType columnType) {
        if(value.trim().isEmpty()) {
            return Optional.empty();
        }
        OWLClassExpression superCls;
        if (columnType == ColumnType.CLASS) {
            final OWLClass filler = DataFactory.getFreshOWLEntity(CLASS, value, Optional.empty());
            superCls = dataFactory.getOWLObjectSomeValuesFrom(columnProperty, filler);
        }
        else if (columnType == ColumnType.NAMED_INDIVIDUAL) {
            final OWLNamedIndividual filler = DataFactory.getFreshOWLEntity(EntityType.NAMED_INDIVIDUAL, value, Optional.empty());
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
            OWLClass rowCls = DataFactory.getFreshOWLEntity(CLASS, rowEntityDisplayName, Optional.empty());
            return dataFactory.getOWLSubClassOfAxiom(rowCls, superCls);
        }
        else {
            OWLNamedIndividual rowInd = DataFactory.getFreshOWLEntity(EntityType.NAMED_INDIVIDUAL, rowEntityDisplayName, Optional.empty());
            return dataFactory.getOWLClassAssertionAxiom(superCls, rowInd);
        }
    }

    @Override
    public Integer getRenamedResult(Integer result, RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Integer> result) {
        return "Generated axioms from imported CSV file";
    }
}

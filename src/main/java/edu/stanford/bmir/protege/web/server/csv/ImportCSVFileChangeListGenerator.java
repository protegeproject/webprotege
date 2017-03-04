package edu.stanford.bmir.protege.web.server.csv;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.change.SilentChangeListGenerator;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.csv.*;
import org.semanticweb.owlapi.model.*;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2013
 */
public class ImportCSVFileChangeListGenerator implements ChangeListGenerator<Integer>, SilentChangeListGenerator {


    private OWLClass importRootClass;

    private CSVGrid csvGrid;

    private CSVImportDescriptor descriptor;

    public ImportCSVFileChangeListGenerator(OWLClass importRootClass, CSVGrid csvGrid, CSVImportDescriptor descriptor) {
        this.importRootClass = importRootClass;
        this.csvGrid = csvGrid;
        this.descriptor = descriptor;
    }

    @Override
    public OntologyChangeList<Integer> generateChanges(Project project, ChangeGenerationContext context) {
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
                        final Optional<OWLClassExpression> superCls = getColumnValueAsClassExpression(project, value, property, columnType);
                        if (superCls.isPresent()) {
                            changesBuilder.addAxiom(project.getRootOntology(), getAxiom(project, displayName, columnDescriptor, superCls.get()));
                        }
                    }
                    else if (columnDescriptor.getColumnProperty().getEntityType() == EntityType.DATA_PROPERTY) {
                        final OWLDataProperty property = (OWLDataProperty) columnDescriptor.getColumnProperty();
                        final Optional<OWLLiteral> filler = getColumnValueAsLiteral(project, value, columnType);
                        if (filler.isPresent()) {
                            final OWLClassExpression superCls = project.getDataFactory().getOWLDataHasValue(property, filler.get());
                            changesBuilder.addAxiom(project.getRootOntology(), getAxiom(project, displayName, columnDescriptor, superCls));
                        }
                    }
                    else if (columnDescriptor.getColumnProperty().getEntityType() == EntityType.ANNOTATION_PROPERTY) {
                        OWLAnnotationProperty property = (OWLAnnotationProperty) columnDescriptor.getColumnProperty();
                        final Optional<? extends OWLAnnotationValue> annotationValue = getColumnValueAsAnnotationValue(project, value, columnType);
                        if (annotationValue.isPresent()) {
                            final IRI rowIRI = DataFactory.getFreshOWLEntityIRI(displayName);
                            changesBuilder.addAxiom(project.getRootOntology(), project.getDataFactory().getOWLAnnotationAssertionAxiom(property, rowIRI, annotationValue.get()));
                        }
                    }
                    OWLAxiom placementAxiom = getPlacementAxiom(project, displayName);
                    changesBuilder.addAxiom(project.getRootOntology(), placementAxiom);
                }
            }
        }
        return changesBuilder.build(csvGrid.getRowCount());
    }

    private OWLAxiom getPlacementAxiom(Project project, String displayName) {
        OWLAxiom placementAxiom;
        if(descriptor.getRowImportType() == CSVRowImportType.CLASS) {
            placementAxiom = project.getDataFactory().getOWLSubClassOfAxiom(DataFactory.getFreshOWLEntity(EntityType.CLASS, displayName), importRootClass);
        }
        else {
            placementAxiom = project.getDataFactory().getOWLClassAssertionAxiom(importRootClass, DataFactory.getFreshOWLEntity(EntityType.NAMED_INDIVIDUAL, displayName));
        }
        return placementAxiom;
    }

    private Optional<OWLClassExpression> getColumnValueAsClassExpression(Project project, String value, OWLObjectProperty columnProperty, ColumnType columnType) {
        if(value.trim().isEmpty()) {
            return Optional.absent();
        }
        OWLClassExpression superCls;
        if (columnType == ColumnType.CLASS) {
            final OWLClass filler = DataFactory.getFreshOWLEntity(EntityType.CLASS, value);
            superCls = project.getDataFactory().getOWLObjectSomeValuesFrom(columnProperty, filler);
        }
        else if (columnType == ColumnType.NAMED_INDIVIDUAL) {
            final OWLNamedIndividual filler = DataFactory.getFreshOWLEntity(EntityType.NAMED_INDIVIDUAL, value);
            superCls = project.getDataFactory().getOWLObjectHasValue(columnProperty, filler);
        }
        else {
            throw new RuntimeException("Unrecognized ColumnType: " + columnType);
        }
        return Optional.of(superCls);
    }

    private Optional<? extends OWLAnnotationValue> getColumnValueAsAnnotationValue(Project project, String value, ColumnType columnType) {
        Optional<? extends OWLAnnotationValue> annotationValue;
        if (columnType == ColumnType.CLASS) {
            annotationValue = Optional.of(DataFactory.getFreshOWLEntityIRI(value));
        }
        else if (columnType == ColumnType.NAMED_INDIVIDUAL) {
            annotationValue = Optional.of(DataFactory.getFreshOWLEntityIRI(value));
        }
        else {
            annotationValue = getColumnValueAsLiteral(project, value, columnType);
        }
        return annotationValue;
    }

    private Optional<OWLLiteral> getColumnValueAsLiteral(Project project, String value, ColumnType columnType) {
        try {
            OWLLiteral filler;
            if (columnType == ColumnType.DOUBLE) {
                filler = project.getDataFactory().getOWLLiteral(Double.parseDouble(value));
            }
            else if (columnType == ColumnType.BOOLEAN) {
                filler = project.getDataFactory().getOWLLiteral(Boolean.parseBoolean(value));
            }
            else if (columnType == ColumnType.INTEGER) {
                filler = project.getDataFactory().getOWLLiteral(Integer.parseInt(value));
            }
            else {
                filler = project.getDataFactory().getOWLLiteral(value);
            }
            return Optional.of(filler);
        }
        catch (NumberFormatException e) {
            return Optional.absent();
        }
    }


    private OWLAxiom getAxiom(Project project, String rowEntityDisplayName, CSVColumnDescriptor columnDescriptor, OWLClassExpression superCls) {
        if (descriptor.getRowImportType() == CSVRowImportType.CLASS) {
            OWLClass rowCls = DataFactory.getFreshOWLEntity(EntityType.CLASS, rowEntityDisplayName);
            return project.getDataFactory().getOWLSubClassOfAxiom(rowCls, superCls);
        }
        else {
            OWLNamedIndividual rowInd = DataFactory.getFreshOWLEntity(EntityType.NAMED_INDIVIDUAL, rowEntityDisplayName);
            return project.getDataFactory().getOWLClassAssertionAxiom(superCls, rowInd);
        }
    }

    @Override
    public Integer getRenamedResult(Integer result, RenameMap renameMap) {
        return result;
    }
}

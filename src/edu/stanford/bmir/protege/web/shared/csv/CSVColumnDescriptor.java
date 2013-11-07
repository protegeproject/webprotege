package edu.stanford.bmir.protege.web.shared.csv;


import org.semanticweb.owlapi.model.*;

import java.io.Serializable;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public final class CSVColumnDescriptor implements Serializable {

    private int columnIndex;

    private OWLEntity columnProperty;

    private ColumnType columnType;

    /**
     * For serialization purposes only
     */
    private CSVColumnDescriptor() {
    }

    public static CSVColumnDescriptor create(final int columnIndex, final OWLEntity columnProperty, final ColumnType columnType) {
        return columnProperty.accept(new OWLEntityVisitorEx<CSVColumnDescriptor>() {
            @Override
            public CSVColumnDescriptor visit(OWLDataProperty property) {
                return new CSVColumnDescriptor(columnIndex, property, columnType);
            }

            @Override
            public CSVColumnDescriptor visit(OWLObjectProperty property) {
                return new CSVColumnDescriptor(columnIndex, property, columnType);
            }

            @Override
            public CSVColumnDescriptor visit(OWLAnnotationProperty property) {
                return new CSVColumnDescriptor(columnIndex, property, columnType);
            }

            @Override
            public CSVColumnDescriptor visit(OWLClass cls) {
                return null;
            }

            @Override
            public CSVColumnDescriptor visit(OWLNamedIndividual individual) {
                return null;
            }

            @Override
            public CSVColumnDescriptor visit(OWLDatatype datatype) {
                return null;
            }
        });
    }

    protected CSVColumnDescriptor(int columnIndex, OWLObjectProperty columnProperty, ColumnType columnType) {
        this.columnIndex = columnIndex;
        this.columnProperty = columnProperty;
        this.columnType = columnType;
    }

    protected CSVColumnDescriptor(int columnIndex, OWLAnnotationProperty columnProperty, ColumnType columnType) {
        this.columnIndex = columnIndex;
        this.columnProperty = columnProperty;
        this.columnType = columnType;
    }

    protected CSVColumnDescriptor(int columnIndex, OWLDataProperty columnProperty, ColumnType columnType) {
        this.columnIndex = columnIndex;
        this.columnProperty = columnProperty;
        this.columnType = columnType;
    }

    public int getColumnIndex() {
        return columnIndex;
    }


    public ColumnType getColumnType() {
        return columnType;
    }

    public OWLEntity getColumnProperty() {
        return columnProperty;
    }

    @Override
    public int hashCode() {
        return "CSVColumnDescriptor".hashCode() + columnIndex + columnProperty.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof CSVColumnDescriptor)) {
            return false;
        }
        CSVColumnDescriptor other = (CSVColumnDescriptor) obj;
        return this.columnIndex == other.columnIndex && this.columnProperty.equals(other.columnProperty) && this.columnType.equals(other.columnType);
    }
}

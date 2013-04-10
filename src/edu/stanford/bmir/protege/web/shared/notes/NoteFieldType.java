package edu.stanford.bmir.protege.web.shared.notes;

import org.semanticweb.owlapi.model.*;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public final class NoteFieldType<T extends Serializable> implements Serializable {

    public static final NoteFieldType<String> STRING = new NoteFieldType<String>();

    public static final NoteFieldType<Boolean> BOOLEAN = new NoteFieldType<Boolean>();

    public static final NoteFieldType<Integer> INTEGER = new NoteFieldType<Integer>();

    public static final NoteFieldType<Long> LONG = new NoteFieldType<Long>();

    public static final NoteFieldType<OWLClass> OWL_CLASS = new NoteFieldType<OWLClass>();

    public static final NoteFieldType<OWLObjectProperty> OWL_OBJECT_PROPERTY = new NoteFieldType<OWLObjectProperty>();

    public static final NoteFieldType<OWLDataProperty> OWL_DATA_PROPERTY = new NoteFieldType<OWLDataProperty>();

    public static final NoteFieldType<OWLAnnotationProperty> OWL_ANNOTATION_PROPERTY = new NoteFieldType<OWLAnnotationProperty>();

    public static final NoteFieldType<OWLNamedIndividual> OWL_NAMED_INDIVIDUAL = new NoteFieldType<OWLNamedIndividual>();

    public static final NoteFieldType<OWLDatatype> OWL_DATATYPE = new NoteFieldType<OWLDatatype>();

    public static final NoteFieldType<NoteType> NOTE_TYPE = new NoteFieldType<NoteType>();

    private NoteFieldType() {
    }
}

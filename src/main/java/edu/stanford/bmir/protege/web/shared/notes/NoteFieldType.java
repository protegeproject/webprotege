package edu.stanford.bmir.protege.web.shared.notes;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public final class NoteFieldType<T extends Serializable> implements Serializable {

    public static final transient NoteFieldType<String> STRING = new NoteFieldType<String>(new Tokenizer<String>() {
        @Override
        public String toToken(String value) {
            return value;
        }

        @Override
        public String fromToken(String token) {
            return token;
        }
    });
//
//    public static final NoteFieldType<Boolean> BOOLEAN = new NoteFieldType<Boolean>();
//
//    public static final NoteFieldType<Integer> INTEGER = new NoteFieldType<Integer>();
//
//    public static final NoteFieldType<Long> LONG = new NoteFieldType<Long>();
//
//    public static final NoteFieldType<OWLClass> OWL_CLASS = new NoteFieldType<OWLClass>();
//
//    public static final NoteFieldType<OWLObjectProperty> OWL_OBJECT_PROPERTY = new NoteFieldType<OWLObjectProperty>();
//
//    public static final NoteFieldType<OWLDataProperty> OWL_DATA_PROPERTY = new NoteFieldType<OWLDataProperty>();
//
//    public static final NoteFieldType<OWLAnnotationProperty> OWL_ANNOTATION_PROPERTY = new NoteFieldType<OWLAnnotationProperty>();
//
//    public static final NoteFieldType<OWLNamedIndividual> OWL_NAMED_INDIVIDUAL = new NoteFieldType<OWLNamedIndividual>();
//
//    public static final NoteFieldType<OWLDatatype> OWL_DATATYPE = new NoteFieldType<OWLDatatype>();

    public static final transient NoteFieldType<NoteType> NOTE_TYPE = new NoteFieldType<NoteType>(new Tokenizer<NoteType>() {
        @Override
        public String toToken(NoteType value) {
            return value.name();
        }

        @Override
        public NoteType fromToken(String token) {
            return NoteType.valueOf(token);
        }
    });

    public static final transient NoteFieldType<NoteStatus> NOTE_STATUS = new NoteFieldType<NoteStatus>(new Tokenizer<NoteStatus>() {
        @Override
        public String toToken(NoteStatus value) {
            return value.name();
        }

        @Override
        public NoteStatus fromToken(String token) {
            return NoteStatus.valueOf(token);
        }
    });


    public String toToken(T value) {
        return tokenizer.toToken(value);
    }

    public T fromToken(String token) {
        return tokenizer.fromToken(token);
    }


    private final transient Tokenizer<T> tokenizer;


    public NoteFieldType(Tokenizer<T> tokenizer) {
        this.tokenizer = tokenizer;
    }

    public interface Tokenizer<T extends Serializable> {

        String toToken(T value);

        T fromToken(String token);

    }




    private static class NoteStatusType {

    }

}

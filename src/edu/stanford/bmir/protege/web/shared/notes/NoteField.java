package edu.stanford.bmir.protege.web.shared.notes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public final class NoteField<T extends Serializable> implements Serializable, Comparable<NoteField<?>> {

    private static final Set<NoteField<? extends Serializable>> VALUES = new HashSet<NoteField<? extends Serializable>>();



    public static final NoteField<NoteType> TYPE = get("type", new Tokenizer<NoteType>() {
        @Override
        public String toToken(NoteType value) {
            return value.name();
        }

        @Override
        public NoteType fromToken(String token) {
            return NoteType.valueOf(token);
        }
    });

    public static final NoteField<String> SUBJECT = get("subject", new Tokenizer<String>() {
        @Override
        public String toToken(String value) {
            return value;
        }

        @Override
        public String fromToken(String token) {
            return token;
        }
    });

    public static final NoteField<String> BODY = get("body", new Tokenizer<String>() {
        @Override
        public String toToken(String value) {
            return value;
        }

        @Override
        public String fromToken(String token) {
            return token;
        }
    });

    public static final NoteField<NoteStatus> STATUS = get("status", new Tokenizer<NoteStatus>() {
        @Override
        public String toToken(NoteStatus value) {
            return value.name();
        }

        @Override
        public NoteStatus fromToken(String token) {
            return NoteStatus.valueOf(token);
        }
    });







    private static <T extends Serializable> NoteField<T> get(String fieldName, Tokenizer<T> tokenizer) {
        final NoteField<T> e = new NoteField<T>(fieldName, tokenizer);
        VALUES.add(e);
        return e;
    }


    private String fieldName;

    private Tokenizer<T> tokenizer;

    /**
     * For serialization only!
     */
    private NoteField() {

    }


    private NoteField(String fieldName, Tokenizer<T> tokenizer) {
        this.fieldName = checkNotNull(fieldName);
        this.tokenizer = tokenizer;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String toToken(T value) {
        return tokenizer.toToken(value);
    }

    public T fromToken(String token) {
        return tokenizer.fromToken(token);
    }


    @Override
    public int hashCode() {
        return "NoteContentField".hashCode() + fieldName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NoteField)) {
            return false;
        }
        NoteField other = (NoteField) obj;
        return this.fieldName.equals(other.fieldName);
    }

    @Override
    public int compareTo(NoteField<?> o) {
        return this.getFieldName().compareTo(o.getFieldName());
    }

    public static Set<NoteField<? extends Serializable>> values() {
        return VALUES;
    }




    private static interface Tokenizer<T extends Serializable> extends Serializable {

        String toToken(T value);

        T fromToken(String token);
    }

}

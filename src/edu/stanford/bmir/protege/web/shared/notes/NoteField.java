package edu.stanford.bmir.protege.web.shared.notes;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public final class NoteField<T extends Serializable> implements Serializable, Comparable<NoteField<?>> {


    public static final NoteField<NoteType> TYPE = new NoteField<NoteType>("type", NoteFieldType.NOTE_TYPE);

    public static final NoteField<String> SUBJECT = new NoteField<String>("subject", NoteFieldType.STRING);

    /**
     * A field which specified the body of a note.
     */
    public static final NoteField<String> BODY = new NoteField<String>("body", NoteFieldType.STRING);

    public static final NoteField<NoteStatus> STATUS = new NoteField<NoteStatus>("resolved", NoteFieldType.NOTE_STATUS);


    private String fieldName;

    private NoteFieldType<T> fieldType;


    /**
     * For serialization only!
     */
    private NoteField() {

    }


    private NoteField(String fieldName, NoteFieldType<T> noteFieldType) {
        this.fieldName = checkNotNull(fieldName);
        this.fieldType = noteFieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public NoteFieldType<T> getNoteFieldType() {
        return fieldType;
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
}

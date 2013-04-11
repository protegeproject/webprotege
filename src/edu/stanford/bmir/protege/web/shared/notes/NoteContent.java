package edu.stanford.bmir.protege.web.shared.notes;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 * <p>
 *     Represents a the content of a simple note which has a type, a subject and a body.
 *     Note that this class DOES NOT capture the relationship between replies.
 * </p>
 * <p>
 *     ReplyContent objects are immutable.
 * </p>
 */
public final class NoteContent implements Serializable {

    private FieldValueList fieldValueList;

    /**
     * Empty constructor for the purposes of serialization
     */
    private NoteContent() {
    }

    /**
     * Constructs a NoteContent.
     *
     * @param body The body of the note.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public NoteContent(String body) {
        fieldValueList = FieldValueList.builder().addValue(NoteField.BODY, body).build();
    }

    public NoteContent(FieldValueList fieldValueList) {
        this.fieldValueList = fieldValueList;
    }

    public List<NoteField<? extends Serializable>> getFields() {
        return fieldValueList.getFields();
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> Optional<T> getFieldValue(NoteField<T> field) {
        return fieldValueList.getValue(field);
    }

    /**
     * Gets the body/content of the note.
     * @return A string representing the body of the note.  Not null.
     */
    public String getBody() {
        return fieldValueList.getValue(NoteField.BODY).or("");
    }

    @Override
    public int hashCode() {
        return "NoteContent".hashCode() + fieldValueList.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NoteContent)) {
            return false;
        }
        NoteContent other = (NoteContent) obj;
        return this.fieldValueList.equals(other.fieldValueList);
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {

        private FieldValueList.Builder builder = FieldValueList.builder();

        public <T extends Serializable> Builder addField(NoteField<T> field, T fieldValue) {
            builder.addValue(field, fieldValue);
            return this;
        }

        public NoteContent build() {
            return new NoteContent(builder.build());
        }
    }


}

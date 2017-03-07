package edu.stanford.bmir.protege.web.shared.notes;

import com.google.common.base.Optional;

import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;

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
 *     NoteContent objects are immutable.
 * </p>
 */
@Deprecated
public final class NoteContent implements Serializable {


    private Optional<String> subject;

    private Optional<String> body;

    private Optional<NoteType> noteType;

    private Optional<NoteStatus> noteStatus;

    private NoteContent() {
    }

    private NoteContent(Optional<String> subject, Optional<String> body, Optional<NoteType> noteType, Optional<NoteStatus> noteStatus) {
        this.subject = subject;
        this.body = body;
        this.noteType = noteType;
        this.noteStatus = noteStatus;
    }

    public Optional<String> getSubject() {
        return subject;
    }

    public Optional<String> getBody() {
        return body;
    }

    public Optional<NoteType> getNoteType() {
        return noteType;
    }

    public Optional<NoteStatus> getNoteStatus() {
        return noteStatus;
    }


    @Override
    public String toString() {
        return toStringHelper("NoteContent")
                .add("subject", subject)
                .add("body", body)
                .add("status", noteStatus)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Optional<String> subject = Optional.absent();

        private Optional<String> body = Optional.absent();

        private Optional<NoteType> noteType = Optional.of(NoteType.COMMENT);

        private Optional<NoteStatus> noteStatus = Optional.absent();

        public Builder setSubject(String subject) {
            this.subject = Optional.of(subject);
            return this;
        }

        public Builder setBody(String body) {
            this.body = Optional.of(body);
            return this;
        }

        public Builder setNoteType(NoteType noteType) {
            this.noteType = Optional.of(noteType);
            return this;
        }

        public Builder setNoteStatus(NoteStatus noteStatus) {
            this.noteStatus = Optional.of(noteStatus);
            return this;
        }

        public Builder setSubject(Optional<String> subject) {
            this.subject = subject;
            return this;
        }

        public Builder setBody(Optional<String> body) {
            this.body = body;
            return this;
        }

        public Builder setNoteType(Optional<NoteType> noteType) {
            this.noteType = noteType;
            return this;
        }

        public Builder setNoteStatus(Optional<NoteStatus> noteStatus) {
            this.noteStatus = noteStatus;
            return this;
        }

        public NoteContent build() {
            return new NoteContent(subject, body, noteType, noteStatus);
        }

    }


    //    private FieldValueList fieldValueList;
//
//    /**
//     * Empty constructor for the purposes of serialization
//     */
//    private NoteContent() {
//    }
//
//    /**
//     * Constructs a NoteContent.
//     *
//     * @param body The body of the note.  Not {@code null}.
//     * @throws NullPointerException if any parameters are {@code null}.
//     */
//    public NoteContent(String body) {
//        fieldValueList = FieldValueList.builder().addValue(NoteField.BODY, body).build();
//    }
//
//    public NoteContent(FieldValueList fieldValueList) {
//        this.fieldValueList = fieldValueList;
//    }
//
//    public List<NoteField<? extends Serializable>> getFields() {
//        return fieldValueList.getFields();
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T extends Serializable> Optional<T> getFieldValue(NoteField<T> field) {
//        return fieldValueList.getValue(field);
//    }
//
//    public <T extends Serializable> Optional<String> getFieldValueAsString(NoteField<T> field) {
//        Optional<T> value = fieldValueList.getValue(field);
//        if(value.isPresent()) {
//            return Optional.of(field.toToken(value.get()));
//        }
//        else {
//            return Optional.absent();
//        }
//    }
//
//    /**
//     * Gets the body/content of the note.
//     * @return A string representing the body of the note.  Not null.
//     */
//    public String getBody() {
//        return fieldValueList.getValue(NoteField.BODY).or("");
//    }
//
//    @Override
//    public int hashCode() {
//        return "NoteContent".hashCode() + fieldValueList.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if(obj == this) {
//            return true;
//        }
//        if(!(obj instanceof NoteContent)) {
//            return false;
//        }
//        NoteContent other = (NoteContent) obj;
//        return this.fieldValueList.equals(other.fieldValueList);
//    }
//
//    public static Builder builder() {
//        return new Builder();
//    }
//
//
//    public static class Builder {
//
//        private FieldValueList.Builder builder = FieldValueList.builder();
//
//        public <T extends Serializable> Builder addField(NoteField<T> field, T fieldValue) {
//            builder.addValue(field, fieldValue);
//            return this;
//        }
//
//        public NoteContent build() {
//            return new NoteContent(builder.build());
//        }
//    }
//
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("NoteContent");
//        sb.append("(");
//        sb.append(fieldValueList);
//        sb.append(")");
//        return sb.toString();
//    }


}

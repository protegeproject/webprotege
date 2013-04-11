package edu.stanford.bmir.protege.web.shared.notes;

import com.google.common.base.Optional;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 * <p>
 *     Instances of this class represent a list of field values.  To construct an instance us the {@link Builder} class
 *     which can be obtained from the {@link #builder()} method.
 * </p>
 */
@Immutable
public final class FieldValueList implements Serializable {


    private List<NoteField<? extends Serializable>> fields;

//    private ImmutableMap<NoteField<? extends Serializable>, Serializable> valueMap;

    private Map<NoteField<? extends Serializable>, Serializable> valueMap;


    private FieldValueList() {
    }

    private FieldValueList(List<NoteField<? extends Serializable>> fields, Map<NoteField<? extends Serializable>, Serializable> valueMap) {
        this.fields = checkNotNull(fields);
//        final ImmutableMap.Builder<NoteField<? extends Serializable>, Serializable> builder = ImmutableMap.builder();
//        this.valueMap = builder.putAll(checkNotNull(valueMap)).build();
        this.valueMap = new HashMap<NoteField<? extends Serializable>, Serializable>(valueMap);
        this.fields.addAll(valueMap.keySet());
    }

    /**
     * Gets the fields in this {@link FieldValueList}.
     * @return A list of the fields that have values. May be empty.  Not {@code null}.
     */
    public List<NoteField<? extends Serializable>> getFields() {
        return new ArrayList<NoteField<? extends Serializable>>(fields);
    }

    /**
     * Gets the value for a field.
     * @param field The field.  Not {@code null}.
     * @param <T> The optional value for the specified field.
     * @return  The value for the field. If the field is not present in this list or the field does not have
     * a specified value in this list then {@link com.google.common.base.Optional#absent()} will be returned.  Not
     * {@code null}.
     * @throws NullPointerException if {@code field} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> Optional<T> getValue(NoteField<T> field) {
        Object value = valueMap.get(field);
        if(value == null) {
            return Optional.absent();
        }
        return Optional.of((T) value);
    }


    @Override
    public int hashCode() {
        return "FieldValueList".hashCode() + fields.hashCode() + valueMap.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof FieldValueList)) {
            return false;
        }
        FieldValueList other = (FieldValueList) obj;
        return this.fields.equals(other.fields) && this.valueMap.equals(other.valueMap);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a {@link Builder} which may be used to construct instances of the {@link FieldValueList} class.
     * @return A {@link Builder}.  Not {@code null}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a {@link Builder} that is initialised with the values in this {@link FieldValueList}.
     * @return The {@link Builder}.
     */
    public Builder builderFrom() {
        return new Builder(fields, valueMap);
    }


    /**
     * A {@link Builder} for creating {@link FieldValueList} instances.
     */
    public static class Builder {


        private Map<NoteField<? extends Serializable>, Serializable> map = new LinkedHashMap<NoteField<? extends Serializable>, Serializable>();


        private Builder() {
        }


        private Builder(List<NoteField<? extends Serializable>> fields, Map<NoteField<? extends Serializable>, Serializable> valueMap) {
            for(NoteField<? extends Serializable> field : fields) {
                map.put(field, valueMap.get(field));
            }
        }


        /**
         * Adds the specified value to the list of field values to build.
         * @param field The field to be added.
         * @param value The value of the field to be added.
         * @param <T> The field and value type.
         * @return This (as a convenience so that calls can be chained).
         * @throws NullPointerException if either {@code field} or {@code value} are {@code null}.
         */
        public <T extends Serializable> Builder addValue(NoteField<T> field, T value) {
            map.put(checkNotNull(field), checkNotNull(value));
            return this;
        }

        /**
         * Builds a {@link FieldValueList} from the values contained in this builder.
         * @return The resulting {@link FieldValueList}.  Not {@code null}.
         */
        public FieldValueList build() {
            return new FieldValueList(new ArrayList<NoteField<? extends Serializable>>(map.keySet()), map);
        }
    }
}

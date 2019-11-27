package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/04/16
 */
public class FormData extends FormDataValue implements Serializable, IsSerializable {

    @Nullable
    private OWLEntity subject;

    private FormDescriptor formDescriptor;

    @JsonUnwrapped
    private Map<String, FormDataValue> data = new HashMap<>();

    private FormData() {
    }

    public static FormData empty() {
        return new FormData(null, Collections.emptyMap(), FormDescriptor.empty());
    }

    public FormData(@Nullable OWLEntity subject,
                    @Nonnull Map<FormElementId, FormDataValue> data,
                    @Nonnull FormDescriptor formDescriptor) {
        this.formDescriptor = checkNotNull(formDescriptor);
        checkNotNull(data);
        this.subject = subject;
        data.forEach((id, val) -> {
            checkNotNull(id);
            checkNotNull(val);
            this.data.put(id.getId(), val);
        });
    }

    @Override
    public Optional<FormData> asFormData() {
        return Optional.of(this);
    }

    public Optional<OWLProperty> getOwlProperty(FormElementId formElementId) {
        return formDescriptor.getOwlProperty(formElementId);
    }

    @Nonnull
    public Optional<OWLEntity> getSubject() {
        return Optional.ofNullable(subject);
    }

    @Override
    public List<FormDataValue> asList() {
        return Collections.singletonList(this);
    }

    @Override
    public Optional<IRI> asIRI() {
        return Optional.empty();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public Optional<OWLEntity> asOWLEntity() {
        return Optional.empty();
    }

    public Map<FormElementId, FormDataValue> getData() {
        Map<FormElementId, FormDataValue> result = new HashMap<>();
        data.forEach((id, val) -> result.put(FormElementId.get(id), val));
        return result;
    }

    public boolean isEmpty() {
        if(data.isEmpty()) {
            return true;
        }
        for(FormDataValue value : data.values()) {
            if(!value.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Nonnull
    public FormDescriptor getFormDescriptor() {
        return formDescriptor;
    }

    @JsonIgnore
    public Optional<FormDataValue> getFormElementData(FormElementId formElementId) {
        return Optional.ofNullable(data.get(formElementId.getId()));
    }

    public static Builder builder(FormDescriptor formDescriptor) {
        return new Builder(formDescriptor);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormData)) {
            return false;
        }
        FormData other = (FormData) obj;
        return this.data.equals(other.data);
    }

    public static class Builder {

        private final ListMultimap<FormElementId, FormDataValue> builder_data = ArrayListMultimap.create();

        private final FormDescriptor formDescriptor;

        public Builder(FormDescriptor formDescriptor) {
            this.formDescriptor = formDescriptor;
        }

        public Builder addData(FormElementId elementId, FormDataValue dataValue) {
            builder_data.put(elementId, dataValue);
            return this;
        }

        public FormData build() {
            Map<FormElementId, FormDataValue> map = new HashMap<>();
            for(FormElementId elementId : builder_data.keys()) {
                map.put(elementId, new FormDataList(builder_data.get(elementId)));
            }
            return new FormData(null, map, formDescriptor);
        }
    }


    @Override
    public String toString() {
        return toStringHelper("FormData")
                .addValue(data)
                .toString();
    }

}

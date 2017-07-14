package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.stanford.bmir.protege.web.server.form.FormDataListSerializer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.*;
import java.util.function.Consumer;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
@JsonSerialize(using = FormDataListSerializer.class)
public class FormDataList extends FormDataValue {

    @JsonProperty
    private List<FormDataValue> list = new ArrayList<>();

    private FormDataList() {
    }

    public FormDataList(FormDataValue value) {
        list.add(value);
    }

    public FormDataList(List<FormDataValue> list) {
        this.list.addAll(checkNotNull(list));
    }

    @Override
    public List<FormDataValue> asList() {
        return new ArrayList<>(list);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public Optional<FormDataValue> getFirst() {
        if(list.isEmpty()) {
            return Optional.empty();
        }
        else {
            return Optional.of(list.get(0));
        }
    }

    @Override
    public Optional<OWLEntity> asOWLEntity() {
        return Optional.empty();
    }

    @Override
    public Optional<IRI> asIRI() {
        return Optional.empty();
    }

    public List<FormDataValue> getList() {
        return new ArrayList<>(list);
    }

    public static FormDataList of(FormDataValue value) {
        return new FormDataList(value);
    }

    public static FormDataList of(FormDataValue value, FormDataValue ... values) {
        List<FormDataValue> list = new ArrayList<>();
        list.add(value);
        Collections.addAll(list, values);
        return new FormDataList(list);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(list);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormDataList)) {
            return false;
        }
        FormDataList other = (FormDataList) obj;
        return this.list.equals(other.list);
    }


    @Override
    public String toString() {
        return toStringHelper("FormDataList")
                .addValue(list)
                .toString();
    }

    public static FormDataList empty() {
        return new FormDataList();
    }

    @Override
    public boolean isObject() {
        return false;
    }
}

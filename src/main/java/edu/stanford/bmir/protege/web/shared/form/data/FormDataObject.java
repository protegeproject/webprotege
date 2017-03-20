package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class FormDataObject extends FormDataValue {

    private Map<String, FormDataValue> map = new HashMap<>();

    public FormDataObject() {
    }

    public FormDataObject(Map<String, FormDataValue> map) {
        this.map.putAll(map);
    }

    public Optional<FormDataValue> get(String key) {
        return Optional.fromNullable(map.get(key));
    }

//    @Override
//    public Optional<OWLClassData> asOWLClassData() {
//        return Optional.absent();
//    }

    @Override
    public Optional<IRI> asIRI() {
        return Optional.absent();
    }

    @Override
    public List<FormDataValue> asList() {
        return Arrays.asList(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(map);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormDataObject)) {
            return false;
        }
        FormDataObject other = (FormDataObject) obj;
        return this.map.equals(other.map);
    }


    @Override
    public String toString() {
        return toStringHelper("FormDataObject")
                .addValue(map)
                .toString();
    }

    @Override
    public boolean isObject() {
        return true;
    }
}

package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class FormDataList extends FormDataValue {

    private List<FormDataValue> list = new ArrayList<>();

    private FormDataList() {
    }

    public FormDataList(FormDataValue value) {
        list.add(value);
    }

    public FormDataList(List<FormDataValue> list) {
        this.list.addAll(list);
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
            return Optional.absent();
        }
        else {
            return Optional.of(list.get(0));
        }
    }

    @Override
    public Optional<OWLClassData> asOWLClassData() {
        return Optional.absent();
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
}

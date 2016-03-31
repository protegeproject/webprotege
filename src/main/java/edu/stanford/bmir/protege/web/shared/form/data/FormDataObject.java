package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class FormDataObject implements FormDataValue {

    private Map<String, FormDataValue> map = new HashMap<>();

    public FormDataObject() {
    }

    public FormDataObject(Map<String, FormDataValue> map) {
        this.map.putAll(map);
    }

    public Optional<FormDataValue> get(String key) {
        return Optional.fromNullable(map.get(key));
    }

}

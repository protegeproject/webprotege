package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/04/16
 */
public class FormData implements Serializable, IsSerializable {

    private Map<FormElementId, FormDataList> data = new HashMap<>();

    private FormData() {
    }

    public static FormData empty() {
        return new FormData(Collections.emptyMap());
    }

    public FormData(Map<FormElementId, FormDataList> data) {
        this.data.putAll(data);
    }

    public Optional<FormDataList> getFormElementData(FormElementId formElementId) {
        return Optional.fromNullable(data.get(formElementId));
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {

        private final ListMultimap<FormElementId, FormDataValue> builder_data = ArrayListMultimap.create();

        public Builder() {
        }

        public Builder addData(FormElementId elementId, FormDataValue dataValue) {
            builder_data.put(elementId, dataValue);
            return this;
        }

        public FormData build() {
            Map<FormElementId, FormDataList> map = new HashMap<>();
            for(FormElementId elementId : builder_data.keys()) {
                map.put(elementId, new FormDataList(builder_data.get(elementId)));
            }
            return new FormData(map);
        }
    }

}

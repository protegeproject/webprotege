package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormElementData implements IsSerializable {

    private FormElementId formElementId;

    private List<FormDataTuple> values = new ArrayList<>();

    private FormElementData() {
    }

    public FormElementData(FormElementId formElementId, List<FormDataTuple> values) {
        this.formElementId = formElementId;
        this.values = values;
    }

    public FormElementId getFormElementId() {
        return formElementId;
    }

    public Optional<OWLPrimitiveData> getSingleValueData() {
        if(values.isEmpty()) {
            return Optional.absent();
        }
        else {
            return values.get(0).getSingleValueData();
        }
    }

    public List<FormDataTuple> getTuples() {
        return values;
    }


    @Override
    public String toString() {
        return toStringHelper("FormElementData")
                .addValue(formElementId)
                .addValue(values)
                .toString();
    }
}

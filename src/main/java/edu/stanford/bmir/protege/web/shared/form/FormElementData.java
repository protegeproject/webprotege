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

    private List<Tuple> tuples = new ArrayList<>();

    private FormElementData() {
    }

    public FormElementData(FormElementId formElementId, List<Tuple> values) {
        this.formElementId = formElementId;
        this.tuples.addAll(values);
    }

    public FormElementId getFormElementId() {
        return formElementId;
    }

    public Optional<OWLPrimitiveData> getSingleValueData() {
        if(tuples.isEmpty()) {
            return Optional.absent();
        }
        else {
            return tuples.get(0).getSingleValueData();
        }
    }

    public List<Tuple> getTuples() {
        return new ArrayList<>(tuples);
    }


    @Override
    public String toString() {
        return toStringHelper("FormElementData")
                .addValue(formElementId)
                .addValue(tuples)
                .toString();
    }
}

package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class Tuple implements IsSerializable {


    @SuppressWarnings("GwtInconsistentSerializableClass")
    private List<OWLPrimitiveData> data = new ArrayList<>();

    private Tuple() {
    }

    public Tuple(OWLPrimitiveData data) {
        this.data.add(data);
    }

    public Tuple(List<OWLPrimitiveData> data) {
        this.data.addAll(data);
    }

    public Optional<OWLPrimitiveData> getSingleValueData() {
        if(data.isEmpty()) {
            return Optional.absent();
        }
        else {
            return Optional.of(data.get(0));
        }
    }

    public List<OWLPrimitiveData> getData() {
        return data;
    }


    @Override
    public String toString() {
        return toStringHelper("FormDataTuple")
                .addValue(data)
                .toString();
    }
}

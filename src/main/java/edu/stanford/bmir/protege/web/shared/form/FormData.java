package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormData implements IsSerializable {

    private List<FormElementDescriptor> formElementDescriptors = new ArrayList<>();

    private Map<FormElementId, FormElementData> formElementData = new HashMap<>();

    private FormData() {
    }

    public FormData(List<FormElementDescriptor> formElementDescriptors) {
        this.formElementDescriptors = formElementDescriptors;
        FormElementId elementId = new FormElementId("TheComment");
        formElementData.put(elementId, new FormElementData(elementId, Arrays.asList(new Tuple(new OWLLiteralData(DataFactory.get().getOWLLiteral("My comment!!!!"))))));
    }

    public Optional<FormElementData> getFormElementData(FormElementId formElementId) {
        return Optional.fromNullable(formElementData.get(formElementId));
    }

    public List<FormElementDescriptor> getFormElementDescriptors() {
        return formElementDescriptors;
    }
}

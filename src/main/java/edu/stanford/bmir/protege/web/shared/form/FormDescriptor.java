package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormDescriptor implements Serializable {

    private List<FormElementDescriptor> formElementDescriptors = new ArrayList<>();

    private Map<FormElementId, FormDataList> formElementData = new HashMap<>();

    private FormDescriptor() {
    }

    public FormDescriptor(List<FormElementDescriptor> formElementDescriptors) {
        this.formElementDescriptors = formElementDescriptors;
        FormElementId elementId = new FormElementId("TheComment");
        formElementData.put(elementId, FormDataList.of(FormDataPrimitive.get("My comment!!!")));
        formElementData.put(new FormElementId("EngineConfiguration"),
                FormDataList.of(
                        FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/TrJWTM")),
                        FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/QJWM"))
                )
        );
        formElementData.put(new FormElementId("PossibleRoles"), FormDataList.of(
                FormDataList.of(FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/Cargo")),
                        FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/Passenger"))
                )
                )
        );
        formElementData.put(new FormElementId("TheLabel"), FormDataList.of(
                FormDataPrimitive.get("My label")
        ));
    }

    public Optional<FormDataList> getFormElementData(FormElementId formElementId) {
        return Optional.fromNullable(formElementData.get(formElementId));
    }

    public List<FormElementDescriptor> getFormElementDescriptors() {
        return formElementDescriptors;
    }
}

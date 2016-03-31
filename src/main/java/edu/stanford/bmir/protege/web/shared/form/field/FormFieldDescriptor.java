package edu.stanford.bmir.protege.web.shared.form.field;


import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FormFieldDescriptor extends IsSerializable {

    String getAssociatedFieldTypeId();
}

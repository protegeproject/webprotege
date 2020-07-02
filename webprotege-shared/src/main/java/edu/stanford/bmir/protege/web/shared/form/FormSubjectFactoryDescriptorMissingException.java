package edu.stanford.bmir.protege.web.shared.form;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class FormSubjectFactoryDescriptorMissingException extends RuntimeException implements IsSerializable {

    public FormSubjectFactoryDescriptorMissingException() {
        super("Subject factory descriptor is missing.  Improperly configured form descriptor.");
    }
}

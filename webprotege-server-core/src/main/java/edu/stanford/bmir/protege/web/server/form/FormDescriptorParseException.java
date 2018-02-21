package edu.stanford.bmir.protege.web.server.form;

import com.google.gson.JsonParseException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/04/16
 */
public class FormDescriptorParseException extends JsonParseException {

    public FormDescriptorParseException(String msg) {
        super(msg);
    }
}

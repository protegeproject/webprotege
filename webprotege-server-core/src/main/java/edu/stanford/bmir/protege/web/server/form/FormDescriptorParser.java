package edu.stanford.bmir.protege.web.server.form;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/04/16
 */
public class FormDescriptorParser {

    public FormDescriptor parser(String input) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FormDescriptor.class, new FormDescriptorDeserializer());
        builder.registerTypeAdapter(FormElementDescriptor.class, new FormElementDescriptorDeserializer());
        Gson gson = builder.create();
        return gson.fromJson(input, FormDescriptor.class);
    }
}

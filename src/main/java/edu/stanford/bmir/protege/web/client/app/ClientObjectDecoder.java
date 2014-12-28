package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.json.client.JSONValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/12/14
 */
public interface ClientObjectDecoder<T> {

    T decode(JSONValue json);

}

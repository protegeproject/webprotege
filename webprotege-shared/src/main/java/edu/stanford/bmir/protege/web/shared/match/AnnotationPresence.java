package edu.stanford.bmir.protege.web.shared.match;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
public enum AnnotationPresence implements IsSerializable {

    AT_LEAST_ONE,

    AT_MOST_ONE,

    NONE
}

package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.annotations.GwtIncompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
public interface SimpleStringCriteria extends LexicalValueCriteria {

    String IGNORE_CASE = "ignoreCase";

    String VALUE = "value";

    String getValue();

    boolean isIgnoreCase();
}

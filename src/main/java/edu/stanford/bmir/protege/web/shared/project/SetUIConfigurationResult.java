package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class SetUIConfigurationResult implements Result {

    @Override
    public int hashCode() {
        return "SetUIConfigurationResult".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof SetUIConfigurationResult;
    }


    @Override
    public String toString() {
        return toStringHelper("SetUIConfigurationResult")
                .toString();
    }
}

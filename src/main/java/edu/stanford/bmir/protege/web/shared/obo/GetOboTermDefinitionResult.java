package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2017
 */
public class GetOboTermDefinitionResult implements Result {

    private OBOTermDefinition definition;

    @GwtSerializationConstructor
    private GetOboTermDefinitionResult() {
    }

    public GetOboTermDefinitionResult(@Nonnull OBOTermDefinition definition) {
        this.definition = checkNotNull(definition);
    }

    public OBOTermDefinition getDefinition() {
        return definition;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermDefinitionResult)) {
            return false;
        }
        GetOboTermDefinitionResult other = (GetOboTermDefinitionResult) obj;
        return this.definition.equals(other.definition);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermDefinitionResult")
                .addValue(definition)
                .toString();
    }
}

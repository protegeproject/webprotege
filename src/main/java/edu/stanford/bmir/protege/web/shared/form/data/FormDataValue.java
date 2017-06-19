package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public abstract class FormDataValue implements IsSerializable {

    /**
     * Obtain this value as a literal.
     * @return The value as a literal, or absent if this value is not a primitive value that wraps
     * a literal.
     */
    public Optional<OWLLiteral> asLiteral() {
        return Optional.empty();
    }

    /**
     * Obtain this value as a list of values.
     * @return This value as a list of values.  If this value is a {@link FormDataList}
     * then the values contained in the list will be returned, otherwise this value as a one element list.
     */
    public abstract List<FormDataValue> asList();

    public abstract Optional<IRI> asIRI();

    public abstract boolean isObject();
}

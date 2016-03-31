package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.IsSerializable;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public interface FormDataValue extends IsSerializable {

    default Optional<OWLPrimitive> asPrimitive() {
        return Optional.absent();
    }

    default Optional<OWLLiteral> asLiteral() {
        return Optional.absent();
    }

    default List<OWLLiteral> asLiteralList() {
        return Collections.emptyList();
    }
}

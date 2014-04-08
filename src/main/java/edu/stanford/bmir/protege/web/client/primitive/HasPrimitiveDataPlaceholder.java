package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 28/02/2014
 */
public interface HasPrimitiveDataPlaceholder {

    void setPrimitiveDataPlaceholder(Optional<OWLPrimitiveData> placeholder);

    Optional<OWLPrimitiveData> getPrimitiveDataPlaceholder();

}

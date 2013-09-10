package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/01/2013
 */
public interface PrimitiveDataParserCallback {

    void parsingFailure();

    void onSuccess(Optional<OWLPrimitiveData> result);
}

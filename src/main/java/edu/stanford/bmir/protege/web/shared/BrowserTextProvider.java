package edu.stanford.bmir.protege.web.shared;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/12/2012
 */
public interface BrowserTextProvider {

    Optional<String> getOWLEntityBrowserText(OWLEntity entity);

}

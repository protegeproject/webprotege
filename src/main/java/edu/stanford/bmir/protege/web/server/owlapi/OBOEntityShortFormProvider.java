package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.ShortFormProvider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/06/2012
 */
public class OBOEntityShortFormProvider implements ShortFormProvider {

    private WebProtegeShortFormProvider delegate;

    public OBOEntityShortFormProvider(OWLAPIProject project) {
        delegate = new WebProtegeShortFormProvider(project);
    }

    public String getShortForm(OWLEntity entity) {
        String shortForm = delegate.getShortForm(entity);
        if(shortForm.startsWith("'")) {
            int end = shortForm.length();
            if(shortForm.endsWith("'")) {
                end = shortForm.length() - 1;
            }
            return shortForm.substring(1, end);
        }
        return shortForm;
    }

    public void dispose() {
    }
}

package edu.stanford.bmir.protege.web.server.owlapi;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2013
 */

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A short form provider which wraps a non-escaping short form provider to ensure that short forms with spaces
 * are surrounded in single quotes.
 */
public class EscapingShortFormProvider implements BidirectionalShortFormProvider {

    private BidirectionalShortFormProvider delegate;

    public EscapingShortFormProvider(BidirectionalShortFormProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public Set<OWLEntity> getEntities(String s) {
        String stripped = getStripped(s);
        return delegate.getEntities(stripped);
    }

    private String getStripped(String s) {
        String stripped;
        if(s.startsWith("'") && s.endsWith("'")) {
            stripped = s.substring(1, s.length() - 1);
        }
        else {
            stripped = s;
        }
        return stripped;
    }

    @Override
    public OWLEntity getEntity(String s) {
        return delegate.getEntity(getStripped(s));
    }

    @Override
    public Set<String> getShortForms() {
        Set<String> rawShortForms = delegate.getShortForms();
        List<String> escapedShortForms = new ArrayList<String>(rawShortForms.size() + 1);
        for(String rawShortForm : rawShortForms) {
            String escapedShortForm = getEscapedShortForm(rawShortForm);
            escapedShortForms.add(escapedShortForm);
        }
        return new HashSet<String>(escapedShortForms);
    }

    private String getEscapedShortForm(String rawShortForm) {
        String escapedShortForm;
        if(rawShortForm.indexOf(' ') != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append("'");
            sb.append(rawShortForm);
            sb.append("'");
            escapedShortForm = sb.toString();
        }
        else {
            escapedShortForm = rawShortForm;
        }
        return escapedShortForm;
    }

    @Override
    public String getShortForm(OWLEntity entity) {
        return getEscapedShortForm(delegate.getShortForm(entity));
    }

    @Override
    public void dispose() {
        delegate.dispose();
    }
}

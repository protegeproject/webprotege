package edu.stanford.bmir.protege.web.client.ontology.attestation;

import ch.unifr.digits.webprotege.attestation.shared.GetProjectOntologyIdResult;
import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Set;

public interface AttestationView extends ValueEditor<OWLOntologyID>, HasEnabled {
    void setContractAddress(String caddress);
    void setIDs(Set<OWLOntologyID> ids);
}

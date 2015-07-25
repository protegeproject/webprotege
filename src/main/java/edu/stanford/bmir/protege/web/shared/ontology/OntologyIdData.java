package edu.stanford.bmir.protege.web.shared.ontology;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.entity.ObjectData;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/07/15
 */
public class OntologyIdData extends ObjectData<OWLOntologyID> {

    private final String browserText;

    public OntologyIdData(OWLOntologyID ontologyID, String browserText) {
        super(checkNotNull(ontologyID));
        this.browserText = checkNotNull(browserText);
    }

    @Override
    public String getBrowserText() {
        return browserText;
    }

    @Override
    public String getUnquotedBrowserText() {
        return browserText;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("OntologyIdData")
                .addValue(getObject())
                .addValue(browserText)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getObject(), browserText);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OntologyIdData)) {
            return false;
        }
        OntologyIdData other = (OntologyIdData) obj;
        return this.getObject().equals(other.getObject()) && this.browserText.equals(other.browserText);
    }
}

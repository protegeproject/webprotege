package edu.stanford.bmir.protege.web.client.library.checkbox;

import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CheckBox;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomTypeGroup;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.Optional;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/07/2013
 */
public class AxiomTypeGroupCheckBox extends CheckBox {

    private AxiomTypeGroup axiomTypeGroup;

    public AxiomTypeGroupCheckBox() {
    }

    public AxiomTypeGroupCheckBox(SafeHtml label) {
        super(label);
    }

    public AxiomTypeGroupCheckBox(SafeHtml label, HasDirection.Direction dir) {
        super(label, dir);
    }

    public AxiomTypeGroupCheckBox(SafeHtml label, DirectionEstimator directionEstimator) {
        super(label, directionEstimator);
    }

    public AxiomTypeGroupCheckBox(String label) {
        super(label);
    }

    public AxiomTypeGroupCheckBox(String label, HasDirection.Direction dir) {
        super(label, dir);
    }

    public AxiomTypeGroupCheckBox(String label, DirectionEstimator directionEstimator) {
        super(label, directionEstimator);
    }

    public AxiomTypeGroupCheckBox(String label, boolean asHTML) {
        super(label, asHTML);
    }

    public AxiomTypeGroupCheckBox(Element elem) {
        super(elem);
    }

    public Optional<AxiomTypeGroup> getAxiomTypeGroup() {
        return Optional.ofNullable(axiomTypeGroup);
    }

    public void setAxiomTypeGroup(AxiomTypeGroup axiomTypeGroup) {
        this.axiomTypeGroup = axiomTypeGroup;
        final Optional<OWLRDFVocabulary> vocab = axiomTypeGroup.getOWLRDFVocabulary();
        String vocabSF = "";
        if(vocab.isPresent()) {
            vocabSF = "(" + vocab.get().getNamespace().name().toLowerCase() + ":" + vocab.get().getShortForm() + ")";
        }
        setHTML(axiomTypeGroup.getDisplayName() + "   <span style=\"color: gray; font-size: 90%;\">" + vocabSF + "</span>");

    }
}

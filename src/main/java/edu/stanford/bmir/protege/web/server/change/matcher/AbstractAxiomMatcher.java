package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.inject.TypeLiteral;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public abstract class AbstractAxiomMatcher<A extends OWLAxiom> implements ChangeMatcher {

    private final TypeLiteral<A> axiomCls;




    public AbstractAxiomMatcher(TypeLiteral<A> axiomCls) {
        this.axiomCls = axiomCls;
    }

    @Override
    public final Optional<String> getDescription(ChangeApplicationResult<?> result) {
        if(result.getChangeList().size() != 1) {
            return Optional.empty();
        }
        OWLOntologyChange change = result.getChangeList().get(0);
        if(!(change instanceof OWLAxiomChange)) {
            return Optional.empty();
        }
        OWLAxiom axiom = change.getAxiom();
        if(!axiomCls.getRawType().isInstance(axiom)) {
            return Optional.empty();
        }
        if(change.isAddAxiom()) {
            return getDescriptionForAddAxiomChange((A)axiom);
        }
        else {
            return getDescriptionForRemoveAxiomChange((A)axiom);
        }
    }

    protected abstract Optional<String> getDescriptionForAddAxiomChange(A axiom);

    protected abstract Optional<String> getDescriptionForRemoveAxiomChange(A axiom);
}

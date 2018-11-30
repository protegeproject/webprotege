package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.AxiomChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public abstract class AbstractAxiomMatcher<A extends OWLAxiom> implements ChangeMatcher {

    private final TypeToken<A> axiomCls;

    public AbstractAxiomMatcher(TypeToken<A> axiomCls) {
        this.axiomCls = axiomCls;
    }

    @Override
    public final Optional<String> getDescription(List<OWLOntologyChangeData> changeData) {
        if(changeData.size() != 1) {
            return Optional.empty();
        }
        OWLOntologyChangeData change = changeData.get(0);
        if(!(change instanceof AxiomChangeData)) {
            return Optional.empty();
        }
        OWLAxiom axiom = ((AxiomChangeData) change).getAxiom();
        if(!axiomCls.getRawType().isInstance(axiom)) {
            return Optional.empty();
        }
        if(change instanceof AddAxiomData) {
            return getDescriptionForAddAxiomChange((A)axiom);
        }
        else {
            return getDescriptionForRemoveAxiomChange((A)axiom);
        }
    }

    protected abstract Optional<String> getDescriptionForAddAxiomChange(A axiom);

    protected abstract Optional<String> getDescriptionForRemoveAxiomChange(A axiom);
}

package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.AxiomChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
        var nonDeclarationChangeData = getNonDeclarationChangeData(changeData);
        if(nonDeclarationChangeData.size() != 1) {
            return Optional.empty();
        }
        var firstChange = nonDeclarationChangeData.get(0);
        if(!(firstChange instanceof AxiomChangeData)) {
            return Optional.empty();
        }
        OWLAxiom axiom = ((AxiomChangeData) firstChange).getAxiom();
        if(!axiomCls.getRawType().isInstance(axiom)) {
            return Optional.empty();
        }
        if(firstChange instanceof AddAxiomData) {
            return getDescriptionForAddAxiomChange((A) axiom);
        }
        else {
            return getDescriptionForRemoveAxiomChange((A) axiom);
        }
    }

    private List<OWLOntologyChangeData> getNonDeclarationChangeData(List<OWLOntologyChangeData> changeData) {
        if(allowSignatureDeclarations()) {
            var entityCreationAxiomSubjectProvider = new EntityCreationAxiomSubjectProvider();
            return changeData.stream()
                    .filter(data -> {
                        if(data instanceof AddAxiomData) {
                            var axiom = ((AddAxiomData) data).getAxiom();
                            var subject = entityCreationAxiomSubjectProvider.getEntityCreationAxiomSubject(axiom);
                            return subject.isEmpty();
                        }
                        else {
                            return true;
                        }
                    })
                    .collect(Collectors.toList());
        }
        else {
            return changeData;
        }
    }

    protected abstract Optional<String> getDescriptionForAddAxiomChange(A axiom);

    protected abstract Optional<String> getDescriptionForRemoveAxiomChange(A axiom);

    protected boolean allowSignatureDeclarations() {
        return false;
    }
}

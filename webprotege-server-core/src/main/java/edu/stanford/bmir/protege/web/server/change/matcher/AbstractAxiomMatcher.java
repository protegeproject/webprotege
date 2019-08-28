package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.AxiomChangeData;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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
    public final Optional<ChangeSummary> getDescription(List<OntologyChange> changeData) {
        List<OntologyChange> coreChangeData;
        if(changeData.size() != 1) {
            var nonDeclarationChangeData = getNonDeclarationChangeData(changeData);
            if(nonDeclarationChangeData.size() != 1) {
                return Optional.empty();
            }
            else {
                coreChangeData = nonDeclarationChangeData;
            }
        }
        else {
            coreChangeData = changeData;
        }
        var firstChange = coreChangeData.get(0);
        if(!(firstChange instanceof AxiomChangeData)) {
            return Optional.empty();
        }
        OWLAxiom axiom = ((AxiomChangeData) firstChange).getAxiom();
        if(!axiomCls.getRawType().isInstance(axiom)) {
            return Optional.empty();
        }
        if(firstChange instanceof AddAxiomData) {
            return getDescriptionForAddAxiomChange((A) axiom, changeData);
        }
        else {
            return getDescriptionForRemoveAxiomChange((A) axiom);
        }
    }

    private List<OntologyChange> getNonDeclarationChangeData(List<OntologyChange> changes) {
        // Inline declarations consist of an entity declaration axiom and zero
        // or more annotations assertions with a subject equal to the IRI of the
        // declared entity
        if(allowSignatureDeclarations()) {
            var subjectProvider = new EntityCreationAxiomSubjectProvider();
            var potentialInlineEntityDeclarationChanges = changes.stream()
                    .filter(this::isPotentialInlineDeclarationChange)
                    .collect(groupingBy(data -> {
                        var axiom = data.getAxiomOrThrow();
                        return subjectProvider.getEntityCreationAxiomSubject(axiom);
                    }));
            var declarationChangeData = potentialInlineEntityDeclarationChanges
                    .values()
                    .stream()
                    .filter(this::containsDeclarationAxiomChange)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            return changes.stream()
                    .filter(data -> !declarationChangeData.contains(data))
                    .collect(Collectors.toList());
        }
        else {
            return changes;
        }
    }

    private boolean containsDeclarationAxiomChange(List<OntologyChange> dataList) {
        return dataList
                .stream()
                .filter(OntologyChange::isAxiomChange)
                .anyMatch(chg -> chg.getAxiomOrThrow() instanceof OWLDeclarationAxiom);
    }

    private boolean isPotentialInlineDeclarationChange(OntologyChange data) {
        if(data instanceof AddAxiomData) {
            var axiom = ((AddAxiomData) data).getAxiom();
            if(axiom instanceof OWLDeclarationAxiom) {
                return true;
            }
            else return axiom instanceof OWLAnnotationAssertionAxiom;
        }
        else {
            return false;
        }
    }

    protected abstract Optional<ChangeSummary> getDescriptionForAddAxiomChange(A axiom,
                                                                               List<OntologyChange> changes);

    protected abstract Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(A axiom);

    protected boolean allowSignatureDeclarations() {
        return false;
    }
}

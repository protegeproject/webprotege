package edu.stanford.bmir.protege.web.server.bulkop;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.SubClassOfAxiomsBySubClassIndex;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MoveClassesChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final ImmutableSet<OWLClass> childClasses;

    @Nonnull
    private final OWLClass targetParent;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologies;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassAxiomIndex;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final String commitMessage;

    @Inject
    @AutoFactory
    public MoveClassesChangeListGenerator(@Nonnull ImmutableSet<OWLClass> childClasses,
                                          @Nonnull OWLClass targetParent,
                                          @Nonnull String commitMessage,
                                          @Provided @Nonnull ProjectOntologiesIndex projectOntologies,
                                          @Provided @Nonnull SubClassOfAxiomsBySubClassIndex subClassAxiomIndex,
                                          @Provided @Nonnull OWLDataFactory dataFactory) {
        this.childClasses = checkNotNull(childClasses);
        this.targetParent = checkNotNull(targetParent);
        this.projectOntologies = checkNotNull(projectOntologies);
        this.subClassAxiomIndex = checkNotNull(subClassAxiomIndex);
        this.dataFactory = checkNotNull(dataFactory);
        this.commitMessage = checkNotNull(commitMessage);
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        var changeList = new OntologyChangeList.Builder<Boolean>();
        projectOntologies.getOntologyIds().forEach(ontId -> {
            childClasses.forEach(childClass -> {
                subClassAxiomIndex
                        .getSubClassOfAxiomsForSubClass(childClass, ontId)
                        .filter(ax -> ax.getSuperClass().isNamed())
                        .filter(ax -> !ax.getSuperClass().equals(targetParent))
                        .forEach(ax -> processAxiom(ax, childClass, ontId, changeList));
            });
        });
        return changeList.build(true);
    }

    private void processAxiom(OWLSubClassOfAxiom ax,
                              OWLClass childCls,
                              OWLOntologyID ontId,
                              OntologyChangeList.Builder<Boolean> changeList) {
        var removeAxiom = RemoveAxiomChange.of(ontId, ax);
        changeList.add(removeAxiom);
        var replacementAx = dataFactory.getOWLSubClassOfAxiom(childCls, targetParent, ax.getAnnotations());
        var addAxiom = AddAxiomChange.of(ontId, replacementAx);
        changeList.add(addAxiom);
    }

    @Override
    public Boolean getRenamedResult(Boolean result,
                                    RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        return commitMessage;
    }
}

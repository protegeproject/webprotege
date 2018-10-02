package edu.stanford.bmir.protege.web.server.bulkop;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.*;

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
    private final ImmutableSet<OWLClass> entities;

    @Nonnull
    private final OWLClass parent;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final MessageFormatter messageFormatter;

    @Nonnull
    private final String commitMessage;

    @Inject
    @AutoFactory
    public MoveClassesChangeListGenerator(@Nonnull ImmutableSet<OWLClass> clses,
                                          @Nonnull OWLClass parent,
                                          @Provided @Nonnull OWLOntology rootOntology,
                                          @Provided @Nonnull OWLDataFactory dataFactory,
                                          @Provided @Nonnull MessageFormatter messageFormatter,
                                          @Nonnull String commitMessage) {
        this.entities = checkNotNull(clses);
        this.parent = checkNotNull(parent);
        this.rootOntology = checkNotNull(rootOntology);
        this.dataFactory = checkNotNull(dataFactory);
        this.messageFormatter = checkNotNull(messageFormatter);
        this.commitMessage = checkNotNull(commitMessage);
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Boolean> changeList = new OntologyChangeList.Builder<>();
        for (OWLClass entity : entities) {
            for(OWLOntology ontology : rootOntology.getImportsClosure()) {
                for(OWLSubClassOfAxiom ax : ontology.getSubClassAxiomsForSubClass(entity)) {
                    processAxiom(ax, entity, ontology, changeList);
                }
            }
        }
        return changeList.build(true);
    }

    private void processAxiom(OWLSubClassOfAxiom ax, OWLClass parentClass, OWLOntology ontology, OntologyChangeList.Builder<Boolean> changeList) {
        OWLClassExpression superClass = ax.getSuperClass();
        if (superClass.isAnonymous()) {
            return;
        }
        if (superClass.equals(parent)) {
            return;
        }
        changeList.removeAxiom(ontology, ax);
        OWLSubClassOfAxiom replacementAx = dataFactory.getOWLSubClassOfAxiom(parentClass,
                                                                             parent,
                                                                             ax.getAnnotations());
        changeList.addAxiom(ontology, replacementAx);
    }

    @Override
    public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        return commitMessage;
    }
}

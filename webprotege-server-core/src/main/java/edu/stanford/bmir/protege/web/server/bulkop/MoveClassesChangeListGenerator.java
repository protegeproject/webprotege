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

    @Inject
    @AutoFactory
    public MoveClassesChangeListGenerator(@Nonnull ImmutableSet<OWLClass> clses,
                                          @Nonnull OWLClass parent,
                                          @Provided @Nonnull OWLOntology rootOntology,
                                          @Provided @Nonnull OWLDataFactory dataFactory,
                                          @Provided @Nonnull MessageFormatter messageFormatter) {
        this.entities = checkNotNull(clses);
        this.parent = checkNotNull(parent);
        this.rootOntology = checkNotNull(rootOntology);
        this.dataFactory = checkNotNull(dataFactory);
        this.messageFormatter = checkNotNull(messageFormatter);
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Boolean> changeList = new OntologyChangeList.Builder<>();
        for (OWLClass entity : entities) {
            for(OWLOntology ontology : rootOntology.getImportsClosure()) {
                for(OWLSubClassOfAxiom ax : ontology.getSubClassAxiomsForSubClass(entity)) {
                    OWLClassExpression superClass = ax.getSuperClass();
                    if(!superClass.isAnonymous() && !superClass.equals(parent)) {
                        changeList.removeAxiom(ontology, ax);
                        OWLSubClassOfAxiom replacementAx = dataFactory.getOWLSubClassOfAxiom(entity,
                                                                                                  parent,
                                                                                                  ax.getAnnotations());
                        changeList.addAxiom(ontology, replacementAx);
                    }
                }
            }
        }
        return changeList.build(true);
    }

    @Override
    public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        return messageFormatter.format("Moved classes to {0}", parent);
    }
}

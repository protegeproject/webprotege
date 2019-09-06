package edu.stanford.bmir.protege.web.server.frame;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange.of;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 14/01/2013
 */
public final class FrameChangeGenerator implements ChangeListGenerator<OWLEntityData> {

    @Nonnull
    private final FrameUpdate frameUpdate;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ReverseEngineeredChangeDescriptionGeneratorFactory factory;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final OntologyAxiomsIndex axiomsIndex;

    @Nonnull
    private final ClassFrameTranslator classFrameTranslator;

    @Nonnull
    private final ObjectPropertyFrameTranslator objectPropertyFrameTranslator;

    @Nonnull
    private final DataPropertyFrameTranslator dataPropertyFrameTranslator;

    @Nonnull
    private final AnnotationPropertyFrameTranslator annotationPropertyFrameTranslator;

    @Nonnull
    private final NamedIndividualFrameTranslator namedIndividualFrameTranslator;

    @AutoFactory
    public FrameChangeGenerator(@Nonnull FrameUpdate frameUpdate,
                                @Provided @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                @Provided @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory factory,
                                @Provided @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                @Provided @Nonnull OntologyAxiomsIndex axiomsIndex,
                                @Provided @Nonnull ClassFrameTranslator classFrameTranslator,
                                @Provided @Nonnull ObjectPropertyFrameTranslator objectPropertyFrameTranslator,
                                @Provided @Nonnull DataPropertyFrameTranslator dataPropertyFrameTranslator,
                                @Provided @Nonnull AnnotationPropertyFrameTranslator annotationPropertyFrameTranslator,
                                @Provided @Nonnull NamedIndividualFrameTranslator namedIndividualFrameTranslator) {
        this.frameUpdate = checkNotNull(frameUpdate);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.factory = checkNotNull(factory);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
        this.axiomsIndex = checkNotNull(axiomsIndex);
        this.classFrameTranslator = checkNotNull(classFrameTranslator);
        this.objectPropertyFrameTranslator = objectPropertyFrameTranslator;
        this.dataPropertyFrameTranslator = dataPropertyFrameTranslator;
        this.annotationPropertyFrameTranslator = annotationPropertyFrameTranslator;
        this.namedIndividualFrameTranslator = namedIndividualFrameTranslator;
    }

    @Override
    public OntologyChangeList<OWLEntityData> generateChanges(ChangeGenerationContext context) {
        var builder = new OntologyChangeList.Builder<OWLEntityData>();
        builder.addAll(createChanges());
        return builder.build(frameUpdate.getToFrame().getSubject());
    }

    private Set<OWLAxiom> getAxiomsForFrame(Frame<?> frame, Mode mode) {
        if(frame instanceof ClassFrame) {
            var classFrame = (ClassFrame) frame;
            return classFrameTranslator.getAxioms(classFrame, mode);
        }
        else if(frame instanceof ObjectPropertyFrame) {
            var objectPropertyFrame = (ObjectPropertyFrame) frame;
            return objectPropertyFrameTranslator.getAxioms(objectPropertyFrame, mode);
        }
        else if(frame instanceof DataPropertyFrame) {
            var dataPropertyFrame = (DataPropertyFrame) frame;
            return dataPropertyFrameTranslator.getAxioms(dataPropertyFrame, mode);
        }
        else if(frame instanceof AnnotationPropertyFrame) {
            var annotationPropertyFrame = (AnnotationPropertyFrame) frame;
            return annotationPropertyFrameTranslator.getAxioms(annotationPropertyFrame, mode);

        }
        else if(frame instanceof NamedIndividualFrame) {
            var namedIndividualFrame = (NamedIndividualFrame) frame;
            return namedIndividualFrameTranslator.getAxioms(namedIndividualFrame, mode);

        }
        else {
            throw new RuntimeException("Cannot determine translator type");
        }
    }

    private Frame<?> getFrameForSubject(OWLEntityData subject) {
        if(subject instanceof OWLClassData) {
            return classFrameTranslator.getFrame((OWLClassData) subject);
        }
        else if(subject instanceof OWLObjectPropertyData) {
            return objectPropertyFrameTranslator.getFrame((OWLObjectPropertyData) subject);
        }
        else if(subject instanceof OWLDataPropertyData) {
            return dataPropertyFrameTranslator.getFrame((OWLDataPropertyData) subject);
        }
        else if(subject instanceof OWLAnnotationPropertyData) {
            return annotationPropertyFrameTranslator.getFrame((OWLAnnotationPropertyData) subject);

        }
        else if(subject instanceof OWLNamedIndividualData) {
            return namedIndividualFrameTranslator.getFrame((OWLNamedIndividualData) subject);
        }
        else {
            throw new RuntimeException("Cannot determine translator type");
        }
    }

    public List<OntologyChange> createChanges() {
        // TODO: Consider axiom annotations!

        // TODO: Three way merge incase the frame has been modified "externally" and is different from the original
        // TODO: from frame


        // This looks like it is more complicated than necessary, however we need to consider the fact that
        // a frame may be generated from multiple axioms (note the minimal and maximal translation)

        var to = frameUpdate.getToFrame();
        var toSubject = to.getSubject();
        Frame serverFrame = getFrameForSubject(toSubject);
        if(serverFrame.equals(to)) {
            // Nothing to do
            return Collections.emptyList();
        }


        // Get the axioms that were consumed in the translation
        var fromAxioms = getAxiomsForFrame(frameUpdate.getFromFrame(), Mode.MAXIMAL);

        var ontologyIds = projectOntologiesIndex.getOntologyIds()
                                                .collect(toList());

        var toAxioms = getAxiomsForFrame(frameUpdate.getToFrame(), Mode.MINIMAL);


        var axiom2OntologyMap = LinkedHashMultimap.<OWLAxiom, OWLOntologyID>create();

        // Generate a map of existing axioms so we can ensure they stay in the correct place
        for(OWLOntologyID ontologyId : ontologyIds) {
            for(OWLAxiom fromAxiom : fromAxioms) {
                if(isContainedInOntology(fromAxiom, ontologyId)) {
                    axiom2OntologyMap.put(fromAxiom, ontologyId);
                }
            }
        }


        var mutatedOntologies = Sets.<OWLOntologyID>newLinkedHashSet();
        List<OntologyChange> changes = Lists.newArrayList();
        for(OWLAxiom fromAxiom : fromAxioms) {
            for(OWLOntologyID ontologyId : ontologyIds) {
                if(isContainedInOntology(fromAxiom, ontologyId) && !toAxioms.contains(fromAxiom)) {
                    mutatedOntologies.add(ontologyId);
                }
                // We need to add this here in case an axiom containing fresh entities has been added and then
                // removed (caused by a re-edit).  The fresh entities will get "grounded" and then the axiom added
                // hence, ontology.contains() won't work.
                changes.add(of(ontologyId, fromAxiom));
            }
        }


        for(OWLAxiom toAxiom : toAxioms) {
            Collection<OWLOntologyID> existingLocations = axiom2OntologyMap.get(toAxiom);
            if(existingLocations.isEmpty()) {
                // Fresh axiom to be placed somewhere
                if(mutatedOntologies.size() == 1) {
                    // Assume edit i.e. replacement of axiom in the same ontology.
                    changes.add(AddAxiomChange.of(mutatedOntologies.iterator()
                                                                              .next(), toAxiom));
                }
                else {
                    // Multiple ontologies were affected.  We now need to place the fresh axiom in the appropriate
                    // ontology
                    OWLOntologyID freshAxiomOntology = getFreshAxiomOntology(fromAxioms, ontologyIds);
                    changes.add(AddAxiomChange.of(freshAxiomOntology, toAxiom));
                }
            }
            else {
                // Ensure it is still in there
                for(OWLOntologyID ontId : existingLocations) {
                    changes.add(AddAxiomChange.of(ontId, toAxiom));
                }
            }
        }
        return changes;
    }

    private boolean isContainedInOntology(OWLAxiom axiom, OWLOntologyID ontologyId) {
        return axiomsIndex.containsAxiomIgnoreAnnotations(axiom, ontologyId);
    }

    private OWLOntologyID getFreshAxiomOntology(Set<OWLAxiom> fromAxioms, List<OWLOntologyID> importsClosure) {
        for(OWLOntologyID ontId : importsClosure) {
            for(OWLAxiom existingFrameAxiom : fromAxioms) {
                if(isContainedInOntology(existingFrameAxiom, ontId)) {
                    return ontId;
                }
            }
        }
        return defaultOntologyIdManager.getDefaultOntologyId();
    }

    @Override
    public OWLEntityData getRenamedResult(OWLEntityData result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<OWLEntityData> result) {
        return factory.get("Edited " + frameUpdate.getFromFrame().getSubject()
                                                  .getBrowserText())
                .generateChangeDescription(result);
    }
}

package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.frame.EmptyEntityFrameFactory;
import edu.stanford.bmir.protege.web.server.frame.FrameChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.frame.FrameUpdate;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormEntitySubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormIriSubject;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class EntityFormChangeListGenerator implements ChangeListGenerator<OWLEntity> {

    @Nonnull
    private final EntityFormDataConverter entityFormDataConverter;

    @Nonnull
    private final MessageFormatter messageFormatter;

    @Nonnull
    private final ImmutableList<FormData> pristineFormsData;

    @Nonnull
    private final ImmutableList<FormData> editedFormsData;

    @Nonnull
    private final FrameChangeGeneratorFactory frameChangeGeneratorFactory;

    @Nonnull
    private final FormFrameConverter formFrameConverter;

    @Nonnull
    private final EmptyEntityFrameFactory emptyEntityFrameFactory;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final OWLEntity subject;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;


    @Inject
    public EntityFormChangeListGenerator(@Nonnull OWLEntity subject,
                                         @Nonnull ImmutableList<FormData> pristineFormsData,
                                         @Nonnull ImmutableList<FormData> editedFormData,
                                         @Nonnull EntityFormDataConverter entityFormDataConverter,
                                         @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory,
                                         @Nonnull MessageFormatter messageFormatter,
                                         @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                         @Nonnull FormFrameConverter formFrameConverter,
                                         @Nonnull EmptyEntityFrameFactory emptyEntityFrameFactory,
                                         @Nonnull RenderingManager renderingManager,
                                         @Nonnull OWLDataFactory dataFactory,
                                         @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        this.subject = checkNotNull(subject);
        this.pristineFormsData = checkNotNull(pristineFormsData);
        this.editedFormsData = checkNotNull(editedFormData);
        this.entityFormDataConverter = checkNotNull(entityFormDataConverter);
        this.messageFormatter = checkNotNull(messageFormatter);
        this.frameChangeGeneratorFactory = checkNotNull(frameChangeGeneratorFactory);
        this.formFrameConverter = checkNotNull(formFrameConverter);
        this.emptyEntityFrameFactory = emptyEntityFrameFactory;
        this.renderingManager = renderingManager;
        this.dataFactory = dataFactory;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }

    @Override
    public OntologyChangeList<OWLEntity> generateChanges(ChangeGenerationContext context) {
        var allChanges = new ArrayList<OntologyChangeList<OWLEntity>>();
        for(int i = 0; i < pristineFormsData.size(); i++) {
            var pristineFormData = pristineFormsData.get(i);
            var editedFormData = editedFormsData.get(i);
            var pristineFormFrame = entityFormDataConverter.convert(pristineFormData);
            var editedFormFrame = entityFormDataConverter.convert(editedFormData);

            if(!pristineFormFrame.equals(editedFormFrame)) {
                var pristineFramesBySubject = getFormFrameClosureBySubject(pristineFormFrame);
                var editedFramesBySubject = getFormFrameClosureBySubject(editedFormFrame);

                var changes = generateChangesForFormFrames(pristineFramesBySubject, editedFramesBySubject, context);
                allChanges.addAll(changes);
            }
        }

        if(allChanges.isEmpty()) {
            return emptyChangeList();
        }
        else {
            return combineIndividualChangeLists(allChanges);
        }
    }

    private OntologyChangeList<OWLEntity> emptyChangeList() {
        return OntologyChangeList.<OWLEntity>builder().build(subject);
    }

    /**
     * Combines a list of change lists
     * @param changes the list of changes lists
     * @return the combined list with the subject equal to the subject of the first change in the list
     */
    @Nonnull
    public OntologyChangeList<OWLEntity> combineIndividualChangeLists(List<OntologyChangeList<OWLEntity>> changes) {
        var firstChangeList = changes.get(0);
        var combinedChanges = changes.stream()
                                     .map(OntologyChangeList::getChanges)
                                     .flatMap(List::stream)
                                     .collect(toImmutableList());

        return OntologyChangeList.<OWLEntity>builder()
                .addAll(combinedChanges)
                .build(firstChangeList.getResult());
    }

    private static ImmutableMap<OWLEntity, FormFrame> getFormFrameClosureBySubject(FormFrame formFrame) {
        // Important: ImmutableMap preserves iteration order
        var result = ImmutableMap.<OWLEntity, FormFrame>builder();
        List<FormFrame> framesToProcess = new ArrayList<>();
        framesToProcess.add(formFrame);
        while(!framesToProcess.isEmpty()) {
            var frame = framesToProcess.remove(0);
            frame.getSubject()
                 .accept(new FormSubject.FormDataSubjectVisitor() {
                     @Override
                     public void visit(@Nonnull FormEntitySubject formDataEntitySubject) {
                         result.put(formDataEntitySubject.getEntity(), frame);
                     }

                     @Override
                     public void visit(@Nonnull FormIriSubject formDataIriSubject) {

                     }
                 });
            framesToProcess.addAll(frame.getNestedFrames());
        }
        return result.build();
    }

    private List<OntologyChangeList<OWLEntity>> generateChangesForFormFrames(ImmutableMap<OWLEntity, FormFrame> pristineFramesBySubject,
                                                                                 ImmutableMap<OWLEntity, FormFrame> editedFramesBySubject,
                                                                                 ChangeGenerationContext context) {

        var resultBuilder = ImmutableList.<OntologyChangeList<OWLEntity>>builder();
        for(OWLEntity entity : pristineFramesBySubject.keySet()) {
            var pristineFrame = pristineFramesBySubject.get(entity);
            var editedFrame = editedFramesBySubject.get(entity);

            var pristineEntityFrame = formFrameConverter.toEntityFrame(pristineFrame)
                                                        .orElseThrow();

            if(editedFrame == null) {
                // Deleted
                var emptyEditedFrame = emptyEntityFrameFactory.getEmptyEntityFrame(entity);
                var changes = generateChangeListForFrames(pristineEntityFrame, emptyEditedFrame, context);
                resultBuilder.add(changes);
                var emptyFormFrame = FormFrame.get(FormSubject.get(entity));
                generateChangesForInstances(entity, pristineFrame, emptyFormFrame, resultBuilder);
            }
            else {
                // Edited, possibly
                var editedEntityFrame = formFrameConverter.toEntityFrame(editedFrame)
                                                          .orElseThrow();
                var changes = generateChangeListForFrames(pristineEntityFrame, editedEntityFrame, context);
                resultBuilder.add(changes);
                // Compute diff of class assertions
                generateChangesForInstances(entity, pristineFrame, editedFrame, resultBuilder);
            }
        }

        for(OWLEntity entity : editedFramesBySubject.keySet()) {
            var pristineFrame = pristineFramesBySubject.get(entity);
            if(pristineFrame == null) {
                // Added
                var emptyPristineFrame = emptyEntityFrameFactory.getEmptyEntityFrame(entity);
                var addedFormFrame = editedFramesBySubject.get(entity);
                var addedEntityFrame = formFrameConverter.toEntityFrame(addedFormFrame)
                                                         .orElseThrow();
                var changes = generateChangeListForFrames(emptyPristineFrame, addedEntityFrame, context);
                resultBuilder.add(changes);
                // Add all class assertions for instances
                generateChangesForInstances(entity,
                                            FormFrame.get(FormSubject.get(entity)),
                                            addedFormFrame,
                                            resultBuilder);
            }
        }
        return resultBuilder.build();
    }


    private OntologyChangeList<OWLEntity> generateChangeListForFrames(PlainEntityFrame pristineFrame,
                                                                          PlainEntityFrame editedFrame,
                                                                          ChangeGenerationContext context) {
        var frameUpdate = FrameUpdate.get(pristineFrame, editedFrame);
        var changeGeneratorFactory = frameChangeGeneratorFactory.create(frameUpdate);

        return changeGeneratorFactory.generateChanges(context);
    }

    private void generateChangesForInstances(OWLEntity subject,
                                             FormFrame pristineFrame,
                                             FormFrame editedFrame,
                                             ImmutableList.Builder<OntologyChangeList<OWLEntity>> changeListBuilder) {
        if(!(subject instanceof OWLClass)) {
            return;
        }
        var ontologyChangeList = OntologyChangeList.<OWLEntity>builder();
        var subjectCls = (OWLClass) subject;
        var pristineInstances = pristineFrame.getInstances();
        var editedInstances = editedFrame.getInstances();
        for(var pristineInstance : pristineInstances) {
            if(!editedInstances.contains(pristineInstance)) {
                // Deleted
                var axiom = dataFactory.getOWLClassAssertionAxiom(subjectCls, pristineInstance);
                // TODO: Project ontologies?
                ontologyChangeList.removeAxiom(defaultOntologyIdManager.getDefaultOntologyId(), axiom);
            }
        }
        for(var editedInstance : editedInstances) {
            if(!pristineInstances.contains(editedInstance)) {
                // Added
                var axiom = dataFactory.getOWLClassAssertionAxiom(subjectCls, editedInstance);
                ontologyChangeList.addAxiom(defaultOntologyIdManager.getDefaultOntologyId(), axiom);
            }
        }
        changeListBuilder.add(ontologyChangeList.build(subject));

    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<OWLEntity> result) {
        OWLEntity entity = result.getSubject();
        return messageFormatter.format("Edited {0}", entity);
    }

    @Override
    public OWLEntity getRenamedResult(OWLEntity result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }
}

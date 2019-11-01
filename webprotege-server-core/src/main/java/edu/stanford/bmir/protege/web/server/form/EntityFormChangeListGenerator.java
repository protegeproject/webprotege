package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.FrameChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.frame.FrameUpdate;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import java.util.HashSet;

import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class EntityFormChangeListGenerator implements ChangeListGenerator<OWLEntityData> {

    @Nonnull
    private final OWLEntity entity;

    @Nonnull
    private final ImmutableSet<PropertyValue> propertyValues;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ClassFrameTranslator classFrameTranslator;

    @Nonnull
    private final FrameChangeGeneratorFactory frameChangeGenerator;

    public EntityFormChangeListGenerator(@Nonnull OWLEntity entity,
                                         @Nonnull ImmutableSet<PropertyValue> propertyValues,
                                         @Nonnull RenderingManager renderingManager,
                                         @Nonnull ClassFrameTranslator classFrameTranslator,
                                         @Nonnull FrameChangeGeneratorFactory frameChangeGenerator) {
        this.entity = entity;
        this.propertyValues = propertyValues;
        this.renderingManager = renderingManager;
        this.classFrameTranslator = classFrameTranslator;
        this.frameChangeGenerator = frameChangeGenerator;
    }

    @Override
    public OntologyChangeList<OWLEntityData> generateChanges(ChangeGenerationContext context) {
        if(entity.isOWLClass()) {
            return generateChangesForClass(context);

        }
        return null;
    }

    private OntologyChangeList<OWLEntityData> generateChangesForClass(ChangeGenerationContext context) {
        var frameSubject = renderingManager.getClassData(entity.asOWLClass());

        // Current frame – may be a superset of the form frame
        // as forms may only edit a subset of properties
        var currentFrame = classFrameTranslator.getFrame(frameSubject);

        // Frame as edited by the form – may be a subset of the current frame
        var editedFrame = classFrameTranslator.getFrame(frameSubject);
        var touchedProperties = propertyValues.stream()
                .map(propertyValue -> propertyValue.getProperty().getEntity())
                .collect(toSet());

        var currentPropertyValues = editedFrame.getPropertyValues();
        // Untouched property values get to stay in the final set
        // as the form may not display these
        var untouchedPropertyValues = currentPropertyValues.stream()
                .filter(propertyValue -> !touchedProperties.contains(propertyValue.getProperty().getEntity()))
                .collect(toSet());

        var mergedPropertyValues = ImmutableSet.<PropertyValue>builder()
                .addAll(untouchedPropertyValues)
                .addAll(propertyValues)
                .build();
        var finalFrame = ClassFrame.get(frameSubject, currentFrame.getClassEntries(), mergedPropertyValues);
        var frameUpdate = FrameUpdate.get(currentFrame, finalFrame);
        return frameChangeGenerator.create(frameUpdate).generateChanges(context);
    }

    @Override
    public OWLEntityData getRenamedResult(OWLEntityData result, RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<OWLEntityData> result) {
        return "Edited frame";
    }
}

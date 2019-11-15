package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.FrameChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.frame.FrameUpdate;
import edu.stanford.bmir.protege.web.server.frame.NamedIndividualFrameTranslator;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.HasPropertyValues;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class EntityFormChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final AxiomTemplatesParser axiomTemplatesParser;

    @Nonnull
    private final EntityFormDataConverter entityFormDataConverter;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ClassFrameTranslator classFrameTranslator;

    @Nonnull
    private final NamedIndividualFrameTranslator individualFrameTranslator;

    @Nonnull
    private final FrameChangeGeneratorFactory frameChangeGenerator;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final FormData formData;

    @Inject
    public EntityFormChangeListGenerator(@Nonnull FormData formData,
                                         @Nonnull AxiomTemplatesParser axiomTemplatesParser,
                                         @Nonnull EntityFormDataConverter entityFormDataConverter,
                                         @Nonnull RenderingManager renderingManager,
                                         @Nonnull ClassFrameTranslator classFrameTranslator,
                                         @Nonnull NamedIndividualFrameTranslator individualFrameTranslator,
                                         @Nonnull FrameChangeGeneratorFactory frameChangeGenerator,
                                         @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        this.formData = checkNotNull(formData);
        this.axiomTemplatesParser = checkNotNull(axiomTemplatesParser);
        this.entityFormDataConverter = checkNotNull(entityFormDataConverter);
        this.renderingManager = checkNotNull(renderingManager);
        this.classFrameTranslator = checkNotNull(classFrameTranslator);
        this.individualFrameTranslator = checkNotNull(individualFrameTranslator);
        this.frameChangeGenerator = checkNotNull(frameChangeGenerator);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {

        var session = entityFormDataConverter.convert(formData);
        var propertyValuesBySubject = session.getEntityPropertyValues();
        var ontologyChanges = generateChangesForPropertyValues(context, propertyValuesBySubject);
        var axiomsTemplates = session.getAdditionalAxiomTemplates();
        var axioms = axiomTemplatesParser.parseAxiomTemplate(axiomsTemplates);
        var builder = OntologyChangeList.<Boolean>builder();
        axioms.forEach(ax -> builder.addAxiom(getTargetOntologyId(session), ax));
        return builder.addAll(ontologyChanges).build(Boolean.TRUE);
    }

    private OWLOntologyID getTargetOntologyId(EntityFormDataConverterSession session) {
        return session.getTargetOntologyIri()
                .map(iri -> new OWLOntologyID(iri))
                .orElse(defaultOntologyIdManager.getDefaultOntologyId());
    }

    public List<OntologyChange> generateChangesForPropertyValues(ChangeGenerationContext context,
                                                                 ImmutableMultimap<OWLEntity, PropertyValue> propertyValuesBySubject) {
        ImmutableMap<OWLEntity, Collection<PropertyValue>> subject2PropertyValues = propertyValuesBySubject.asMap();
        return subject2PropertyValues
                .entrySet()
                .stream()
                .map(entry -> {
                    var entity = entry.getKey();
                    var propertyValues = entry.getValue();
                    if(entity.isOWLClass()) {
                        return generateChangesForClass(entity.asOWLClass(),
                                                       propertyValues,
                                                       context);
                    }
                    else if(entity.isOWLNamedIndividual()) {
                        return generateChangesForIndividual(entity.asOWLNamedIndividual(),
                                                            propertyValues,
                                                            context);
                    }
                    else {
                        return Collections.<OntologyChange>emptyList();
                    }
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<OntologyChange> generateChangesForClass(OWLEntity entity,
                                                         Collection<PropertyValue> propertyValues,
                                                         ChangeGenerationContext context) {
        var frameSubject = renderingManager.getClassData(entity.asOWLClass());

        // Current frame – may be a superset of the form frame
        // as forms may only edit a subset of properties
        var currentFrame = classFrameTranslator.getFrame(frameSubject);

        var mergedPropertyValues = getMergedPropertyValues(propertyValues, currentFrame);
        var finalFrame = ClassFrame.get(frameSubject, currentFrame.getClassEntries(), mergedPropertyValues);
        var frameUpdate = FrameUpdate.get(currentFrame, finalFrame);
        return frameChangeGenerator.create(frameUpdate)
                                   .generateChanges(context)
                                   .getChanges();
    }

    private List<OntologyChange> generateChangesForIndividual(OWLNamedIndividual entity,
                                                              Collection<PropertyValue> propertyValues,
                                                              ChangeGenerationContext context) {
        var frameSubject = renderingManager.getIndividualData(entity);

        // Current frame – may be a superset of the form frame
        // as forms may only edit a subset of properties
        var currentFrame = individualFrameTranslator.getFrame(frameSubject);
        ImmutableSet<PropertyValue> mergedPropertyValues = getMergedPropertyValues(propertyValues, currentFrame);

        var finalFrame = NamedIndividualFrame.get(frameSubject,
                                                  currentFrame.getClasses(),
                                                  mergedPropertyValues,
                                                  currentFrame.getSameIndividuals());
        var frameUpdate = FrameUpdate.get(currentFrame, finalFrame);
        return frameChangeGenerator.create(frameUpdate)
                                   .generateChanges(context)
                                   .getChanges();
    }

    private ImmutableSet<PropertyValue> getMergedPropertyValues(Collection<PropertyValue> editedPropertyValues,
                                                                HasPropertyValues existingFrame) {
        // Frame as edited by the form – may be a subset of the current frame
        var touchedProperties = editedPropertyValues.stream()
                                                    .map(propertyValue -> propertyValue.getProperty()
                                                                                       .getEntity())
                                                    .collect(toSet());

        var currentPropertyValues = existingFrame.getPropertyValues();
        // Untouched property values get to stay in the final set
        // as the form may not display these
        var untouchedPropertyValues = currentPropertyValues.stream()
                                                           .filter(propertyValue -> !touchedProperties.contains(
                                                                   propertyValue.getProperty()
                                                                                .getEntity()))
                                                           .collect(toSet());

        return ImmutableSet.<PropertyValue>builder()
                .addAll(untouchedPropertyValues)
                .addAll(editedPropertyValues)
                .build();
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        return "Edited frame";
    }

    @Override
    public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
        return result;
    }
}

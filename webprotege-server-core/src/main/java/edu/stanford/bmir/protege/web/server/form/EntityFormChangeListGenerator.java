package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.FrameChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.frame.FrameUpdate;
import edu.stanford.bmir.protege.web.server.frame.NamedIndividualFrameTranslator;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class EntityFormChangeListGenerator implements ChangeListGenerator<OWLEntityData> {

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
    private final ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory;

    @Nonnull
    private final MessageFormatter messageFormatter;

    @Nonnull
    private final FormData formData;
    
    private OWLEntity subject;

    @Inject
    public EntityFormChangeListGenerator(@Nonnull FormData formData,
                                         @Nonnull AxiomTemplatesParser axiomTemplatesParser,
                                         @Nonnull EntityFormDataConverter entityFormDataConverter,
                                         @Nonnull RenderingManager renderingManager,
                                         @Nonnull ClassFrameTranslator classFrameTranslator,
                                         @Nonnull NamedIndividualFrameTranslator individualFrameTranslator,
                                         @Nonnull FrameChangeGeneratorFactory frameChangeGenerator,
                                         @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                         @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory,
                                         @Nonnull MessageFormatter messageFormatter) {
        this.formData = checkNotNull(formData);
        this.axiomTemplatesParser = checkNotNull(axiomTemplatesParser);
        this.entityFormDataConverter = checkNotNull(entityFormDataConverter);
        this.renderingManager = checkNotNull(renderingManager);
        this.classFrameTranslator = checkNotNull(classFrameTranslator);
        this.individualFrameTranslator = checkNotNull(individualFrameTranslator);
        this.frameChangeGenerator = checkNotNull(frameChangeGenerator);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
        this.reverseEngineeredChangeDescriptionGeneratorFactory = reverseEngineeredChangeDescriptionGeneratorFactory;
        this.messageFormatter = messageFormatter;
    }

    @Override
    public OntologyChangeList<OWLEntityData> generateChanges(ChangeGenerationContext context) {

        var session = entityFormDataConverter.convert(formData);
        this.subject = session.getSubject(formData).orElseThrow();


        var ontologyChanges = generateChangesForPropertyValues(context,
                                                               session);
        var axiomsTemplates = session.getAdditionalAxiomTemplates();
        var axioms = axiomTemplatesParser.parseAxiomTemplate(axiomsTemplates);
        var builder = OntologyChangeList.<OWLEntityData>builder();
        axioms.forEach(ax -> builder.addAxiom(getTargetOntologyId(session), ax));
        return builder.addAll(ontologyChanges)
                      .build(renderingManager.getRendering(subject));
    }

    private List<OntologyChange> generateChangesForPropertyValues(ChangeGenerationContext context,
                                                                  EntityFormDataConverterSession session) {

        return session.getFormData()
                      .stream()
                      .flatMap(formData -> getChangesForFormData(formData, session, context).stream())
                      .collect(toList());
    }

    private OWLOntologyID getTargetOntologyId(EntityFormDataConverterSession session) {
        return session.getTargetOntologyIri()
                      .map(OWLOntologyID::new)
                      .orElse(defaultOntologyIdManager.getDefaultOntologyId());
    }

    private List<OntologyChange> getChangesForFormData(FormData formData,
                                                       EntityFormDataConverterSession session,
                                                       ChangeGenerationContext context) {
        // The subject entity must be present for us to generate actual
        // ontology changes
        var subject = session.getSubject(formData);
        return subject.map(theSubject -> getChangesFormFormDataSubject(formData, theSubject, session, context))
                      .orElse(Collections.emptyList());
    }

    private List<OntologyChange> getChangesFormFormDataSubject(FormData formData,
                                                               OWLEntity theSubject,
                                                               EntityFormDataConverterSession session,
                                                               ChangeGenerationContext context) {
        var formProperties = formData.getFormDescriptor()
                                     .getOwlProperties();

        var propertyValues = session.getFormDataPropertyValues(formData);

        if(theSubject.isOWLClass()) {
            return generateChangesForClass(theSubject.asOWLClass(),
                                           formProperties,
                                           propertyValues,
                                           context);
        }
        else if(theSubject.isOWLNamedIndividual()) {
            return generateChangesForIndividual(theSubject.asOWLNamedIndividual(),
                                                formProperties,
                                                propertyValues,
                                                context);
        }
        else {
            return Collections.emptyList();
        }
    }

    private List<OntologyChange> generateChangesForClass(OWLEntity entity,
                                                         ImmutableSet<OWLProperty> formProperties,
                                                         Collection<PropertyValue> propertyValues,
                                                         ChangeGenerationContext context) {
        var frameSubject = renderingManager.getClassData(entity.asOWLClass());

        // Current frame – may be a superset of the form frame
        // as forms may only edit a subset of properties
        var currentFrame = classFrameTranslator.getFrame(frameSubject);

        var mergedPropertyValues = getMergedPropertyValues(formProperties, propertyValues, currentFrame);
        var finalFrame = ClassFrame.get(frameSubject, currentFrame.getClassEntries(), mergedPropertyValues);
        var frameUpdate = FrameUpdate.get(currentFrame, finalFrame);
        return frameChangeGenerator.create(frameUpdate)
                                   .generateChanges(context)
                                   .getChanges();
    }

    private List<OntologyChange> generateChangesForIndividual(OWLNamedIndividual entity,
                                                              ImmutableSet<OWLProperty> formProperties,
                                                              Collection<PropertyValue> propertyValues,
                                                              ChangeGenerationContext context) {
        var frameSubject = renderingManager.getIndividualData(entity);

        // Current frame – may be a superset of the form frame
        // as forms may only edit a subset of properties
        var currentFrame = individualFrameTranslator.getFrame(frameSubject);
        var mergedPropertyValues = getMergedPropertyValues(formProperties,
                                                           propertyValues,
                                                           currentFrame);

        var finalFrame = NamedIndividualFrame.get(frameSubject,
                                                  currentFrame.getClasses(),
                                                  mergedPropertyValues,
                                                  currentFrame.getSameIndividuals());
        var frameUpdate = FrameUpdate.get(currentFrame, finalFrame);
        return frameChangeGenerator.create(frameUpdate)
                                   .generateChanges(context)
                                   .getChanges();
    }

    private ImmutableSet<PropertyValue> getMergedPropertyValues(ImmutableSet<OWLProperty> formProperties,
                                                                Collection<PropertyValue> editedPropertyValues,
                                                                HasPropertyValues existingFrame) {
        var currentPropertyValues = existingFrame.getPropertyValues();
        // Non-form property values get to stay in the final set
        // as the form does not display these
        var nonFormPropertyValues = currentPropertyValues.stream()
                                                         .filter(propertyValue -> !formProperties.contains(
                                                                 propertyValue.getProperty()
                                                                              .getEntity()))
                                                         .collect(toSet());

        return ImmutableSet.<PropertyValue>builder()
                .addAll(nonFormPropertyValues)
                .addAll(editedPropertyValues)
                .build();
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<OWLEntityData> result) {
        var msg = messageFormatter.format("Edited the details of {0}", subject);
        return reverseEngineeredChangeDescriptionGeneratorFactory.get(msg).generateChangeDescription(result);
    }

    @Override
    public OWLEntityData getRenamedResult(OWLEntityData result, RenameMap renameMap) {
        return result;
    }
}

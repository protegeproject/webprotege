package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableMultimap;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.FrameChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.frame.NamedIndividualFrameTranslator;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.SetEntityFormDataAction;
import edu.stanford.bmir.protege.web.shared.form.SetEntityFormDataResult;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class SetEntityFormDataActionHandler extends AbstractProjectChangeHandler<Boolean, SetEntityFormDataAction, SetEntityFormDataResult> {

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final OWLEntityProvider entityProvider;

    @Nonnull
    private final ClassFrameTranslator classFrameTranslator;

    @Nonnull
    private final NamedIndividualFrameTranslator namedIndividualFrameTranslator;

    @Nonnull
    private final FrameChangeGeneratorFactory frameChangeGeneratorFactory;

    @Inject
    public SetEntityFormDataActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull RenderingManager renderingManager,
                                          @Nonnull OWLEntityProvider entityProvider,
                                          @Nonnull ClassFrameTranslator classFrameTranslator,
                                          @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                          EventManager<ProjectEvent<?>> eventManager,
                                          HasApplyChanges applyChanges,
                                          @Nonnull NamedIndividualFrameTranslator namedIndividualFrameTranslator) {
        super(accessManager, eventManager, applyChanges);
        this.renderingManager = renderingManager;
        this.entityProvider = entityProvider;
        this.classFrameTranslator = classFrameTranslator;
        this.frameChangeGeneratorFactory = frameChangeGeneratorFactory;
        this.namedIndividualFrameTranslator = namedIndividualFrameTranslator;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(SetEntityFormDataAction action,
                                                                        ExecutionContext executionContext) {

        var formData = action.getFormData();
        if(formData.getSubject().isEmpty()) {
            return new FixedChangeListGenerator<>(Collections.emptyList(), false, "");
        }

        var elementId2PropertyMap = action.getFormData().getFormDescriptor().getOwlPropertyMap();

        var resultBuilder = ImmutableMultimap.<OWLEntity, PropertyValue>builder();

        buildIt(formData, resultBuilder, elementId2PropertyMap);
        return new EntityFormChangeListGenerator(resultBuilder.build(),
                                                 renderingManager,
                                                 classFrameTranslator,
                                                 namedIndividualFrameTranslator,
                                                 frameChangeGeneratorFactory);
    }

    private void buildIt(FormData formData, ImmutableMultimap.Builder<OWLEntity, PropertyValue> resultBuilder,
                         Map<FormElementId, Optional<OWLProperty>> elementId2PropertyMap) {
        formData.getData()
                .forEach((formElementId, formDataValue) -> toPropertyValues(formData.getSubject()
                                                                                    .get(),
                                                                            resultBuilder,
                                                                            formElementId,
                                                                            elementId2PropertyMap,
                                                                            formDataValue));
    }

    @Override
    protected SetEntityFormDataResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult,
                                                         SetEntityFormDataAction action,
                                                         ExecutionContext executionContext,
                                                         EventList<ProjectEvent<?>> eventList) {
        return new SetEntityFormDataResult();
    }

    @Nonnull
    @Override
    public Class<SetEntityFormDataAction> getActionClass() {
        return SetEntityFormDataAction.class;
    }

    private void toPropertyValues(OWLEntity subject, ImmutableMultimap.Builder<OWLEntity, PropertyValue> resultBuilder,
                                                 FormElementId elementId,
                                                 Map<FormElementId, Optional<OWLProperty>> elementId2PropertyMap,
                                                 FormDataValue dataValue) {
        var property = elementId2PropertyMap.get(elementId);
        property.ifPresent(prop -> toPropertyValues(subject, resultBuilder, prop, dataValue));
    }

    private void toPropertyValues(OWLEntity subject,
                                  ImmutableMultimap.Builder<OWLEntity, PropertyValue> resultBuilder,
                                  OWLProperty property, FormDataValue dataValue) {
        toPropertyValues(subject, resultBuilder, property, dataValue.asList());
    }

    private void toPropertyValues(OWLEntity subject,
                                  ImmutableMultimap.Builder<OWLEntity, PropertyValue> resultBuilder,
                                  OWLProperty property, List<FormDataValue> formDataValues) {
        List<PropertyValue> propertyValues = new ArrayList<>();
        if(property.isOWLAnnotationProperty()) {
            formDataValues.forEach(formDataValue -> {
                formDataValue.asLiteral().ifPresent(literal -> {
                    propertyValues.add(PropertyAnnotationValue.get(
                            renderingManager.getAnnotationPropertyData(property.asOWLAnnotationProperty()),
                            OWLLiteralData.get(literal),
                            State.ASSERTED
                    ));
                });
                formDataValue.asIRI().ifPresent(iri -> {
                    propertyValues.add(PropertyAnnotationValue.get(
                            renderingManager.getAnnotationPropertyData(property.asOWLAnnotationProperty()),
                            renderingManager.getRendering(iri),
                            State.ASSERTED
                    ));
                });
                formDataValue.asOWLEntity().map(OWLEntity::getIRI).ifPresent(iri -> {
                    propertyValues.add(PropertyAnnotationValue.get(
                            renderingManager.getAnnotationPropertyData(property.asOWLAnnotationProperty()),
                            renderingManager.getRendering(iri),
                            State.ASSERTED
                    ));
                });
            });
        }
        else if(property.isOWLObjectProperty()) {
            formDataValues.forEach(formDataValue -> {
                formDataValue.asOWLEntity().ifPresent(entity -> {
                    if(entity.isOWLClass()) {
                        propertyValues.add(PropertyClassValue.get(
                                renderingManager.getObjectPropertyData(property.asOWLObjectProperty()),
                                renderingManager.getClassData(entity.asOWLClass()),
                                State.ASSERTED  // Sort this!
                        ));
                    }
                    else if(entity.isOWLNamedIndividual()) {
                        propertyValues.add(PropertyIndividualValue.get(
                                renderingManager.getObjectPropertyData(property.asOWLObjectProperty()),
                                renderingManager.getIndividualData(entity.asOWLNamedIndividual()),
                                State.ASSERTED  // Sort this!
                        ));
                    }
                });
                formDataValue.asFormData().ifPresent(formData -> {
                    formData.getSubject().ifPresent(nestedSubject -> {
                        if(nestedSubject.isOWLClass()) {
                            propertyValues.add(PropertyClassValue.get(
                                    renderingManager.getObjectPropertyData(property.asOWLObjectProperty()),
                                    renderingManager.getClassData(nestedSubject.asOWLClass()),
                                    State.ASSERTED  // Sort this!
                            ));
                        }
                        else if(nestedSubject.isOWLNamedIndividual()) {
                            propertyValues.add(PropertyIndividualValue.get(
                                    renderingManager.getObjectPropertyData(property.asOWLObjectProperty()),
                                    renderingManager.getIndividualData(nestedSubject.asOWLNamedIndividual()),
                                    State.ASSERTED  // Sort this!
                            ));
                        }
                        buildIt(formData, resultBuilder, formData.getFormDescriptor().getOwlPropertyMap());
                    });
                });
            });
        }
        else if(property.isOWLDataProperty()) {
            formDataValues.forEach(formDataValue -> {
                formDataValue.asOWLEntity().ifPresent(entity -> {
                    if(entity.isOWLDatatype()) {
                        propertyValues.add(PropertyDatatypeValue.get(
                                renderingManager.getDataPropertyData(property.asOWLDataProperty()),
                                renderingManager.getDatatypeData(entity.asOWLDatatype()),
                                State.ASSERTED  // Sort this!
                        ));
                    }
                });
                formDataValue.asLiteral().ifPresent(literal -> {
                    propertyValues.add(PropertyLiteralValue.get(
                            renderingManager.getDataPropertyData(property.asOWLDataProperty()),
                            OWLLiteralData.get(literal),
                            State.ASSERTED
                    ));
                });
            });
        }
        resultBuilder.putAll(subject, propertyValues);
    }
}

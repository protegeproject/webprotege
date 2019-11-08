package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.FrameChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.form.SetEntityFormDataAction;
import edu.stanford.bmir.protege.web.shared.form.SetEntityFormDataResult;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
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
public class SetEntityFormDataActionHandler extends AbstractProjectChangeHandler<OWLEntityData, SetEntityFormDataAction, SetEntityFormDataResult> {

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final OWLEntityProvider entityProvider;

    @Nonnull
    private final ClassFrameTranslator classFrameTranslator;

    @Nonnull
    private final FrameChangeGeneratorFactory frameChangeGeneratorFactory;

    @Inject
    public SetEntityFormDataActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull RenderingManager renderingManager,
                                          @Nonnull OWLEntityProvider entityProvider,
                                          @Nonnull ClassFrameTranslator classFrameTranslator,
                                          @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                          EventManager<ProjectEvent<?>> eventManager,
                                          HasApplyChanges applyChanges) {
        super(accessManager, eventManager, applyChanges);
        this.renderingManager = renderingManager;
        this.entityProvider = entityProvider;
        this.classFrameTranslator = classFrameTranslator;
        this.frameChangeGeneratorFactory = frameChangeGeneratorFactory;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Override
    protected ChangeListGenerator<OWLEntityData> getChangeListGenerator(SetEntityFormDataAction action,
                                                                        ExecutionContext executionContext) {
        var formData = action.getFormData();
        var elementId2PropertyMap = action.getFormDescriptor().getOwlPropertyMap();

        var propertyValues = formData.getData()
                                     .entrySet()
                                     .stream()
                                     .map(entry -> {
                                         var formElementId = entry.getKey();
                                         var formDataValue = entry.getValue();
                                         return toPropertyValues(formElementId, elementId2PropertyMap, formDataValue);
                                     })
                                     .flatMap(Collection::stream)
                                     .collect(toImmutableSet());
        var entity = action.getEntity();
        return new EntityFormChangeListGenerator(entity,
                                                 propertyValues,
                                                 renderingManager,
                                                 classFrameTranslator,
                                                 frameChangeGeneratorFactory);
    }

    @Override
    protected SetEntityFormDataResult createActionResult(ChangeApplicationResult<OWLEntityData> changeApplicationResult,
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

    private List<PropertyValue> toPropertyValues(FormElementId elementId,
                                                 Map<FormElementId, Optional<OWLProperty>> elementId2PropertyMap,
                                                 FormDataValue dataValue) {
        var property = elementId2PropertyMap.get(elementId);
        return property.map(prop -> toPropertyValues(prop, dataValue)).orElse(Collections.emptyList());
    }

    private List<PropertyValue> toPropertyValues(OWLProperty property, FormDataValue dataValue) {
        return toPropertyValues(property, dataValue.asList());
    }

    private List<PropertyValue> toPropertyValues(OWLProperty property, List<FormDataValue> formDataValues) {
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
        return propertyValues;
    }
}

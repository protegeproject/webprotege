package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-13
 */
public class EntityFormDataConverter {

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final Provider<EntityFormDataConverterSession> sessionFactory;


    @Inject
    public EntityFormDataConverter(@Nonnull RenderingManager renderingManager,
                                   @Nonnull Provider<EntityFormDataConverterSession> sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.renderingManager = renderingManager;
    }

    public EntityFormDataConverterSession convert(@Nonnull FormData formData) {
        var session = sessionFactory.get();
        convertFormDataToPropertyValues(formData, session);
        return session;
    }

    private void convertAnnotationPropertyBasedFormValueToPropertyValues(OWLEntity subject,
                                                                         OWLAnnotationProperty property,
                                                                         List<FormDataValue> formDataValues,
                                                                         EntityFormDataConverterSession session) {
        formDataValues.forEach(formDataValue -> {
            formDataValue.asLiteral()
                         .ifPresent(literal -> {
                             var pv = PropertyAnnotationValue.get(
                                     renderingManager.getAnnotationPropertyData(property),
                                     OWLLiteralData.get(literal),
                                     State.ASSERTED
                             );
                             session.putPropertyValue(subject, pv);
                         });
            formDataValue.asIRI()
                         .ifPresent(iri -> {
                             var pv = PropertyAnnotationValue.get(
                                     renderingManager.getAnnotationPropertyData(property),
                                     renderingManager.getRendering(iri),
                                     State.ASSERTED
                             );
                             session.putPropertyValue(subject, pv);
                         });
            formDataValue.asOWLEntity()
                         .map(OWLEntity::getIRI)
                         .ifPresent(iri -> {
                             var pv = PropertyAnnotationValue.get(
                                     renderingManager.getAnnotationPropertyData(property),
                                     renderingManager.getRendering(iri),
                                     State.ASSERTED
                             );
                             session.putPropertyValue(subject, pv);
                         });
        });
    }

    private void convertDataPropertyBasedFormValueToPropertyValues(OWLEntity subject,
                                                                   OWLDataProperty property,
                                                                   List<FormDataValue> formDataValues,
                                                                   EntityFormDataConverterSession session) {
        formDataValues.forEach(formDataValue -> {
            formDataValue.asOWLEntity()
                         .ifPresent(entity -> {
                             if(entity.isOWLDatatype()) {
                                 var pv = PropertyDatatypeValue.get(
                                         renderingManager.getDataPropertyData(property),
                                         renderingManager.getDatatypeData(entity.asOWLDatatype()),
                                         State.ASSERTED  // Sort this!
                                 );
                                 session.putPropertyValue(subject, pv);
                             }
                         });
            formDataValue.asLiteral()
                         .ifPresent(literal -> {
                             var pv = PropertyLiteralValue.get(
                                     renderingManager.getDataPropertyData(property),
                                     OWLLiteralData.get(literal),
                                     State.ASSERTED
                             );
                             session.putPropertyValue(subject, pv);
                         });
        });
    }

    private void convertFormDataToPropertyValues(FormData formData,
                                                 EntityFormDataConverterSession session) {
        formData.getData()
                .forEach((formElementId, formDataValue) -> {
                    var subject = session.getSubject(formData);
                    subject.ifPresent(theSubject -> {
                        convertFormFieldToPropertyValue(theSubject,
                                                        formData,
                                                        formElementId,
                                                        formDataValue,
                                                        session
                        );
                    });
                });
    }

    private void convertFormFieldToPropertyValue(OWLEntity subject,
                                                 FormData formData,
                                                 FormElementId elementId,
                                                 FormDataValue dataValue,
                                                 EntityFormDataConverterSession session) {
        var property = formData.getOwlProperty(elementId);
        property.ifPresent(theProperty -> convertFormValueToPropertyValue(subject, theProperty, dataValue, session));
    }

    private void convertFormValueToPropertyValue(OWLEntity subject,
                                                 OWLProperty property,
                                                 FormDataValue dataValue,
                                                 EntityFormDataConverterSession session) {
        convertFormValueToPropertyValues(subject, property, dataValue.asList(), session);
    }

    private void convertFormValueToPropertyValues(OWLEntity subject,
                                                  OWLProperty property,
                                                  List<FormDataValue> formDataValues,
                                                  EntityFormDataConverterSession session) {
        if(property.isOWLAnnotationProperty()) {
            convertAnnotationPropertyBasedFormValueToPropertyValues(subject,
                                                                    property.asOWLAnnotationProperty(),
                                                                    formDataValues,
                                                                    session);
        }
        else if(property.isOWLObjectProperty()) {
            convertObjectPropertyBasedFormDataToPropertyValues(subject,
                                                               property.asOWLObjectProperty(),
                                                               formDataValues,
                                                               session);
        }
        else if(property.isOWLDataProperty()) {
            convertDataPropertyBasedFormValueToPropertyValues(subject,
                                                              property.asOWLDataProperty(),
                                                              formDataValues,
                                                              session);
        }
    }

    private void convertObjectPropertyBasedFormDataToPropertyValues(OWLEntity subject,
                                                                    OWLObjectProperty property,
                                                                    List<FormDataValue> formDataValues,
                                                                    EntityFormDataConverterSession session) {
        formDataValues.forEach(formDataValue -> {
            formDataValue.asOWLEntity()
                         .ifPresent(entity -> convertObjectPropertyEntityValueToPropertyValue(subject, property, entity, session));
            formDataValue.asFormData()
                         .ifPresent(subFormData -> {
                             var subFormSubject = session.getSubject(subFormData);
                             subFormSubject.ifPresent(nestedSubject -> {
                                 convertObjectPropertyEntityValueToPropertyValue(subject,
                                                                                 property,
                                                                                 nestedSubject, session
                                 );
                                 convertFormDataToPropertyValues(subFormData,
                                                                 session);
                             });
                         });
        });
    }

    private void convertObjectPropertyEntityValueToPropertyValue(OWLEntity subject,
                                                                 OWLObjectProperty property,
                                                                 OWLEntity value,
                                                                 EntityFormDataConverterSession session) {
        if(value.isOWLClass()) {
            var pv = PropertyClassValue.get(
                    renderingManager.getObjectPropertyData(property),
                    renderingManager.getClassData(value.asOWLClass()),
                    State.ASSERTED  // Sort this!
            );
            session.putPropertyValue(subject, pv);
        }
        else if(value.isOWLNamedIndividual()) {
            var pv = PropertyIndividualValue.get(
                    renderingManager.getObjectPropertyData(property),
                    renderingManager.getIndividualData(value.asOWLNamedIndividual()),
                    State.ASSERTED  // Sort this!
            );
            session.putPropertyValue(subject, pv);
        }
    }
}

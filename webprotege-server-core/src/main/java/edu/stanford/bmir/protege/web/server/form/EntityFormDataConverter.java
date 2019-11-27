package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
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

    /**
     * Converts the specified {@link FormData} into various
     * @param formData
     * @return
     */
    public EntityFormDataConverterSession convert(@Nonnull FormData formData) {
        var session = sessionFactory.get();
        session.addFormData(formData);
        addFormPropertyValuesToSession(formData, session);
        return session;
    }

    private void addFormPropertyValuesToSession(FormData formData,
                                                EntityFormDataConverterSession session) {
        formData.getData()
                .forEach((formElementId, formDataValue) -> {
                        addFormElementPropertyValuesToSession(formData, formElementId, formDataValue, session);
                });
    }

    private void addFormElementPropertyValuesToSession(FormData formData,
                                                       FormElementId elementId,
                                                       FormDataValue dataValue,
                                                       EntityFormDataConverterSession session) {
        var property = formData.getOwlProperty(elementId);
        property.ifPresent(theProperty -> addPropertyValuesToSession(formData,
                                                                     theProperty,
                                                                     dataValue.asList(),
                                                                     session));
    }



    private void addPropertyValuesToSession(FormData formData,
                                            OWLProperty property,
                                            List<FormDataValue> formDataValues,
                                            EntityFormDataConverterSession session) {
        if(property.isOWLAnnotationProperty()) {
            addAnnotationPropertyPropertyValuesToSession(formData,
                                                         property.asOWLAnnotationProperty(),
                                                         formDataValues,
                                                         session);
        }
        else if(property.isOWLObjectProperty()) {
            convertObjectPropertyBasedFormDataToPropertyValues(formData,
                                                               property.asOWLObjectProperty(),
                                                               formDataValues,
                                                               session);
        }
        else if(property.isOWLDataProperty()) {
            addDataPropertyPropertyValuesToSession(formData,
                                                   property.asOWLDataProperty(),
                                                   formDataValues,
                                                   session);
        }
    }

    private void convertObjectPropertyBasedFormDataToPropertyValues(FormData formData,
                                                                    OWLObjectProperty property,
                                                                    List<FormDataValue> formDataValues,
                                                                    EntityFormDataConverterSession session) {
        formDataValues.forEach(formDataValue -> {

            formDataValue.asOWLEntity()
                         .ifPresent(entity -> addObjectPropertyPropertyValuesToSession(formData, property, entity, session));

            formDataValue.asFormData()
                         .ifPresent(subFormData -> {
                             session.addFormData(subFormData);
                             var subFormSubject = session.getSubject(subFormData);
                             subFormSubject.ifPresent(nestedSubject -> {
                                 addObjectPropertyPropertyValuesToSession(formData,
                                                                          property,
                                                                          nestedSubject, session
                                 );
                                 addFormPropertyValuesToSession(subFormData,
                                                                session);
                             });
                         });
        });
    }

    private void addObjectPropertyPropertyValuesToSession(FormData formData,
                                                          OWLObjectProperty property,
                                                          OWLEntity value,
                                                          EntityFormDataConverterSession session) {
        if(value.isOWLClass()) {
            var pv = PropertyClassValue.get(
                    renderingManager.getObjectPropertyData(property),
                    renderingManager.getClassData(value.asOWLClass()),
                    State.ASSERTED  // Sort this!
            );
            session.putPropertyValue(formData, pv);
        }
        else if(value.isOWLNamedIndividual()) {
            var pv = PropertyIndividualValue.get(
                    renderingManager.getObjectPropertyData(property),
                    renderingManager.getIndividualData(value.asOWLNamedIndividual()),
                    State.ASSERTED  // Sort this!
            );
            session.putPropertyValue(formData, pv);
        }
    }

    private void addAnnotationPropertyPropertyValuesToSession(FormData formData,
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
                             session.putPropertyValue(formData, pv);
                         });
            formDataValue.asIRI()
                         .ifPresent(iri -> {
                             var pv = PropertyAnnotationValue.get(
                                     renderingManager.getAnnotationPropertyData(property),
                                     renderingManager.getRendering(iri),
                                     State.ASSERTED
                             );
                             session.putPropertyValue(formData, pv);
                         });
            formDataValue.asOWLEntity()
                         .map(OWLEntity::getIRI)
                         .ifPresent(iri -> {
                             var pv = PropertyAnnotationValue.get(
                                     renderingManager.getAnnotationPropertyData(property),
                                     renderingManager.getRendering(iri),
                                     State.ASSERTED
                             );
                             session.putPropertyValue(formData, pv);
                         });
        });
    }

    private void addDataPropertyPropertyValuesToSession(FormData formData,
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
                                 session.putPropertyValue(formData, pv);
                             }
                         });
            formDataValue.asLiteral()
                         .ifPresent(literal -> {
                             var pv = PropertyLiteralValue.get(
                                     renderingManager.getDataPropertyData(property),
                                     OWLLiteralData.get(literal),
                                     State.ASSERTED
                             );
                             session.putPropertyValue(formData, pv);
                         });
        });
    }
}

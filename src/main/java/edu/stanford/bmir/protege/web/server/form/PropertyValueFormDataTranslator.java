package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jul 2017
 */
public class PropertyValueFormDataTranslator {

    private final RenderingManager renderingManager;

    private final OWLPrimitiveDataVisitor<FormDataValue, RuntimeException> primitiveDataMapper = new OWLPrimitiveDataVisitor<FormDataValue, RuntimeException>() {
        @Override
        public FormDataValue visit(OWLClassData data) throws RuntimeException {
            return FormDataPrimitive.get(data.getEntity());
        }

        @Override
        public FormDataValue visit(OWLObjectPropertyData data) throws RuntimeException {
            return FormDataPrimitive.get(data.getEntity());
        }

        @Override
        public FormDataValue visit(OWLDataPropertyData data) throws RuntimeException {
            return FormDataPrimitive.get(data.getEntity());
        }

        @Override
        public FormDataValue visit(OWLAnnotationPropertyData data) throws RuntimeException {
            return FormDataPrimitive.get(data.getEntity());
        }

        @Override
        public FormDataValue visit(OWLNamedIndividualData data) throws RuntimeException {
            return FormDataPrimitive.get(data.getEntity());
        }

        @Override
        public FormDataValue visit(OWLDatatypeData data) throws RuntimeException {
            return FormDataPrimitive.get(data.getEntity());
        }

        @Override
        public FormDataValue visit(OWLLiteralData data) throws RuntimeException {
            return FormDataPrimitive.get(data.getLiteral());
        }

        @Override
        public FormDataValue visit(IRIData data) throws RuntimeException {
            return FormDataPrimitive.get(data.getObject());
        }
    };

    @Inject
    public PropertyValueFormDataTranslator(RenderingManager renderingManager) {
        this.renderingManager = renderingManager;
    }

    /**
     * Translates the specified property values into form data using the specified binding.
     *
     * @param propertyValues The property values to be translated.
     * @return The form data corresponding to the translated frame.
     */
    public FormData toFormData(@Nonnull Set<PropertyValue> propertyValues,
                               @Nonnull Map<OWLProperty, FormElementId> binding) {
        ListMultimap<FormElementId, FormDataValue> dataMultiMap = ArrayListMultimap.create();
        propertyValues.forEach(propertyValue -> {
            OWLProperty entity = propertyValue.getProperty().getEntity();
            FormElementId formElementId = binding.get(entity);
            if (formElementId != null) {
                OWLPrimitiveData owlVal = propertyValue.getValue();
                FormDataValue val = toFormDataValue(owlVal);
                dataMultiMap.put(formElementId, val);
            }
        });
        Map<FormElementId, FormDataValue> dataMap = new HashMap<>();
        for (FormElementId formElementId : dataMultiMap.keySet()) {
            List<FormDataValue> dataValues = dataMultiMap.get(formElementId);
            if (dataValues.size() == 1) {
                dataMap.put(formElementId, dataValues.get(0));
            }
            else {
                dataMap.put(formElementId, new FormDataList(dataValues));
            }
        }
        return new FormData(dataMap);
    }

    private FormDataValue toFormDataValue(OWLPrimitiveData owlVal) {
        return owlVal.accept(primitiveDataMapper);
    }

    public Set<PropertyValue> toPropertyValues(@Nonnull FormData formData,
                                               @Nonnull Map<FormElementId, OWLProperty> binding) {
        Map<FormElementId, FormDataValue> data = formData.getData();
        Set<PropertyValue> propertyValues = new HashSet<>();
        data.entrySet().forEach(e -> {
            FormElementId formElementId = e.getKey();
            FormDataValue dataValue = e.getValue();
            Set<OWLObject> owlObjects = toOWLObject(dataValue);
            OWLEntity property = binding.get(formElementId);
            if (property instanceof OWLObjectProperty) {
                for (OWLObject val : owlObjects) {
                    OWLObjectPropertyData objectProp = renderingManager.getRendering((OWLObjectProperty) property);
                    if (val instanceof OWLClass) {
                        propertyValues.add(new PropertyClassValue(objectProp,
                                                                  renderingManager.getRendering((OWLClass) val),
                                                                  State.ASSERTED));
                    }
                    else if (val instanceof OWLNamedIndividual) {
                        propertyValues.add(new PropertyIndividualValue(objectProp,
                                                                       renderingManager.getRendering((OWLNamedIndividual) val),
                                                                       State.ASSERTED));
                    }
                }
            }
            else if (property instanceof OWLDataProperty) {
                for (OWLObject val : owlObjects) {
                    OWLDataPropertyData dataProp = renderingManager.getRendering((OWLDataProperty) property);
                    if (val instanceof OWLClass) {
                        propertyValues.add(new PropertyDatatypeValue(dataProp,
                                                                     renderingManager.getRendering((OWLDatatype) val),
                                                                     State.ASSERTED));
                    }
                    else if (val instanceof OWLLiteral) {
                        propertyValues.add(new PropertyLiteralValue(dataProp,
                                                                    new OWLLiteralData((OWLLiteral) val),
                                                                    State.ASSERTED));
                    }
                }
            }
            else if (property instanceof OWLAnnotationProperty) {
                for (OWLObject val : owlObjects) {
                    OWLAnnotationPropertyData annotationProp = renderingManager.getRendering((OWLAnnotationProperty) property);
                    if (val instanceof OWLLiteral) {
                        propertyValues.add(new PropertyAnnotationValue(annotationProp,
                                                                       new OWLLiteralData((OWLLiteral) val),
                                                                       State.ASSERTED));
                    }
                    else if (val instanceof OWLEntity) {
                        propertyValues.add(new PropertyAnnotationValue(annotationProp,
                                                                       renderingManager.getRendering((OWLEntity) val),
                                                                       State.ASSERTED));

                    }
                    else if (val instanceof IRI) {
                        propertyValues.add(new PropertyAnnotationValue(annotationProp,
                                                                       new IRIData((IRI) val),
                                                                       State.ASSERTED));
                    }
                }
            }
            else {
                throw new RuntimeException("Unknown property type: " + property);
            }
        });
        return propertyValues;
    }

    private Set<OWLObject> toOWLObject(FormDataValue dataValue) {
        if (dataValue instanceof FormDataPrimitive) {
            FormDataPrimitive formDataPrimitive = (FormDataPrimitive) dataValue;
            OWLObject owlObject = formDataPrimitive.toOWLObject();
            return Collections.singleton(owlObject);
        }
        else if (dataValue instanceof FormDataList) {
            FormDataList formDataList = (FormDataList) dataValue;
            return formDataList.getList().stream().flatMap(e -> toOWLObject(e).stream()).collect(toSet());
        }
        else {
            throw new RuntimeException("Cannot convert to OWL Object");
        }
    }
}

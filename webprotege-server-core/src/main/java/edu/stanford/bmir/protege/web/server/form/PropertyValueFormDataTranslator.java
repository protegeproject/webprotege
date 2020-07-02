package edu.stanford.bmir.protege.web.server.form;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jul 2017
 */
public class PropertyValueFormDataTranslator {

//    private final RenderingManager renderingManager;
//
//    private final OWLPrimitiveDataVisitor<FormDataValue, RuntimeException> primitiveDataMapper = new OWLPrimitiveDataVisitor<FormDataValue, RuntimeException>() {
//        @Override
//        public FormDataValue accept(OWLClassData data) throws RuntimeException {
//            return FormDataPrimitive.get(data.getEntity());
//        }
//
//        @Override
//        public FormDataValue accept(OWLObjectPropertyData data) throws RuntimeException {
//            return FormDataPrimitive.get(data.getEntity());
//        }
//
//        @Override
//        public FormDataValue accept(OWLDataPropertyData data) throws RuntimeException {
//            return FormDataPrimitive.get(data.getEntity());
//        }
//
//        @Override
//        public FormDataValue accept(OWLAnnotationPropertyData data) throws RuntimeException {
//            return FormDataPrimitive.get(data.getEntity());
//        }
//
//        @Override
//        public FormDataValue accept(OWLNamedIndividualData data) throws RuntimeException {
//            return FormDataPrimitive.get(data.getEntity());
//        }
//
//        @Override
//        public FormDataValue accept(OWLDatatypeData data) throws RuntimeException {
//            return FormDataPrimitive.get(data.getEntity());
//        }
//
//        @Override
//        public FormDataValue accept(OWLLiteralData data) throws RuntimeException {
//            return FormDataPrimitive.get(data.getLiteral());
//        }
//
//        @Override
//        public FormDataValue accept(IRIData data) throws RuntimeException {
//            return FormDataPrimitive.get(data.getObject());
//        }
//    };
//
//    @Inject
//    public PropertyValueFormDataTranslator(RenderingManager renderingManager) {
//        this.renderingManager = renderingManager;
//    }
//
//    /**
//     * Translates the specified property values into form data using the specified binding.
//     *
//     * @param propertyValues The property values to be translated.
//     * @return The form data corresponding to the translated frame.
//     */
//    public FormData toFormData(@Nonnull OWLEntity subject,
//                               @Nonnull Set<PropertyValue> propertyValues,
//                               @Nonnull Map<OWLProperty, FormFieldId> binding) {
//        ListMultimap<FormFieldId, FormDataValue> dataMultiMap = ArrayListMultimap.create();
//        propertyValues.forEach(propertyValue -> {
//            OWLProperty entity = propertyValue.getProperty().getEntity();
//            FormFieldId formElementId = binding.get(entity);
//            if (formElementId != null) {
//                OWLPrimitiveData owlVal = propertyValue.getValue();
//                FormDataValue val = toFormDataValue(owlVal);
//                dataMultiMap.put(formElementId, val);
//            }
//        });
//        Map<FormFieldId, FormDataValue> dataMap = new HashMap<>();
//        for (FormFieldId formElementId : dataMultiMap.keySet()) {
//            List<FormDataValue> dataValues = dataMultiMap.get(formElementId);
//            if (dataValues.size() == 1) {
//                dataMap.put(formElementId, dataValues.get(0));
//            }
//            else {
//                dataMap.put(formElementId, new FormDataList(dataValues));
//            }
//        }
//        return new FormData(subject, dataMap, formDescriptor);
//    }
//
//    private FormDataValue toFormDataValue(OWLPrimitiveData owlVal) {
//        return owlVal.accept(primitiveDataMapper);
//    }
//
//    public Set<PropertyValue> toPropertyValues(@Nonnull FormData formData,
//                                               @Nonnull Map<FormFieldId, OWLProperty> binding) {
//        Map<FormFieldId, FormDataValue> data = formData.getData();
//        Set<PropertyValue> propertyValues = new HashSet<>();
//        data.forEach((formElementId, dataValue) -> {
//            Set<OWLObject> owlObjects = toOWLObject(dataValue);
//            OWLEntity property = binding.get(formElementId);
//            if (property instanceof OWLObjectProperty) {
//                for (OWLObject val : owlObjects) {
//                    OWLObjectPropertyData objectProp = renderingManager.getObjectPropertyData((OWLObjectProperty) property);
//                    if (val instanceof OWLClass) {
//                        propertyValues.add(PropertyClassValue.get(objectProp,
//                                                                  renderingManager.getClassData((OWLClass) val),
//                                                                  State.ASSERTED));
//                    }
//                    else if (val instanceof OWLNamedIndividual) {
//                        propertyValues.add(PropertyIndividualValue.get(objectProp,
//                                                                       renderingManager.getIndividualData((OWLNamedIndividual) val),
//                                                                       State.ASSERTED));
//                    }
//                }
//            }
//            else if (property instanceof OWLDataProperty) {
//                for (OWLObject val : owlObjects) {
//                    OWLDataPropertyData dataProp = renderingManager.getDataPropertyData((OWLDataProperty) property);
//                    if (val instanceof OWLClass) {
//                        propertyValues.add(PropertyDatatypeValue.get(dataProp,
//                                                                     renderingManager.getDatatypeData((OWLDatatype) val),
//                                                                     State.ASSERTED));
//                    }
//                    else if (val instanceof OWLLiteral) {
//                        propertyValues.add(PropertyLiteralValue.get(dataProp,
//                                                                    OWLLiteralData.get((OWLLiteral) val),
//                                                                    State.ASSERTED));
//                    }
//                }
//            }
//            else if (property instanceof OWLAnnotationProperty) {
//                for (OWLObject val : owlObjects) {
//                    OWLAnnotationPropertyData annotationProp = renderingManager.getAnnotationPropertyData((OWLAnnotationProperty) property);
//                    if (val instanceof OWLLiteral) {
//                        propertyValues.add(PropertyAnnotationValue.get(annotationProp,
//                                                                       OWLLiteralData.get((OWLLiteral) val),
//                                                                       State.ASSERTED));
//                    }
//                    else if (val instanceof OWLEntity) {
//                        propertyValues.add(PropertyAnnotationValue.get(annotationProp,
//                                                                       renderingManager.getRendering((OWLEntity) val),
//                                                                       State.ASSERTED));
//
//                    }
//                    else if (val instanceof IRI) {
//                        propertyValues.add(PropertyAnnotationValue.get(annotationProp,
//                                                                       IRIData.get((IRI) val, ImmutableMap.of()),
//                                                                       State.ASSERTED));
//                    }
//                }
//            }
//            else {
//                throw new RuntimeException("Unknown property type: " + property);
//            }
//        });
//        return propertyValues;
//    }
//
//    private Set<OWLObject> toOWLObject(FormDataValue dataValue) {
//        if (dataValue instanceof FormDataPrimitive) {
//            FormDataPrimitive formDataPrimitive = (FormDataPrimitive) dataValue;
//            OWLObject owlObject = formDataPrimitive.toOWLObject();
//            return Collections.singleton(owlObject);
//        }
//        else if (dataValue instanceof FormDataList) {
//            FormDataList formDataList = (FormDataList) dataValue;
//            return formDataList.getList().stream().flatMap(e -> toOWLObject(e).stream()).collect(toSet());
//        }
//        else {
//            throw new RuntimeException("Cannot convert to OWL Object");
//        }
//    }
}

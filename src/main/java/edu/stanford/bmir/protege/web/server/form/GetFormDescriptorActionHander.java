package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.form.GetFormDescriptorAction;
import edu.stanford.bmir.protege.web.shared.form.GetFormDescriptorResult;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/04/16
 */
public class GetFormDescriptorActionHander extends AbstractHasProjectActionHandler<GetFormDescriptorAction, GetFormDescriptorResult> {

    @Inject
    public GetFormDescriptorActionHander(ProjectManager projectManager,
                                         AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<GetFormDescriptorAction> getActionClass() {
        return GetFormDescriptorAction.class;
    }

    @Override
    protected GetFormDescriptorResult execute(GetFormDescriptorAction action, Project project, ExecutionContext executionContext) {
        throw new RuntimeException();
//        return getDummy(action.getSubject(), project, project.getDataFactory());
    }



//    private GetFormDescriptorResult getDummy(OWLEntity entity, Project project, final OWLDataFactory dataFactory) {
//        if(!entity.isOWLClass()) {
//            return new GetFormDescriptorResult(project.getProjectId(), entity, FormDescriptor.empty(), FormData.empty());
//        }
//
//        FormDescriptor.Builder builder = FormDescriptor.builder(new FormId("Class"));
//        FormElementId labelFieldId = new FormElementId("Label");
//        builder.addDescriptor(new FormElementDescriptor(
//                labelFieldId,
//                "Label",
//                new StringFieldDescriptor(
//                        "Enter label",
//                        StringType.SIMPLE_STRING,
//                        LineMode.SINGLE_LINE,
//                        ".+",
//                        "Please enter a non-empty label"
//                ),
//                Repeatability.NON_REPEATABLE,
//                Required.REQUIRED
//        ));
//        FormElementId altLabelFieldId = new FormElementId("skos:altLabel");
//        builder.addDescriptor(new FormElementDescriptor(
//                altLabelFieldId,
//                "Synonyms",
//                new StringFieldDescriptor(
//                        "Enter synonym",
//                        StringType.LANG_STRING,
//                        LineMode.SINGLE_LINE,
//                        "",
//                        ""
//                ),
//                Repeatability.REPEATABLE_VERTICAL,
//                Required.OPTIONAL
//        ));
//        FormElementId definitionFieldId = new FormElementId("Definition");
//        builder.addDescriptor(new FormElementDescriptor(
//                definitionFieldId,
//                "Definition",
//                new StringFieldDescriptor(
//                        "Enter label",
//                        StringType.SIMPLE_STRING,
//                        LineMode.SINGLE_LINE,
//                        "",
//                        ""
//                ),
//                Repeatability.NON_REPEATABLE,
//                Required.OPTIONAL
//        ));
//        FormElementId statusFieldId = new FormElementId("ProductionStatus");
//        builder.addDescriptor(new FormElementDescriptor(
//                        statusFieldId,
//                        "Production Status",
//                        new ChoiceFieldDescriptor(
//                                ChoiceFieldType.COMBO_BOX,
//                                Arrays.asList(
//                                        new ChoiceDescriptor("In Production", FormDataPrimitive.get(dataFactory.getOWLClass(IRI.create("http://webprotege.stanford.edu/InProduction")))),
//                                        new ChoiceDescriptor("Out of Production", FormDataPrimitive.get(dataFactory.getOWLClass(IRI.create("http://webprotege.stanford.edu/NotInProduction")))))
//                        ),
//                        Repeatability.NON_REPEATABLE,
//                        Required.REQUIRED
//                )
//        );
//        FormElementId depictionFieldId = new FormElementId("depiction");
//        builder.addDescriptor(new FormElementDescriptor(
//                depictionFieldId,
//                "Depictions",
//                new ImageFieldDescriptor(),
//                Repeatability.REPEATABLE_HORIZONTAL,
//                Required.OPTIONAL
//        ));
//
//        FormElementId manufacturerField = new FormElementId("manufacturerField");
//        List<ChoiceDescriptor> manufacturerChoices;
////        if(project.getClassHierarchyProvider().getAncestors(entity.asOWLClass()).contains(dataFactory.getOWLClass(IRI.create("http://webprotege.stanford.edu/R3boSgK0ZoXcizHloIAZhy")))) {
//        manufacturerChoices = Arrays.asList(
//                new ChoiceDescriptor("Boeing", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/Boeing")))),
//                new ChoiceDescriptor("Airbus", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/AirbusIndustrie")))),
//                new ChoiceDescriptor("Lockheed", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/AirbusIndustrie"))))
//        );
////        }
////        else {
////            manufacturerChoices = Arrays.asList(
////                    new ChoiceDescriptor("Rolls Royce", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/RollsRoyce")))),
////                    new ChoiceDescriptor("General Electric", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/GeneralElectric")))),
////                    new ChoiceDescriptor("Pratt & Whitney", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/PrattAndWhitney"))))
////            );
////        }
//        builder.addDescriptor(new FormElementDescriptor(
//                manufacturerField,
//                "Manufacturer",
//                new ChoiceFieldDescriptor(
//                        ChoiceFieldType.COMBO_BOX,
//                        manufacturerChoices
//                ),
//                Repeatability.NON_REPEATABLE,
//                Required.REQUIRED
//        ));
//
//        List<CompositeFieldDescriptorEntry> childEntries = new ArrayList<>();
//        childEntries.add(new CompositeFieldDescriptorEntry(
//                new FormElementId("Designer.FirstName"),
//                1, 1,
//                new StringFieldDescriptor(
//                        "Enter first name",
//                        StringType.SIMPLE_STRING,
//                        LineMode.SINGLE_LINE,
//                        "",
//                        ""
//                )
//        ));
//        childEntries.add(new CompositeFieldDescriptorEntry(
//                new FormElementId("Designer.LastName"),
//                1, 1,
//                new StringFieldDescriptor(
//                        "Enter last name",
//                        StringType.SIMPLE_STRING,
//                        LineMode.SINGLE_LINE,
//                        "",
//                        ""
//                )
//        ));
//        childEntries.add(new CompositeFieldDescriptorEntry(
//                new FormElementId("Designer.Role"),
//                0, 0,
//                new ChoiceFieldDescriptor(
//                        ChoiceFieldType.COMBO_BOX,
//                        Arrays.asList(
//                                new ChoiceDescriptor("Chief designer", FormDataPrimitive.get(IRI.create("Chief"))),
//                                new ChoiceDescriptor("Assistant designer", FormDataPrimitive.get(IRI.create("Assistant"))),
//                                new ChoiceDescriptor("Junior designer", FormDataPrimitive.get(IRI.create("Junior")))
//                        )
//                )
//        ));
//        CompositeFieldDescriptor designerField = new CompositeFieldDescriptor(childEntries);
//        builder.addDescriptor(new FormElementDescriptor(
//                new FormElementId("Designer"),
//                "Designer",
//                designerField,
//                Repeatability.REPEATABLE_VERTICAL,
//                Required.REQUIRED
//        ));
//
//        FormData.Builder dataBuilder = FormData.builder();
//        EntityFrameTranslator<ClassFrame, OWLClass> translator = new ClassFrameTranslator();
//        ClassFrame frame = translator.getFrame(entity.asOWLClass(), project.getRootOntology(), project);
//        for(PropertyValue pv : frame.getPropertyValues()) {
//            OWLPropertyData propertyData = pv.getProperty();
//            OWLEntity property = propertyData.getEntity();
//            if(property.equals(dataFactory.getRDFSLabel())) {
//                OWLPrimitiveData primitiveData = pv.getValue();
//                OWLObject value = primitiveData.getObject();
//                if (value instanceof OWLLiteral) {
//                    dataBuilder.addData(labelFieldId, FormDataPrimitive.get((OWLLiteral) value));
//                }
//            }
//            else if(property.equals(dataFactory.getOWLAnnotationProperty(SKOSVocabulary.DEFINITION.getIRI()))) {
//                if (pv.getValue() instanceof OWLLiteral) {
//                    dataBuilder.addData(definitionFieldId, FormDataPrimitive.get((OWLLiteral) pv.getValue()));
//                }
//            }
//            else if(property.equals(dataFactory.getOWLDeprecated())) {
//                if(pv.getValue() instanceof OWLLiteral) {
//                    dataBuilder.addData(statusFieldId, FormDataPrimitive.get((OWLLiteral) pv.getValue()));
//                }
//            }
//            else if(property.equals(dataFactory.getOWLAnnotationProperty(SKOSVocabulary.ALTLABEL.getIRI()))) {
//                if(pv.getValue() instanceof OWLLiteral) {
//                    dataBuilder.addData(altLabelFieldId, FormDataPrimitive.get((OWLLiteral) pv.getValue()));
//                }
//            }
//            else if(property
//                      .equals(dataFactory.getOWLAnnotationProperty(IRI.create("http://xmlns.com/foaf/0.1/depiction")))) {
//                if(pv.getValue() instanceof IRI) {
//                    dataBuilder.addData(depictionFieldId, FormDataPrimitive.get((IRI) pv.getValue()));
//                }
//            }
//            else if(((OWLPropertyData) property).getIRI().toString().equals("http://webprotege.stanford.edu/hasManufacturer")) {
//                if(pv.getValue() instanceof OWLNamedIndividual) {
//                    if (pv.getState() == State.ASSERTED) {
//                        dataBuilder.addData(manufacturerField, FormDataPrimitive.get((OWLNamedIndividual) pv.getValue()));
//                    }
//                }
//            }
//            else if(((OWLPropertyData) property).getIRI().toString().equals("http://webprotege.stanford.edu/hasProductionStatus")) {
//                OWLObject value = pv.getValue();
//                if (value instanceof OWLClass) {
//                    dataBuilder.addData(statusFieldId, FormDataPrimitive.get((OWLClass) value));
//                }
//            }
//        }
//        return new GetFormDescriptorResult(
//                project.getProjectId(),
//                entity,
//                builder.build(),
//                dataBuilder.build()
//        );
//    }
}

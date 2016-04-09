package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameTranslator;
import edu.stanford.bmir.protege.web.server.frame.EntityFrameTranslator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.GetFormDescriptorAction;
import edu.stanford.bmir.protege.web.shared.form.GetFormDescriptorResult;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueState;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import javax.inject.Inject;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/04/16
 */
public class GetFormDescriptorActionHander extends AbstractHasProjectActionHandler<GetFormDescriptorAction, GetFormDescriptorResult> {

    private final ValidatorFactory<ReadPermissionValidator> validatorFactory;

    @Inject
    public GetFormDescriptorActionHander(OWLAPIProjectManager projectManager, ValidatorFactory<ReadPermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Class<GetFormDescriptorAction> getActionClass() {
        return GetFormDescriptorAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(GetFormDescriptorAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected GetFormDescriptorResult execute(GetFormDescriptorAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new GetFormDescriptorResult(action.getProjectId(), action.getSubject(), getDummy(action.getSubject(), project, project.getDataFactory()));
    }



    private FormDescriptor getDummy(OWLEntity entity, OWLAPIProject project, final OWLDataFactory dataFactory) {
        if(!entity.isOWLClass()) {
            return FormDescriptor.builder().build();
        }

        FormDescriptor.Builder builder = FormDescriptor.builder();
        FormElementId labelFieldId = new FormElementId("Label");
        builder.addDescriptor(new FormElementDescriptor(
                labelFieldId,
                "Label",
                Repeatability.UNREPEATABLE,
                new StringFieldDescriptor(
                        "Enter label",
                        StringType.SIMPLE_STRING,
                        LineMode.SINGLE_LINE,
                        ""
                )
        ));
        FormElementId altLabelFieldId = new FormElementId("skos:altLabel");
        builder.addDescriptor(new FormElementDescriptor(
           altLabelFieldId,
                "Synonyms",
                Repeatability.REPEATABLE,
                new StringFieldDescriptor(
                        "Enter synonym",
                        StringType.LANG_STRING,
                        LineMode.SINGLE_LINE,
                        ""
                )
        ));
        FormElementId definitionFieldId = new FormElementId("Definition");
        builder.addDescriptor(new FormElementDescriptor(
                definitionFieldId,
                "Definition",
                Repeatability.UNREPEATABLE,
                new StringFieldDescriptor(
                        "Enter label",
                        StringType.SIMPLE_STRING,
                        LineMode.SINGLE_LINE,
                        ""
                )
        ));
        FormElementId statusFieldId = new FormElementId("Status");
        builder.addDescriptor(new FormElementDescriptor(
                statusFieldId,
                "Status",
                Repeatability.UNREPEATABLE,
                new ChoiceFieldDescriptor(
                        ChoiceFieldType.CHECK_BOX,
                        Arrays.asList(
                                new ChoiceDescriptor("Deprecated", FormDataPrimitive.get(dataFactory.getOWLLiteral(true)))
                        )
                )
        ));

        FormElementId manufacturerField = new FormElementId("manufacturerField");
        List<ChoiceDescriptor> manufacturerChoices;
        if(project.getClassHierarchyProvider().getAncestors(entity.asOWLClass()).contains(dataFactory.getOWLClass(IRI.create("http://webprotege.stanford.edu/R3boSgK0ZoXcizHloIAZhy")))) {
            manufacturerChoices = Arrays.asList(
                    new ChoiceDescriptor("Boeing", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/Boeing")))),
                    new ChoiceDescriptor("Airbus", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/AirbusIndustrie")))),
                    new ChoiceDescriptor("Lockheed", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/AirbusIndustrie"))))
            );
        }
        else {
            manufacturerChoices = Arrays.asList(
                    new ChoiceDescriptor("Rolls Royce", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/RollsRoyce")))),
                    new ChoiceDescriptor("General Electric", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/GeneralElectric")))),
                    new ChoiceDescriptor("Pratt & Whitney", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/PrattAndWhitney"))))
            );
        }
        builder.addDescriptor(new FormElementDescriptor(
                manufacturerField,
                "Manufacturer",
                Repeatability.UNREPEATABLE,
                new ChoiceFieldDescriptor(
                        ChoiceFieldType.COMBO_BOX,
                        manufacturerChoices
                )
        ));

        EntityFrameTranslator<ClassFrame, OWLClass> translator = new ClassFrameTranslator();
        ClassFrame frame = translator.getFrame(entity.asOWLClass(), project.getRootOntology(), project);
        for(PropertyValue pv : frame.getPropertyValues()) {
            if(pv.getProperty().equals(dataFactory.getRDFSLabel())) {
                OWLObject value = pv.getValue();
                if (value instanceof OWLLiteral) {
                    builder.addData(labelFieldId, FormDataPrimitive.get((OWLLiteral) value));
                }
            }
            else if(pv.getProperty().equals(dataFactory.getOWLAnnotationProperty(SKOSVocabulary.DEFINITION.getIRI()))) {
                if (pv.getValue() instanceof OWLLiteral) {
                    builder.addData(definitionFieldId, FormDataPrimitive.get((OWLLiteral) pv.getValue()));
                }
            }
            else if(pv.getProperty().equals(dataFactory.getOWLDeprecated())) {
                if(pv.getValue() instanceof OWLLiteral) {
                    builder.addData(statusFieldId, FormDataPrimitive.get((OWLLiteral) pv.getValue()));
                }
            }
            else if(pv.getProperty().equals(dataFactory.getOWLAnnotationProperty(SKOSVocabulary.ALTLABEL.getIRI()))) {
                if(pv.getValue() instanceof OWLLiteral) {
                    builder.addData(altLabelFieldId, FormDataPrimitive.get((OWLLiteral) pv.getValue()));
                }
            }
            else if(pv.getProperty().getIRI().toString().equals("http://webprotege.stanford.edu/hasManufacturer")) {
                if(pv.getValue() instanceof OWLNamedIndividual) {
                    if (pv.getState() == PropertyValueState.ASSERTED) {
                        builder.addData(manufacturerField, FormDataPrimitive.get((OWLNamedIndividual) pv.getValue()));
                    }
                }
            }
        }
        return builder.build();
//
//
//
//        return new FormDescriptor(
//                Arrays.asList(
//                        new FormElementDescriptor(
//                                new FormElementId("TheLabel"),
//                                "Label",
//                                Repeatability.UNREPEATABLE,
//                                new StringFieldDescriptor(
//                                        "Enter label",
//                                        StringType.SIMPLE_STRING,
//                                        LineMode.SINGLE_LINE,
//                                        ""
//                                )
//                        ),
//                        new FormElementDescriptor(new FormElementId("TheComment"), "Comment", Repeatability.UNREPEATABLE, new StringFieldDescriptor("Enter comment", StringType.LANG_STRING, LineMode.MULTI_LINE, "")),
//                        new FormElementDescriptor(new FormElementId("Synonyms"), "Synonyms", Repeatability.REPEATABLE, new StringFieldDescriptor("Enter synonym", StringType.LANG_STRING, LineMode.MULTI_LINE, "")),
//
//                        new FormElementDescriptor(
//                                new FormElementId("EngineConfiguration"),
//                                "Engine Configuration",
//                                Repeatability.REPEATABLE,
//                                new ChoiceFieldDescriptor(
//                                        ChoiceFieldType.COMBO_BOX,
//                                        Arrays.<ChoiceDescriptor>asList(
//                                                new ChoiceDescriptor("Twin Jet - Wing Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/TJWM"))),
//                                                new ChoiceDescriptor("Twin Jet - Tail Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/TJTM"))),
//                                                new ChoiceDescriptor("Tri Jet - Wing/Tail Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/TrJWTM"))),
//                                                new ChoiceDescriptor("Tri Jet - Wing Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/TrJWM"))),
//                                                new ChoiceDescriptor("Quad Jet - Wing Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/QJWM"))),
//                                                new ChoiceDescriptor("Quad Jet - Tail Mounted", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/QJTM")))
//                                        ))),
//                        new FormElementDescriptor(new FormElementId("PossibleRoles"), "Role", Repeatability.UNREPEATABLE, new ChoiceFieldDescriptor(ChoiceFieldType.CHECK_BOX, Arrays.<ChoiceDescriptor>asList(
//                                new ChoiceDescriptor("Passenger", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/Passenger"))),
//                                new ChoiceDescriptor("Cargo", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/Cargo"))),
//                                new ChoiceDescriptor("Combi", FormDataPrimitive.get(DataFactory.getOWLClass("http://aero.com/Combi")))
//                        ))),
//                        new FormElementDescriptor(new FormElementId("ClassName"), "The Class", Repeatability.UNREPEATABLE, new ClassNameFieldDescriptor(Collections.<OWLClass>emptySet()))
//                ),
//        new HashMap<>());
    }
}

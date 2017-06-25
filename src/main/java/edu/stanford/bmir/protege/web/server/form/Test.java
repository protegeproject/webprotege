package edu.stanford.bmir.protege.web.server.form;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.Arrays;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/04/16
 */
public class Test {

    public static void main(String[] args) {
        OWLDataFactory dataFactory = new OWLDataFactoryImpl();
        FormDescriptor.Builder builder = FormDescriptor.builder(new FormId("Class"));
        FormElementId labelFieldId = new FormElementId("Label");
        builder.addDescriptor(new FormElementDescriptor(
                labelFieldId,
                "Label",
                new TextFieldDescriptor(
                        "Enter label",
                        StringType.SIMPLE_STRING,
                        LineMode.SINGLE_LINE,
                        ".+",
                        "Please enter a non-empty label"
                ),
                Repeatability.NON_REPEATABLE,
                Required.REQUIRED
        ));
        FormElementId altLabelFieldId = new FormElementId("skos:altLabel");
        builder.addDescriptor(new FormElementDescriptor(
                altLabelFieldId,
                "Synonyms",
                new TextFieldDescriptor(
                        "Enter synonym",
                        StringType.LANG_STRING,
                        LineMode.SINGLE_LINE,
                        "",
                        ""
                ),
                Repeatability.REPEATABLE_VERTICAL,
                Required.OPTIONAL
        ));
        FormElementId definitionFieldId = new FormElementId("Definition");
        builder.addDescriptor(new FormElementDescriptor(
                definitionFieldId,
                "Definition",
                new TextFieldDescriptor(
                        "Enter label",
                        StringType.SIMPLE_STRING,
                        LineMode.SINGLE_LINE,
                        "",
                        ""
                ),
                Repeatability.NON_REPEATABLE,
                Required.OPTIONAL
        ));
        FormElementId statusFieldId = new FormElementId("ProductionStatus");
        builder.addDescriptor(new FormElementDescriptor(
                        statusFieldId,
                        "Production Status",
                        new ChoiceFieldDescriptor(
                                ChoiceFieldType.COMBO_BOX,
                                Arrays.asList(
                                        ChoiceDescriptor.choice("In Production", FormDataPrimitive.get(dataFactory.getOWLClass(IRI.create("http://webprotege.stanford.edu/InProduction")))),
                                        ChoiceDescriptor.choice("Out of Production", FormDataPrimitive.get(dataFactory.getOWLClass(IRI.create("http://webprotege.stanford.edu/NotInProduction")))))
                        ),
                        Repeatability.NON_REPEATABLE,
                        Required.REQUIRED
                )
        );
        FormElementId depictionFieldId = new FormElementId("depiction");
        builder.addDescriptor(new FormElementDescriptor(
                depictionFieldId,
                "Depictions",
                new ImageFieldDescriptor(),
                Repeatability.REPEATABLE_HORIZONTAL,
                Required.OPTIONAL
        ));

        FormElementId manufacturerField = new FormElementId("manufacturerField");
        List<ChoiceDescriptor> manufacturerChoices;
//        if(project.getClassHierarchyProvider().getAncestors(entity.asOWLClass()).contains(dataFactory.getOWLClass(IRI.create("http://webprotege.stanford.edu/R3boSgK0ZoXcizHloIAZhy")))) {
            manufacturerChoices = Arrays.asList(
                    ChoiceDescriptor.choice("Boeing", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/Boeing")))),
                    ChoiceDescriptor.choice("Airbus", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/AirbusIndustrie")))),
                    ChoiceDescriptor.choice("Lockheed", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/AirbusIndustrie"))))
            );
//        }
//        else {
//            manufacturerChoices = Arrays.asList(
//                    new ChoiceDescriptor("Rolls Royce", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/RollsRoyce")))),
//                    new ChoiceDescriptor("General Electric", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/GeneralElectric")))),
//                    new ChoiceDescriptor("Pratt & Whitney", FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create("http://webprotege.stanford.edu/PrattAndWhitney"))))
//            );
//        }
        builder.addDescriptor(new FormElementDescriptor(
                manufacturerField,
                "Manufacturer",
                new ChoiceFieldDescriptor(
                        ChoiceFieldType.COMBO_BOX,
                        manufacturerChoices
                ),
                Repeatability.NON_REPEATABLE,
                Required.REQUIRED
        ));

        FormDescriptor build = builder.build();
        Gson g = new GsonBuilder()
                .registerTypeAdapter(FormElementDescriptor.class, new FormElementDescriptorSerializer())
                .registerTypeAdapter(FormDataPrimitive.class, new FormDataPrimitiveSerializer())
                .registerTypeAdapter(FormId.class, new FormIdSerializer())
                .registerTypeAdapter(FormElementId.class, new FormElementIdSerializer())
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        System.out.println(g.toJson(build));

    }
}

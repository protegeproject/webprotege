package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.hierarchy.AssertedClassHierarchyProvider;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cern.clhep.Units.s;
import static edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor.choice;
import static edu.stanford.bmir.protege.web.shared.form.field.ChoiceFieldType.COMBO_BOX;
import static java.util.Arrays.asList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/04/16
 */
public class GetFormDescriptorActionHander extends AbstractHasProjectActionHandler<GetFormDescriptorAction, GetFormDescriptorResult> {

    private final ProjectId projectId;

    private final AssertedClassHierarchyProvider classHierarchyProvider;

    private final OWLDataFactory dataFactory;

    private final FormDataRepository formDataRepository;

    @Inject
    public GetFormDescriptorActionHander(@Nonnull AccessManager accessManager,
                                         ProjectId projectId,
                                         AssertedClassHierarchyProvider classHierarchyProvider,
                                         OWLDataFactory dataFactory, FormDataRepository formDataRepository) {
        super(accessManager);
        this.projectId = projectId;
        this.classHierarchyProvider = classHierarchyProvider;
        this.dataFactory = dataFactory;
        this.formDataRepository = formDataRepository;
    }


    @Override
    public Class<GetFormDescriptorAction> getActionClass() {
        return GetFormDescriptorAction.class;
    }

    public GetFormDescriptorResult execute(GetFormDescriptorAction action, ExecutionContext executionContext) {
        return getDummy(action.getSubject());
    }


    private GetFormDescriptorResult getDummy(OWLEntity entity) {
        try {
            if (!entity.isOWLClass()) {
                return new GetFormDescriptorResult(projectId, entity, FormDescriptor.empty(), FormData.empty());
            }

            URL url = GetFormDescriptorActionHander.class.getResource("/form.json");
            System.out.println(url);
            InputStream is = GetFormDescriptorActionHander.class.getResourceAsStream("/form.json");

            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(FormDataValue.class, new FormDataValueDeserializer(dataFactory));
            module.addSerializer(new EntitySerializer());
            module.addSerializer(new IRISerializer());
            module.addSerializer(new LiteralSerializer());
            module.addSerializer(new FormElementIdSerializer());
            module.addSerializer(new FormDataSerializer());
            module.addSerializer(new FormDataObjectSerializer());
            mapper.registerModule(module);
            mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
            FormDescriptor d = mapper.readerFor(FormDescriptor.class).readValue(new BufferedInputStream(is));
            System.out.println(d);

            is.close();

            /*
            FormDescriptor.Builder builder = FormDescriptor.builder(new FormId("Class"));
            FormElementId labelFieldId = FormElementId.get("Label");
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
                    Required.REQUIRED,
                    ""
            ));
            FormElementId altLabelFieldId = FormElementId.get("synonyms");
            builder.addDescriptor(new FormElementDescriptor(
                    altLabelFieldId,
                    "Synonyms",
                    new CompositeFieldDescriptor(
                            asList(
                                    new CompositeFieldDescriptorEntry(
                                            FormElementId.get("synonym"), 1, 1,
                                            new TextFieldDescriptor(
                                                    "Enter synonym",
                                                    StringType.LANG_STRING,
                                                    LineMode.SINGLE_LINE,
                                                    "",
                                                    ""
                                            )),
                                    new CompositeFieldDescriptorEntry(
                                            FormElementId.get("synonymType"), 0, 0,
                                            new ChoiceFieldDescriptor(
                                                    COMBO_BOX,
                                                    asList(
                                                            choice("Exact", FormDataPrimitive.get("EXACT")),
                                                            choice("Narrower", FormDataPrimitive.get("NARROWER")),
                                                            choice("Broader", FormDataPrimitive.get("BROADER"))
                                                    ),
                                                    Collections.emptyList()
                                            )

                                    )
                            ))
                    ,
                    Repeatability.REPEATABLE_VERTICAL,
                    Required.OPTIONAL,
                    ""
            ));
            FormElementId definitionFieldId = FormElementId.get("definition");
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
                    Required.OPTIONAL,
                    ""
            ));
            FormElementId statusFieldId = FormElementId.get("productionStatus");
            builder.addDescriptor(new FormElementDescriptor(
                                          statusFieldId,
                                          "Production Status",
                                          new ChoiceFieldDescriptor(
                                                  COMBO_BOX,
                                                  asList(
                                                          choice("In Production", FormDataPrimitive.get("InProduction")),
                                                          choice("Out of Production", FormDataPrimitive.get("OutOfProduction"))
                                                  ),
                                                  Collections.singletonList(FormDataPrimitive.get("InProduction"))),
                                          Repeatability.NON_REPEATABLE,
                                          Required.REQUIRED,
                                          ""
                                  )
            );

            FormElementId manufacturerField = FormElementId.get("manufacturer");
            List<ChoiceDescriptor> manufacturerChoices;
            manufacturerChoices = asList(
                    choice("Boeing",
                           FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create(
                                   "http://webprotege.stanford.edu/Boeing")))),
                    choice("Airbus",
                           FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create(
                                   "http://webprotege.stanford.edu/AirbusIndustrie")))),
                    choice("Lockheed",
                           FormDataPrimitive.get(dataFactory.getOWLNamedIndividual(IRI.create(
                                   "http://webprotege.stanford.edu/AirbusIndustrie"))))
            );
            FormElementDescriptor manufacturer = new FormElementDescriptor(
                    manufacturerField,
                    "Manufacturer",
                    new ChoiceFieldDescriptor(
                            COMBO_BOX,
                            manufacturerChoices,
                            Collections.emptyList()
                    ),
                    Repeatability.NON_REPEATABLE,
                    Required.REQUIRED,
                    ""
            );
            builder.addDescriptor(manufacturer);

            FormElementId depictionFieldId = FormElementId.get("depiction");
            builder.addDescriptor(new FormElementDescriptor(
                    depictionFieldId,
                    "Depictions",
                    new ImageFieldDescriptor(),
                    Repeatability.REPEATABLE_HORIZONTAL,
                    Required.OPTIONAL,
                    ""
            ));


            List<CompositeFieldDescriptorEntry> childEntries = new ArrayList<>();
            childEntries.add(new CompositeFieldDescriptorEntry(
                    FormElementId.get("firstName"),
                    1, 1,
                    new TextFieldDescriptor(
                            "Enter first name",
                            StringType.SIMPLE_STRING,
                            LineMode.SINGLE_LINE,
                            "",
                            ""
                    )
            ));
            childEntries.add(new CompositeFieldDescriptorEntry(
                    FormElementId.get("lastName"),
                    1, 1,
                    new TextFieldDescriptor(
                            "Enter last name",
                            StringType.SIMPLE_STRING,
                            LineMode.SINGLE_LINE,
                            "",
                            ""
                    )
            ));
            childEntries.add(new CompositeFieldDescriptorEntry(
                    FormElementId.get("role"),
                    0, 0,
                    new ChoiceFieldDescriptor(
                            COMBO_BOX,
                            asList(
                                    choice("Chief designer", FormDataPrimitive.get("Chief")),
                                    choice("Assistant designer", FormDataPrimitive.get("Assistant")),
                                    choice("Junior designer", FormDataPrimitive.get("Junior"))
                            ),
                            Collections.emptyList()
                    )
            ));
            CompositeFieldDescriptor designerField = new CompositeFieldDescriptor(childEntries);
            builder.addDescriptor(new FormElementDescriptor(
                    FormElementId.get("designers"),
                    "Designers",
                    designerField,
                    Repeatability.REPEATABLE_VERTICAL,
                    Required.REQUIRED,
                    ""
            ));
            */
            FormData formData = formDataRepository.get(projectId, entity);
            System.out.println("Got form data from repository for entity: " + entity + " ---> " + formData);
            return new GetFormDescriptorResult(
                    projectId,
                    entity,
                    d,
                    formData
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

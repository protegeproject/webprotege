package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.stanford.bmir.protege.web.server.form.FormDataSerializer;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor.choice;
import static edu.stanford.bmir.protege.web.shared.form.field.ChoiceFieldType.CHECK_BOX;
import static edu.stanford.bmir.protege.web.shared.form.field.LineMode.SINGLE_LINE;
import static edu.stanford.bmir.protege.web.shared.form.field.NumberFieldRange.BoundType.EXCLUSIVE;
import static edu.stanford.bmir.protege.web.shared.form.field.NumberFieldRange.BoundType.INCLUSIVE;
import static edu.stanford.bmir.protege.web.shared.form.field.NumberFieldRange.range;
import static edu.stanford.bmir.protege.web.shared.form.field.NumberFieldType.PLAIN;
import static edu.stanford.bmir.protege.web.shared.form.field.NumberFieldType.SLIDER;
import static edu.stanford.bmir.protege.web.shared.form.field.Repeatability.NON_REPEATABLE;
import static edu.stanford.bmir.protege.web.shared.form.field.Required.OPTIONAL;
import static edu.stanford.bmir.protege.web.shared.form.field.Required.REQUIRED;
import static edu.stanford.bmir.protege.web.shared.form.field.StringType.SIMPLE_STRING;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jun 2017
 */
public class Fiddle {

    public static void main(String[] args) {
        OWLDataFactory df = new OWLDataFactoryImpl();

        FormElementDescriptor descriptor = new FormElementDescriptor(FormElementId.get("HelloForm"),
                                                                     "My Hello Form",
                                                                     new ChoiceFieldDescriptor(CHECK_BOX,
                                                                                               asList(choice(
                                                                                                       "Tail Mounted Quad",
                                                                                                       FormDataPrimitive
                                                                                                               .get(33)),
                                                                                                      choice("Wing Mounted Quad",
                                                                                                             FormDataPrimitive
                                                                                                                     .get("Hello World")),
                                                                                                      choice("Wing Mounted Twin",
                                                                                                             FormDataPrimitive
                                                                                                                     .get(false)),
                                                                                                      choice("Tail Mounted Twin",
                                                                                                             FormDataPrimitive
                                                                                                                     .get(df.getOWLThing())),
                                                                                                      choice("Tail Mounted Tri",
                                                                                                             FormDataPrimitive
                                                                                                                     .get(44))),
                                                                                               emptyList()),
                                                                     NON_REPEATABLE,
                                                                     REQUIRED,
                                                                     "");

        FormElementDescriptor descriptor1 = new FormElementDescriptor(FormElementId.get("Age"),
                                                                      "Age",
                                                                      new NumberFieldDescriptor("###",
                                                                                                range(1,
                                                                                                      INCLUSIVE,
                                                                                                      18,
                                                                                                      EXCLUSIVE),
                                                                                                SLIDER,
                                                                                                7,
                                                                                                ""),
                                                                      NON_REPEATABLE,
                                                                      REQUIRED,
                                                                      "");

        FormElementDescriptor descriptor2 = new FormElementDescriptor(FormElementId.get("OtherElement"),
                                                                      "My other label",
                                                                      new TextFieldDescriptor("Enter a name",
                                                                                              SIMPLE_STRING,
                                                                                              SINGLE_LINE,
                                                                                              ".*",
                                                                                              ""),
                                                                      NON_REPEATABLE,
                                                                      REQUIRED,
                                                                      "");

        OWLClass owlThing = df.getOWLThing();
        FormElementDescriptor descriptor3 = new FormElementDescriptor(FormElementId.get("Entity"),
                                                                      "My Entity",
                                                                      new ClassNameFieldDescriptor(singleton(
                                                                              owlThing),
                                                                                                   NodeType.LEAF, ""),
                                                                      NON_REPEATABLE,
                                                                      REQUIRED,
                                                                      "");


        FormElementDescriptor descriptor4 = new FormElementDescriptor(FormElementId.get("Ind Name"),
                                                                      "Individual Name",
                                                                      new IndividualNameFieldDescriptor(singleton(df.getOWLClass(
                                                                              IRI.create(
                                                                                      "http://protege.stanford.edu/ClsA")))),
                                                                      NON_REPEATABLE,
                                                                      OPTIONAL,
                                                                      "");
        FormElementDescriptor descriptor5 = new FormElementDescriptor(FormElementId.get("Ind Name"),
                                                                      "Individual Name",
                                                                      new NumberFieldDescriptor("#.##", NumberFieldRange.all(), PLAIN, 7, ""),
                                                                      NON_REPEATABLE,
                                                                      OPTIONAL,
                                                                      "");


        List<FormElementDescriptor> formElements = asList(descriptor5);

        FormDescriptor formDescriptor = new FormDescriptor(new FormId("MyForm"),
                                                           formElements);

        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(OWLEntity.class, new EntitySerializer());
            module.addSerializer(new FormDataSerializer());
            mapper.registerModule(module);
            mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
            String s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    formDescriptor
            );
            System.out.println(s);

            System.out.println("--------------------------");

            Map<FormElementId, FormDataValue> map = new HashMap<>();
            Map<String, FormDataValue> dataMap = new HashMap<>();
            dataMap.put("Hello", FormDataPrimitive.get("World"));
            map.put(FormElementId.get("TheDetails"), new FormDataList(FormDataPrimitive.get("X")));
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new FormData(map));

            List<Object> d = mapper.readerFor(FormElementDescriptor.class).readValues(s).readAll();
            System.out.println(d);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

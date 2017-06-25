package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor.choice;
import static edu.stanford.bmir.protege.web.shared.form.field.ChoiceFieldType.CHECK_BOX;
import static edu.stanford.bmir.protege.web.shared.form.field.LineMode.SINGLE_LINE;
import static edu.stanford.bmir.protege.web.shared.form.field.NumberFieldRange.BoundType.EXCLUSIVE;
import static edu.stanford.bmir.protege.web.shared.form.field.NumberFieldRange.BoundType.INCLUSIVE;
import static edu.stanford.bmir.protege.web.shared.form.field.NumberFieldRange.range;
import static edu.stanford.bmir.protege.web.shared.form.field.NumberFieldType.SLIDER;
import static edu.stanford.bmir.protege.web.shared.form.field.Repeatability.NON_REPEATABLE;
import static edu.stanford.bmir.protege.web.shared.form.field.Required.OPTIONAL;
import static edu.stanford.bmir.protege.web.shared.form.field.Required.REQUIRED;
import static edu.stanford.bmir.protege.web.shared.form.field.StringType.SIMPLE_STRING;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jun 2017
 */
public class Fiddle {

    public static void main(String[] args) {
        OWLDataFactory df = new OWLDataFactoryImpl();

        FormElementDescriptor descriptor = new FormElementDescriptor(new FormElementId("HelloForm"),
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
                                                                                                                     .get(44)))),
                                                                     NON_REPEATABLE,
                                                                     REQUIRED);

        FormElementDescriptor descriptor1 = new FormElementDescriptor(new FormElementId("Age"),
                                                                      "Age",
                                                                      new NumberFieldDescriptor("###",
                                                                                                range(1,
                                                                                                      INCLUSIVE,
                                                                                                      18,
                                                                                                      EXCLUSIVE),
                                                                                                SLIDER),
                                                                      NON_REPEATABLE,
                                                                      REQUIRED);

        FormElementDescriptor descriptor2 = new FormElementDescriptor(new FormElementId("OtherElement"),
                                                                      "My other label",
                                                                      new TextFieldDescriptor("Enter a name",
                                                                                              SIMPLE_STRING,
                                                                                              SINGLE_LINE,
                                                                                              ".*",
                                                                                              ""),
                                                                      NON_REPEATABLE,
                                                                      REQUIRED);

        OWLClass owlThing = df.getOWLThing();
        FormElementDescriptor descriptor3 = new FormElementDescriptor(new FormElementId("Entity"),
                                                                      "My Entity",
                                                                      new ClassNameFieldDescriptor(singleton(
                                                                              owlThing),
                                                                                                   NodeType.LEAF),
                                                                      NON_REPEATABLE,
                                                                      REQUIRED);


        FormElementDescriptor descriptor4 = new FormElementDescriptor(new FormElementId("Ind Name"),
                                                                      "Individual Name",
                                                                      new IndividualNameFieldDescriptor(singleton(df.getOWLClass(
                                                                              IRI.create(
                                                                                      "http://protege.stanford.edu/ClsA")))),
                                                                      NON_REPEATABLE,
                                                                      OPTIONAL);
        try {
            ObjectMapper mapper = new ObjectMapper();

            SimpleModule module = new SimpleModule();
            module.addSerializer(OWLEntity.class, new EntitySerializer());
            mapper.registerModule(module);
            mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
            String s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    asList(descriptor, descriptor1, descriptor2, descriptor3, descriptor4)
            );
            System.out.println(s);
//            List<Object> d = mapper.readerFor(FormElementDescriptor.class).readValues(s).readAll();
//            System.out.println(d);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

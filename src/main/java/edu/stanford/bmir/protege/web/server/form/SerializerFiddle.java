package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.EntitySerializer;
import org.semanticweb.owlapi.model.OWLDataFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Jul 2017
 */
public class SerializerFiddle {

    public static void main(String[] args) throws Exception {


        OWLDataFactory dataFactory = new OWLDataFactoryImpl();
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

        InputStream is = SerializerFiddle.class.getResourceAsStream("/form.json");
        BufferedInputStream src = new BufferedInputStream(is);
        FormDescriptor d = mapper.readerFor(FormDescriptor.class).readValue(src);
        System.out.println(d);
    }
}

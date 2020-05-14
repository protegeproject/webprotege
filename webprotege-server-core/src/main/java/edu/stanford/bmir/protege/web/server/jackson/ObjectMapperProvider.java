package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.stanford.bmir.protege.web.server.form.FormControlValueDeserializer;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class ObjectMapperProvider implements Provider<ObjectMapper> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public ObjectMapperProvider() {
        this.dataFactory = new OWLDataFactoryImpl();
    }

    @Override
    public ObjectMapper get() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new GuavaModule());
        SimpleModule module = new SimpleModule();
        module.addSerializer(OWLEntity.class, new OWLEntitySerializer());
        module.addSerializer(OWLProperty.class, new OWLEntitySerializer());
        module.addSerializer(new EntityTypeSerializer());
        module.addDeserializer(EntityType.class, new EntityTypeDeserializer());
        module.addDeserializer(OWLEntity.class, new OWLEntityDeserializer<>(dataFactory, OWLEntity.class));
        module.addDeserializer(OWLNamedIndividual.class, new OWLEntityDeserializer<>(dataFactory, OWLNamedIndividual.class));
        module.addDeserializer(OWLProperty.class, new OWLPropertyDeserializer<>(dataFactory, OWLProperty.class));
        module.addDeserializer(OWLObjectProperty.class, new OWLPropertyDeserializer<>(dataFactory, OWLObjectProperty.class));
        module.addDeserializer(OWLDataProperty.class, new OWLPropertyDeserializer<>(dataFactory, OWLDataProperty.class));
        module.addDeserializer(OWLAnnotationProperty.class, new OWLPropertyDeserializer<>(dataFactory, OWLAnnotationProperty.class));
        module.addDeserializer(OWLDatatype.class, new OWLEntityDeserializer<>(dataFactory, OWLDatatype.class));
        module.addDeserializer(OWLClass.class, new OWLClassDeserializer(dataFactory));
        module.addDeserializer(IRI.class, new IriDeserializer());
        module.addDeserializer(OWLAnnotationValue.class, new OWLAnnotationValueDeserializer(new OWLLiteralDeserializer(dataFactory),
                                                                                            new IriDeserializer()));
        module.addSerializer(OWLLiteral.class, new OWLLiteralSerializer());
        module.addDeserializer(OWLLiteral.class, new OWLLiteralDeserializer(dataFactory));
        module.addDeserializer(PrimitiveFormControlData.class, new FormControlValueDeserializer(dataFactory));
        module.addSerializer(IRI.class, new IriSerializer());
        mapper.registerModule(module);

        return mapper;
    }
}

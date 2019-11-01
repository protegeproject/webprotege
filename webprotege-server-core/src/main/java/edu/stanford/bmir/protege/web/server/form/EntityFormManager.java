package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class EntityFormManager {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public EntityFormManager(@Nonnull OWLDataFactory dataFactory) {
        this.dataFactory = checkNotNull(dataFactory);
    }

    public Optional<FormDescriptor> getFormDescriptor(@Nonnull OWLEntity entity) {
        try(InputStream is = GetFormDescriptorActionHander.class.getResourceAsStream("/amino-acid-form.json")) {

            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(FormDataValue.class, new FormDataValueDeserializer(dataFactory));
            mapper.registerModule(module);
            mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
            FormDescriptor formDescriptor = mapper.readerFor(FormDescriptor.class)
                                                  .readValue(new BufferedInputStream(is));
            is.close();
            return Optional.of(formDescriptor);
        } catch(IOException e) {
            return Optional.empty();
        }
    }

}

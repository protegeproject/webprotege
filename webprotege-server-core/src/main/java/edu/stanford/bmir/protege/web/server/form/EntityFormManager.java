package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class EntityFormManager {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final EntityFormRepository entityFormRepository;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public EntityFormManager(@Nonnull OWLDataFactory dataFactory,
                             @Nonnull EntityFormRepository entityFormRepository,
                             @Nonnull ObjectMapper objectMapper) {
        this.dataFactory = checkNotNull(dataFactory);
        this.entityFormRepository = entityFormRepository;
        this.objectMapper = objectMapper;
    }

    public Optional<FormDescriptor> getFormDescriptor(@Nonnull OWLEntity entity,
                                                      @Nonnull ProjectId projectId) {
        try(InputStream is = GetFormDescriptorActionHander.class.getResourceAsStream("/amino-acid-form.json")) {
//            FormDescriptor formDescriptor = objectMapper.readerFor(FormDescriptor.class)
//                                                  .readValue(new BufferedInputStream(is));
//            is.close();
//            entityFormRepository.saveFormDescriptor(projectId, formDescriptor);
            return entityFormRepository.findFormDescriptors(projectId)
                                .findFirst();

//            return Optional.of(formDescriptor);
        } catch(IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}

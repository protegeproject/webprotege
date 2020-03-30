package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-13
 */
public class EntityFormSubjectFactory {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private SuppliedNameTemplateResolver templateResolver;

    @Inject
    public EntityFormSubjectFactory(@Nonnull OWLDataFactory dataFactory,
                                    @Nonnull SuppliedNameTemplateResolver templateResolver) {
        this.dataFactory = checkNotNull(dataFactory);
        this.templateResolver = checkNotNull(templateResolver);
    }

    @Nonnull
    public OWLEntity createSubject(@Nonnull String suppliedNameTemplate,
                                   @Nonnull EntityType<?> entityType,
                                   @Nonnull Optional<String> langTag) {
        if(suppliedNameTemplate.isBlank()) {
            throw new RuntimeException("Supplied name template is blank");
        }
        var resolvedTemplate = templateResolver.resolveTemplateVariables(suppliedNameTemplate, entityType);
        return DataFactory.getFreshOWLEntity(entityType, resolvedTemplate, langTag, dataFactory);
    }
}

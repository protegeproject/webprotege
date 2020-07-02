package edu.stanford.bmir.protege.web.server.form;

import org.semanticweb.owlapi.model.EntityType;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-12
 */
public class SuppliedNameTemplateResolver {

    @Inject
    public SuppliedNameTemplateResolver() {
    }

    public String resolveTemplateVariables(String suppliedNameTemplate,
                                           EntityType<?> entityType) {
        return suppliedNameTemplate
                .replace("${uuid}", UUID.randomUUID().toString())
                .replace("${type}", entityType.getPrintName());

    }
}

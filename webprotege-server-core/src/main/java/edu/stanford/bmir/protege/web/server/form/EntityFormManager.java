package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.match.MatchingEngine;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class EntityFormManager {

    @Nonnull
    private final EntityFormRepository entityFormRepository;

    @Nonnull
    private final EntityFormSelectorRepository entityFormSelectorRepository;

    @Nonnull
    private final MatchingEngine matchingEngine;

    @Inject
    public EntityFormManager(@Nonnull EntityFormRepository entityFormRepository,
                             @Nonnull EntityFormSelectorRepository entityFormSelectorRepository,
                             @Nonnull MatchingEngine matchingEngine) {
        this.entityFormRepository = entityFormRepository;
        this.entityFormSelectorRepository = entityFormSelectorRepository;
        this.matchingEngine = matchingEngine;
    }

    public Optional<FormDescriptor> getFormDescriptor(@Nonnull OWLEntity entity,
                                                      @Nonnull ProjectId projectId) {
        var formId = entityFormSelectorRepository.findFormTriggers(projectId)
                                    .filter(selector -> matchingEngine.matches(entity,
                                                                               selector.getCriteria()))
                                    .map(EntityFormSelector::getFormId)
                                    .findFirst();

        return formId.flatMap(id -> entityFormRepository.findFormDescriptor(projectId, id));
    }

}

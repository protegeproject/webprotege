package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.match.MatchingEngine;
import edu.stanford.bmir.protege.web.shared.form.EntityFormSelector;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static dagger.internal.codegen.DaggerStreams.toImmutableSet;

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

    public ImmutableList<FormDescriptor> getFormDescriptors(@Nonnull OWLEntity entity,
                                                           @Nonnull ProjectId projectId) {
        var formIds = entityFormSelectorRepository.findFormSelectors(projectId)
                                    .filter(selector -> matchingEngine.matches(entity,
                                                                               selector.getCriteria()))
                                    .map(EntityFormSelector::getFormId)
                                    .collect(toImmutableSet());
        return entityFormRepository.findFormDescriptors(formIds, projectId)
                            .collect(toImmutableList());
    }

}

package edu.stanford.bmir.protege.web.client.search;

import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyFilterType;
import edu.stanford.bmir.protege.web.shared.match.criteria.SubClassOfCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilterId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-16
 */
public class EntitySearchFilterProvider implements Provider<EntitySearchFilter> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final UuidV4Provider uuidV4Provider;

    @Inject
    public EntitySearchFilterProvider(@Nonnull ProjectId projectId, @Nonnull UuidV4Provider uuidV4Provider) {
        this.projectId = checkNotNull(projectId);
        this.uuidV4Provider = checkNotNull(uuidV4Provider);
    }

    @Override
    public EntitySearchFilter get() {
        return EntitySearchFilter.get(getFreshFilterId(),
                                      projectId,
                                      LanguageMap.empty(),
                                      SubClassOfCriteria.get(DataFactory.getOWLThing(),
                                                             HierarchyFilterType.ALL));
    }

    private EntitySearchFilterId getFreshFilterId() {
        return EntitySearchFilterId.get(uuidV4Provider.get());
    }
}

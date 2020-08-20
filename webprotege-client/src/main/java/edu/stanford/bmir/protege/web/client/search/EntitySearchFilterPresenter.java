package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.ObjectPresenter;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilterId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-16
 */
public class EntitySearchFilterPresenter implements ObjectPresenter<EntitySearchFilter> {

    @Nonnull
    private final EntitySearchFilterView view;

    @Nonnull
    private final EntityCriteriaPresenter entityCriteriaPresenter;

    private Optional<EntitySearchFilterId> currentId = Optional.empty();

    @Nonnull
    private final UuidV4Provider uuidV4Provider;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public EntitySearchFilterPresenter(@Nonnull EntitySearchFilterView view,
                                       @Nonnull EntityCriteriaPresenter entityCriteriaPresenter,
                                       @Nonnull UuidV4Provider uuidV4Provider,
                                       @Nonnull ProjectId projectId) {
        this.view = checkNotNull(view);
        this.entityCriteriaPresenter = checkNotNull(entityCriteriaPresenter);
        this.uuidV4Provider = checkNotNull(uuidV4Provider);
        this.projectId = checkNotNull(projectId);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        entityCriteriaPresenter.start(view.getCriteriaContainer());
    }

    @Override
    public void setValue(@Nonnull EntitySearchFilter filter) {
        currentId = Optional.of(filter.getId());
        view.setLanguageMap(filter.getLabel());
        entityCriteriaPresenter.setCriteria(filter.getEntityMatchCriteria().asCompositeRootCriteria());
    }

    @Nonnull
    @Override
    public Optional<EntitySearchFilter> getValue() {
        Optional<? extends CompositeRootCriteria> criteria = entityCriteriaPresenter.getCriteria();
        if(!criteria.isPresent()) {
            return Optional.empty();
        }
        LanguageMap languageMap = view.getLanguageMap();
        EntitySearchFilterId filterId = currentId.orElseGet(() -> EntitySearchFilterId.get(uuidV4Provider.get()));
        EntityMatchCriteria entityMatchCriteria = criteria.get();
        EntitySearchFilter filter = EntitySearchFilter.get(filterId,
                                                           projectId,
                                                           languageMap, entityMatchCriteria);
        return Optional.of(filter);
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        return "Search Filter";
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {
        // Doesn't Change
    }
}

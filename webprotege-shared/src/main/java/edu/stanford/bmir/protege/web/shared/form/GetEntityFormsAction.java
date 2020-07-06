package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.form.data.FormRegionFilter;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class GetEntityFormsAction implements ProjectAction<GetEntityFormsResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private ImmutableList<FormId> formFilter;

    private ImmutableList<FormPageRequest> formPageRequests;

    private LangTagFilter langTagFilter;

    private ImmutableSet<FormRegionOrdering> orderings;

    private ImmutableSet<FormRegionFilter> filters;

    /**
     * Get the forms for an entity
     * @param projectId The project id
     * @param entity The entity
     * @param formFilter A list of {@link FormId}s.  If the list is empty then all forms that are applicable
 *                   to the entity will be retrieved.  If the list is non-empty then the only the applicable
 *                   forms that have form Ids in the list will be retrieved.
     * @param formPageRequests A list of page requests pertaining to various regions on the form.
     * @param langTagFilter A language tag filter that can be used to filter data in a specific language.
     * @param orderings A set of region orderings that can be used to specify the ordering of specific regions of
     * @param filters A set of region filters that can be used to filter values
     */
    public GetEntityFormsAction(@Nonnull ProjectId projectId,
                                @Nonnull OWLEntity entity,
                                @Nonnull ImmutableList<FormId> formFilter,
                                @Nonnull ImmutableList<FormPageRequest> formPageRequests,
                                @Nonnull LangTagFilter langTagFilter,
                                @Nonnull ImmutableSet<FormRegionOrdering> orderings, ImmutableSet<FormRegionFilter> filters) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.formFilter = checkNotNull(formFilter);
        this.formPageRequests = checkNotNull(formPageRequests);
        this.langTagFilter = checkNotNull(langTagFilter);
        this.orderings = checkNotNull(orderings);
        this.filters = checkNotNull(filters);
    }

    @GwtSerializationConstructor
    private GetEntityFormsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public ImmutableList<FormPageRequest> getFormPageRequests() {
        return formPageRequests;
    }

    @Nonnull
    public LangTagFilter getLangTagFilter() {
        return langTagFilter;
    }

    @Nonnull
    public ImmutableSet<FormRegionOrdering> getGridControlOrdering() {
        return orderings;
    }

    public ImmutableList<FormId> getFormFilter() {
        return formFilter;
    }

    @Nonnull
    public ImmutableSet<FormRegionFilter> getFilters() {
        return filters;
    }
}

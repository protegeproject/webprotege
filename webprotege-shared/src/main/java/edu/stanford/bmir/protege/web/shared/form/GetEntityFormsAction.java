package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
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

    private ImmutableList<FormPageRequest> formPageRequests;

    private LangTagFilter langTagFilter;

    private ImmutableSet<FormRegionOrdering> orderings;

    public GetEntityFormsAction(@Nonnull ProjectId projectId,
                                @Nonnull OWLEntity entity,
                                @Nonnull ImmutableList<FormPageRequest> formPageRequests,
                                @Nonnull LangTagFilter langTagFilter,
                                @Nonnull ImmutableSet<FormRegionOrdering> orderings) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.formPageRequests = checkNotNull(formPageRequests);
        this.langTagFilter = checkNotNull(langTagFilter);
        this.orderings = checkNotNull(orderings);
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
}

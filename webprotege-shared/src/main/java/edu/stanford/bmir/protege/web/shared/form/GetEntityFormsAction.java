package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
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

    public GetEntityFormsAction(@Nonnull ProjectId projectId,
                                @Nonnull OWLEntity entity,
                                @Nonnull ImmutableList<FormPageRequest> formPageRequests,
                                @Nonnull LangTagFilter langTagFilter) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.formPageRequests = checkNotNull(formPageRequests);
        this.langTagFilter = checkNotNull(langTagFilter);
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
}

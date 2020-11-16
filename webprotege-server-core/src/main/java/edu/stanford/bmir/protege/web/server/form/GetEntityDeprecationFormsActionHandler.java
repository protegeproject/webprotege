package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.axiom.AxiomSubjectProvider;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.index.AxiomsByReferenceIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.inject.ProjectComponent;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.form.FormPurpose;
import edu.stanford.bmir.protege.web.shared.form.GetEntityDeprecationFormsAction;
import edu.stanford.bmir.protege.web.shared.form.GetEntityDeprecationFormsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-21
 */
public class GetEntityDeprecationFormsActionHandler extends AbstractProjectActionHandler<GetEntityDeprecationFormsAction, GetEntityDeprecationFormsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityFormManager entityFormManager;

    @Nonnull
    private final ProjectComponent projectComponent;

    @Nonnull
    private final AxiomsByReferenceIndex axiomsByReferenceIndex;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AxiomSubjectProvider axiomSubjectProvider;

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public GetEntityDeprecationFormsActionHandler(@Nonnull ProjectId projectId,
                                                  @Nonnull AccessManager accessManager,
                                                  @Nonnull EntityFormManager entityFormManager,
                                                  @Nonnull ProjectComponent projectComponent,
                                                  @Nonnull AxiomsByReferenceIndex axiomsByReferenceIndex,
                                                  @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                  @Nonnull AxiomSubjectProvider axiomSubjectProvider,
                                                  @Nonnull ProjectDetailsManager projectDetailsManager) {
        super(accessManager);
        this.projectId = checkNotNull(projectId);
        this.entityFormManager = checkNotNull(entityFormManager);
        this.projectComponent = checkNotNull(projectComponent);
        this.axiomsByReferenceIndex = checkNotNull(axiomsByReferenceIndex);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.axiomSubjectProvider = checkNotNull(axiomSubjectProvider);
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<GetEntityDeprecationFormsAction> getActionClass() {
        return GetEntityDeprecationFormsAction.class;
    }

    @Nonnull
    @Override
    public GetEntityDeprecationFormsResult execute(@Nonnull GetEntityDeprecationFormsAction action,
                                                   @Nonnull ExecutionContext executionContext) {
        var entityCreationForms = entityFormManager.getFormDescriptors(action.getEntity(),
                                                                       action.getProjectId(),
                                                                       FormPurpose.ENTITY_DEPRECATION);

        var formDtoTranslatorComponent = projectComponent.getFormDescriptorDtoTranslatorComponent(new EntityFrameFormDataModule());
        var formDtoTranslator = formDtoTranslatorComponent.getFormDescriptorDtoTranslator();
        var formDtos = entityCreationForms.stream()
                                          .map(formDtoTranslator::toFormDescriptorDto)
                                          .collect(toImmutableList());
        var referencesCount = projectOntologiesIndex.getOntologyIds()
                .flatMap(ontId -> axiomsByReferenceIndex.getReferencingAxioms(Collections.singleton(action.getEntity()),
                                                                          ontId))
                .filter(ax -> !hasSubject(action.getEntity(), ax))
                .count();
        var criteria = projectDetailsManager.getProjectDetails(projectId)
                .getEntityDeprecationSettings()
                .getReplacedByFilter()
                .orElse(null);
        return GetEntityDeprecationFormsResult.get(formDtos, referencesCount, criteria);
    }

    private boolean hasSubject(@Nonnull OWLEntity entity, OWLAxiom ax) {
        return axiomSubjectProvider.getSubject(ax).map(s -> s.equals(entity) || s.equals(entity.getIRI())).orElse(false);
    }
}

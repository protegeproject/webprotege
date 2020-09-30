package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.entity.FreshEntityIri;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataDto;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-30
 */
public class CreateEntityViaFormAction extends AbstractCreateEntitiesAction<CreateEntityViaFormResult, OWLEntity> {

    private FreshEntityIri freshEntityIri;

    private FormDataDto formDataDto;

    public CreateEntityViaFormAction(@Nonnull ProjectId projectId,
                                     @Nonnull String sourceText,
                                     String langTag,
                                     FreshEntityIri freshEntityIri) {
        super(projectId, sourceText, langTag);
        this.freshEntityIri = freshEntityIri;
    }
}

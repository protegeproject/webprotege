package edu.stanford.bmir.protege.web.client.inject;

import dagger.Subcomponent;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorClientModule;
import edu.stanford.bmir.protege.web.client.project.ProjectPresenter;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsPresenter;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Oct 2016
 */
@Subcomponent(
        modules = {
                ClientProjectModule.class,
                PrimitiveDataEditorClientModule.class
        }
)
@ProjectSingleton
public interface ClientProjectComponent {

    ProjectPresenter getProjectPresenter();

    SharingSettingsPresenter getSharingSettingsPresenter();
}

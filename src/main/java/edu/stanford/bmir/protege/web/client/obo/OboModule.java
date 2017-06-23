package edu.stanford.bmir.protege.web.client.obo;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.webprotege.shared.annotations.PortletModule;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
@Module
@PortletModule
public class OboModule {

    @Provides
    OBOTermDefinitionEditor providesOboTermDefinitionEditor(OBOTermDefinitionEditorImpl impl) {
        return impl;
    }

    @Provides
    OBOTermIdEditor providesOboTermIdEditor(OBOTermIdEditorImpl impl) {
        return impl;
    }

    @Provides
    OBOTermCrossProductEditor providesOboTermCrossProductEditor(OBOTermCrossProductEditorImpl impl) {
        return impl;
    }

    @Provides
    OBOTermSynonymEditor providesOboTermSynonymEditor(OBOTermSynonymEditorImpl impl) {
        return impl;
    }
}

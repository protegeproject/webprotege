package edu.stanford.bmir.protege.web.client.inject;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import edu.stanford.bmir.protege.web.client.WebProtegeInitializer;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorClientModule;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.generated.UIFactory;
import edu.stanford.bmir.protege.web.client.workspace.WorkspaceView;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/12/15
 */
@GinModules({ApplicationClientModule.class, PrimitiveDataEditorClientModule.class})
public interface ApplicationClientInjector extends Ginjector {

    public WebProtegeInitializer getWebProtegeInitializer();

    public WorkspaceView getWorkspaceView();

    public PrimitiveDataEditorImpl getPrimitiveDataEditor();

    public UIFactory getUiFactory();

    public ClientApplicationProperties getClientApplicationProperties();
}

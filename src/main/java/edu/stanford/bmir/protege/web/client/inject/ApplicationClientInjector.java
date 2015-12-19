package edu.stanford.bmir.protege.web.client.inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorClientModule;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.client.workspace.WorkspaceView;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/12/15
 */
@GinModules({ApplicationClientModule.class, PrimitiveDataEditorClientModule.class})
public interface ApplicationClientInjector extends Ginjector {

    public static final ApplicationClientInjector instance = GWT.create(ApplicationClientInjector.class);

    public WorkspaceView getWorkspaceView();

    public PrimitiveDataEditorImpl getPrimitiveDataEditor();
}

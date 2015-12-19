package edu.stanford.bmir.protege.web.client.inject;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.place.PlaceManager;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.workspace.WorkspaceView;
import edu.stanford.bmir.protege.web.client.workspace.WorkspaceViewImpl;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2014
 */
public class ApplicationClientModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(EventBus.class).to(SimpleEventBus.class).asEagerSingleton();
        bind(ProjectManager.class).asEagerSingleton();
        bind(DispatchServiceManager.class).asEagerSingleton();
        bind(WorkspaceView.class).to(WorkspaceViewImpl.class);

        bind(LoggedInUserManager.class).asEagerSingleton();
        bind(PlaceManager.class).asEagerSingleton();
        bind(SelectionModel.class).asEagerSingleton();
    }
}

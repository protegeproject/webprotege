package edu.stanford.bmir.protege.web.client.ui.ontology.id;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdResult;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class OntologyIdPortlet extends AbstractEntityPortlet {

    private OntologyIdEditor editor;

    private boolean loaded = false;

    public OntologyIdPortlet(Project project) {
        super(project);
    }

    public OntologyIdPortlet(Project project, boolean initialize) {
        super(project, initialize);
    }

    @Override
    public void initialize() {
        editor = new OntologyIdViewImpl();
        add(editor.getWidget());
        setTitle("Ontology Id");
        editor.setEnabled(false);
    }

    @Override
    public void onPermissionsChanged() {
//        editor.setEnabled(hasWritePermission());
    }

    @Override
    protected void onRefresh() {
        DispatchServiceManager.get().execute(new GetRootOntologyIdAction(getProjectId()), new AsyncCallback<GetRootOntologyIdResult>() {
            @Override
            public void onFailure(Throwable caught) {
                MessageBox.alert("There was a problem retrieving the current ontology Id from the server.");
            }

            @Override
            public void onSuccess(GetRootOntologyIdResult result) {
                editor.setValue(result.getObject());
            }
        });
    }

    @Override
    public void reload() {
        if(!loaded) {
            loaded = true;
            onRefresh();
        }
    }

    @Override
    public Collection<EntityData> getSelection() {
        return null;
    }
}

package edu.stanford.bmir.protege.web.client.obo;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermXRefsAction;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermXRefsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/05/2012
 */
@Portlet(id = "portlets.obo.TermXRefs", title = "OBO Term XRefs")
public class OBOTermXRefsEditorPortletPresenter extends AbstractOBOTermPortletPresenter {


    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final XRefListEditor editor;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;


    @Inject
    public OBOTermXRefsEditorPortletPresenter(@Nonnull SelectionModel selectionModel,
                                              @Nonnull ProjectId projectId,
                                              @Nonnull DispatchServiceManager dispatch,
                                              @Nonnull XRefListEditor editor,
                                              @Nonnull LoggedInUserProjectPermissionChecker permissionChecker) {
        super(selectionModel, projectId);
        this.dispatch = dispatch;
        this.editor = editor;
        this.permissionChecker = permissionChecker;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editor);
        editor.setEnabled(false);
        permissionChecker.hasPermission(BuiltInAction.EDIT_ONTOLOGY, editor::setEnabled);

    }

    @Override
    protected boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        editor.getValue().ifPresent(xrefs -> dispatch.execute(new SetOboTermXRefsAction(getProjectId(), entity, xrefs),
                                                              result -> {
                                                              }));
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        dispatch.execute(new GetOboTermXRefsAction(getProjectId(), entity),
                         this,
                         result -> editor.setValue(result.getxRefs()));
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
    }

    @Override
    protected String getTitlePrefix() {
        return "XRefs";
    }
}

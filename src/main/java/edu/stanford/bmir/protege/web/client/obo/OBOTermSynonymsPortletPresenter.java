package edu.stanford.bmir.protege.web.client.obo;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermSynonymsAction;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermSynonymsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
@Portlet(id = "portlets.obo.TermSynonyms", title = "OBO Term Synonyms")
public class OBOTermSynonymsPortletPresenter extends AbstractOBOTermPortletPresenter {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final OBOTermSynonymListEditor editor;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public OBOTermSynonymsPortletPresenter(@Nonnull SelectionModel selectionModel,
                                           @Nonnull ProjectId projectId,
                                           @Nonnull DispatchServiceManager dispatch,
                                           @Nonnull OBOTermSynonymListEditor editor,
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
        permissionChecker.hasPermission(EDIT_ONTOLOGY, editor::setEnabled);
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        dispatch.execute(new GetOboTermSynonymsAction(getProjectId(), entity),
                         this,
                         result -> editor.setValue(result.getSynonyms()));
    }

    @Override
    protected boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        editor.getValue().ifPresent(synonyms -> {
            dispatch.execute(new SetOboTermSynonymsAction(getProjectId(), entity, synonyms),
                             result -> {});
        });
    }

    @Override
    protected String getTitlePrefix() {
        return "Synonyms";
    }

}

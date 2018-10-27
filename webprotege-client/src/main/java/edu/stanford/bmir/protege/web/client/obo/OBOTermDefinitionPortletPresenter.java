package edu.stanford.bmir.protege.web.client.obo;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermDefinitionAction;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermDefinitionAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
@Portlet(id = "portlets.obo.TermDefinition", title = "OBO Term Definition")
public class OBOTermDefinitionPortletPresenter extends AbstractOBOTermPortletPresenter {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final OBOTermDefinitionEditor editor;

    @Inject
    public OBOTermDefinitionPortletPresenter(SelectionModel selectionModel,
                                             ProjectId projectId,
                                             @Nonnull DispatchServiceManager dispatch,
                                             @Nonnull OBOTermDefinitionEditor editor, DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
        this.dispatch = dispatch;
        this.editor = editor;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editor.asWidget());
    }

    @Override
    protected boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        dispatch.execute(new GetOboTermDefinitionAction(getProjectId(), entity),
                         this,
                         result -> editor.setValue(result.getDefinition()));
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        editor.getValue().ifPresent(def -> dispatch.execute(new SetOboTermDefinitionAction(getProjectId(), entity, def),
                                                            result -> {}));
    }


    @Override
    protected String getTitlePrefix() {
        return "Definition";
    }

}

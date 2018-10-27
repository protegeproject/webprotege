package edu.stanford.bmir.protege.web.client.obo;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermRelationshipsAction;
import edu.stanford.bmir.protege.web.shared.obo.OBORelationship;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermRelationships;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermRelationshipsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
@Portlet(id = "portlets.obo.TermRelationships", title = "OBO Term Relationships")
public class OBOTermRelationshipPortletPresenter extends AbstractOBOTermPortletPresenter {


    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final OBOTermRelationshipEditor editor;

    private Optional<List<OBORelationship>> pristineValue = Optional.empty();

    @Inject
    public OBOTermRelationshipPortletPresenter(@Nonnull SelectionModel selectionModel,
                                               @Nonnull ProjectId projectId,
                                               @Nonnull DispatchServiceManager dispatch,
                                               @Nonnull OBOTermRelationshipEditor editor,
                                               @Nonnull DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
        this.dispatch = dispatch;
        this.editor = editor;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(this.editor.getWidget());
    }

    @Override
    protected boolean isDirty() {
        boolean dirty = editor.isDirty();
        GWT.log("[OBOTermRelationshipPortletPresenter] isDirty = " + dirty);
        return dirty;
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        if(!(entity instanceof OWLClass)) {
            return;
        }
        List<OBORelationship> relationships = editor.getValue().orElse(Collections.emptyList());
        dispatch.execute(new SetOboTermRelationshipsAction(getProjectId(), entity,
                                                           new OBOTermRelationships(Sets.newHashSet(relationships))),
                         result -> {});

    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        Optional<OWLEntity> current = getSelectedEntity();
        if(!current.isPresent()) {
            editor.clearValue();
            pristineValue = Optional.empty();
            return;
        }
        if(!(current.get() instanceof OWLClass)) {
            editor.clearValue();
            pristineValue = Optional.empty();
            return;
        }
        dispatch.execute(new GetOboTermRelationshipsAction(getProjectId(), entity.asOWLClass()),
                         this,
                         result -> {
                             List<OBORelationship> listOfRels = new ArrayList<>(result.getRelationships().getRelationships());
                             pristineValue = Optional.of(listOfRels);
                             editor.setValue(listOfRels);
                         });
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
        pristineValue = Optional.empty();
    }

    @Override
    protected String getTitlePrefix() {
        return "Relationships";
    }

}

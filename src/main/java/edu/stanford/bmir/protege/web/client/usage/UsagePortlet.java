package edu.stanford.bmir.protege.web.client.usage;

import com.google.common.base.Optional;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletActionHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageAction;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageResult;
import edu.stanford.bmir.protege.web.shared.usage.UsageFilter;
import edu.stanford.bmir.protege.web.shared.usage.UsageReference;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class UsagePortlet extends AbstractOWLEntityPortlet {

    private UsageView usageView;

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public UsagePortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserManager loggedInUserManager) {
        super(selectionModel, eventBus, projectId, loggedInUserManager);
        this.dispatchServiceManager = dispatchServiceManager;
        usageView = new UsageViewImpl();
        getContentHolder().setWidget(usageView.asWidget());
        usageView.addValueChangeHandler(new ValueChangeHandler<UsageFilter>() {
            @Override
            public void onValueChange(ValueChangeEvent<UsageFilter> event) {
                updateDisplayForSelectedEntity();
            }
        });
        addPortletAction(new PortletAction("Filter", new PortletActionHandler() {
            @Override
            public void handleActionInvoked(PortletAction action, ClickEvent event) {
                usageView.showFilter();
            }
        }));
    }


    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        updateDisplayForSelectedEntity();
    }

    private void updateDisplayForSelectedEntity() {
        final Optional<OWLEntity> sel = getSelectedEntity();
        if(sel.isPresent()) {
            showUsageForEntity(sel.get());
        }
        else {
            setTitle("No entity selected");
            usageView.clearData();
        }
    }

    private void showUsageForEntity(final OWLEntity entity) {
        final GetUsageAction action = new GetUsageAction(entity, getProjectId(), Optional.of(usageView.getUsageFilter()));
        dispatchServiceManager.execute(action, new DispatchServiceCallback<GetUsageResult>() {

            @Override
            protected String getErrorMessage(Throwable throwable) {
                return "There was a problem retrieving the usage for the selected entity";
            }

            @Override
            public void handleSuccess(GetUsageResult result) {
                final Collection<UsageReference> references = result.getUsageReferences();
                final int visibleReferences = references.size();
                final int totalReferences = result.getTotalUsageCount();
                setTitle("Usage (definition and references) [showing " + visibleReferences + " references of " + totalReferences + "]");
                usageView.setData(entity, references);
            }
        });
    }
}

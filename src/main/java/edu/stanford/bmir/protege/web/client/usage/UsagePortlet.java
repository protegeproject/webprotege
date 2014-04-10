package edu.stanford.bmir.protege.web.client.usage;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageAction;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageResult;
import edu.stanford.bmir.protege.web.shared.usage.UsageFilter;
import edu.stanford.bmir.protege.web.shared.usage.UsageReference;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class UsagePortlet extends AbstractOWLEntityPortlet {

    private static final int DEFAULT_HEIGHT = 250;

    private UsageView usageView;

    public UsagePortlet(Project project) {
        super(project);
        setHeight(DEFAULT_HEIGHT);
    }

    public UsagePortlet(Project project, boolean initialize) {
        super(project, initialize);
        setHeight(DEFAULT_HEIGHT);
    }


    @Override
    public void initialize() {
        usageView = new UsageViewImpl();
        add(usageView.asWidget());
        usageView.addValueChangeHandler(new ValueChangeHandler<UsageFilter>() {
            @Override
            public void onValueChange(ValueChangeEvent<UsageFilter> event) {
                reload();
            }
        });
    }

    @Override
    public void reload() {
        final Optional<OWLEntityData> sel = getSelectedEntityData();
        if(sel.isPresent()) {
            final OWLEntityData entityData = sel.get();
            showUsageForEntity(entityData);
        }
        else {
            setTitle("No entity selected");
            usageView.clearData();
        }
    }

    private void showUsageForEntity(final OWLEntityData entityData) {
        final OWLEntity entity = entityData.getEntity();
        final GetUsageAction action = new GetUsageAction(entity, getProjectId(), Optional.of(usageView.getUsageFilter()));
        DispatchServiceManager.get().execute(action, new AsyncCallback<GetUsageResult>() {
            @Override
            public void onFailure(Throwable caught) {
                MessageBox.alert("There was a problem retrieving the usage for the selected entity");
                setTitle("Error retrieving definition and usage of " + entityData.getBrowserText());
                usageView.clearData();
            }

            @Override
            public void onSuccess(GetUsageResult result) {
                final Collection<UsageReference> references = result.getUsageReferences();
                final int visibleReferences = references.size();
                final int totalReferences = result.getTotalUsageCount();
                setTitle("Usage of " + entityData.getBrowserText() + " (definition and references) [showing " + visibleReferences + " references of " + totalReferences + "]");
                usageView.setData(entity, references);
            }
        });
    }


    @Override
    public Collection<EntityData> getSelection() {
        return null;
    }
}

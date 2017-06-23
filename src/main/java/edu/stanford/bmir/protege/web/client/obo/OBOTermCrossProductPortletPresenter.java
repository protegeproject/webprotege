package edu.stanford.bmir.protege.web.client.obo;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermCrossProductAction;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermCrossProduct;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermCrossProductAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
@Portlet(id = "portlets.obo.TermCrossProduct", title = "OBO Term Cross Product")
public class OBOTermCrossProductPortletPresenter extends AbstractOBOTermPortletPresenter {

    @Nonnull
    private final OBOTermCrossProductEditor editor;

    @Nonnull
    private final DispatchServiceManager dispatch;

    private Optional<OBOTermCrossProduct> pristineValue = Optional.empty();

    @Inject
    public OBOTermCrossProductPortletPresenter(@Nonnull SelectionModel selectionModel,
                                               @Nonnull ProjectId projectId,
                                               @Nonnull OBOTermCrossProductEditor editor,
                                               @Nonnull DispatchServiceManager dispatch) {
        super(selectionModel, projectId);
        this.editor = editor;
        this.dispatch = dispatch;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editor.asWidget());
    }

    @Override
    protected void clearDisplay() {
        pristineValue = Optional.empty();
        editor.clearValue();
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        if (!(entity.isOWLClass())) {
            editor.clearValue();
        }
        else {
            dispatch.execute(new GetOboTermCrossProductAction(getProjectId(), entity.asOWLClass()),
                             this,
                             result -> {
                                 pristineValue = Optional.of(result.getCrossProduct());
                                 GWT.log("Got XP: " + result.getCrossProduct());
                                 editor.setValue(result.getCrossProduct());
                             });
        }
    }

    @Override
    protected boolean isDirty() {
        return !pristineValue.equals(editor.getValue());
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        if (!(entity.isOWLClass())) {
            return;
        }
        if (!editor.getValue().isPresent()) {
            return;
        }
        editor.getValue()
              .ifPresent(crossProduct -> dispatch.execute(new SetOboTermCrossProductAction(getProjectId(),
                                                                                           entity.asOWLClass(),
                                                                                           crossProduct),
                                                          result -> {}));
    }

    @Override
    protected String getTitlePrefix() {
        return "Cross product";
    }
}

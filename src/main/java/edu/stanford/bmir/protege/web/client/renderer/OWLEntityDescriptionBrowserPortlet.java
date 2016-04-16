package edu.stanford.bmir.protege.web.client.renderer;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class OWLEntityDescriptionBrowserPortlet extends AbstractWebProtegePortlet {

    private final HTML html;

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public OWLEntityDescriptionBrowserPortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, loggedInUserProvider, projectId);
        this.dispatchServiceManager = dispatchServiceManager;
        html = new HTML();
        getContentHolder().setWidget(new ScrollPanel(html));
        addProjectEventHandler(ClassFrameChangedEvent.TYPE, new ClassFrameChangedEventHandler() {
            @Override
            public void classFrameChanged(ClassFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
        addProjectEventHandler(ObjectPropertyFrameChangedEvent.TYPE, new ObjectPropertyFrameChangedEventHandler() {
            @Override
            public void objectPropertyFrameChanged(ObjectPropertyFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
        addProjectEventHandler(DataPropertyFrameChangedEvent.TYPE, new DataPropertyFrameChangedEventHandler() {
            @Override
            public void dataPropertyFrameChanged(DataPropertyFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
        addProjectEventHandler(AnnotationPropertyFrameChangedEvent.TYPE, new AnnotationPropertyFrameChangedEventHandler() {

            @Override
            public void annotationPropertyFrameChanged(AnnotationPropertyFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
        addProjectEventHandler(NamedIndividualFrameChangedEvent.TYPE, new NamedIndividualFrameChangedEventHandler() {
            @Override
            public void namedIndividualFrameChanged(NamedIndividualFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
        addProjectEventHandler(AnnotationPropertyFrameChangedEvent.TYPE, new AnnotationPropertyFrameChangedEventHandler() {

            @Override
            public void annotationPropertyFrameChanged(AnnotationPropertyFrameChangedEvent event) {
                handleEntityChange(event.getEntity());
            }
        });
        setTitle("OWL description");
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        if(entity.isPresent()) {
            dispatchServiceManager.execute(new GetEntityRenderingAction(getProjectId(), entity.get()),
                                                 new DispatchServiceCallback<GetEntityRenderingResult>() {
                                                     @Override
                                                     public void handleSuccess(GetEntityRenderingResult result) {
                                                         html.setHTML(result.getRendering());
                                                     }
                                                 });
            setTitle("OWL " + entity.get().getEntityType().getPrintName() + " description");
        }
    }


    private void handleEntityChange(OWLEntity entity) {
        if(Optional.of(entity).equals(getSelectedEntity())) {
            handleAfterSetEntity(getSelectedEntity());
        }
    }

}

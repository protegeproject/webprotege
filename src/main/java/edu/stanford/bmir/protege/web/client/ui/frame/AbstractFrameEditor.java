package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.HasEntityDataProvider;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataResult;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/01/2013
 */
public abstract class AbstractFrameEditor<O> extends SimplePanel implements ValueEditor<O> {

    private ProjectId projectId;

    private final DispatchServiceManager dispatchServiceManager;

    /**
     * Creates an empty panel that uses a DIV for its contents.
     */
    protected AbstractFrameEditor(ProjectId projectId, DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    final public void setValue(final O object) {
        if (object instanceof HasSignature) {
            final HasSignature hasSignature = (HasSignature) object;
            dispatchServiceManager.execute(new GetEntityDataAction(projectId, ImmutableSet.copyOf(hasSignature.getSignature())), new DispatchServiceCallback<GetEntityDataResult>() {

                @Override
                public void handleSuccess(final GetEntityDataResult result) {
                    setValue(object, new HasEntityDataProvider() {
                        @Override
                        public Optional<OWLEntityData> getEntityData(OWLEntity entity) {
                            return Optional.fromNullable(result.getEntityDataMap().get(entity));
                        }
                    });
                }
            });
        }
        else {
            setValue(object, new HasEntityDataProvider() {
                @Override
                public Optional<OWLEntityData> getEntityData(OWLEntity entity) {
                    return Optional.absent();
                }
            });
        }
    }

    public abstract void setValue(O object, HasEntityDataProvider entityDataProvider);
}

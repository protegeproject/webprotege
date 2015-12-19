package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import org.semanticweb.owlapi.model.EntityType;

import java.io.DataInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class EntityDataContextSelector implements EditorManagerSelector {

    private Map<EntityType<?>, EditorManager<?, ?>> map = new HashMap<EntityType<?>, EditorManager<?, ?>>();

    public EntityDataContextSelector(DispatchServiceManager dispatchServiceManager) {
        map.put(EntityType.CLASS, new ClassFrameEditorManager(dispatchServiceManager));
        map.put(EntityType.OBJECT_PROPERTY, new ObjectPropertyFrameEditorManager(dispatchServiceManager));
        map.put(EntityType.DATA_PROPERTY, new DataPropertyFrameEditorManager(dispatchServiceManager));
        map.put(EntityType.ANNOTATION_PROPERTY, new AnnotationPropertyFrameEditorManager(dispatchServiceManager));
        map.put(EntityType.NAMED_INDIVIDUAL, new NamedIndividualFrameEditorManager(dispatchServiceManager));
    }

    @Override
    public boolean canEditContext(EditorCtx editorCtx) {
        if(!(editorCtx instanceof OWLEntityDataContext)) {
            return false;
        }
        EntityType<?> entityType = ((OWLEntityDataContext) editorCtx).getEntityData().getEntity().getEntityType();
        return map.containsKey(entityType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends EditorCtx, O extends Serializable> EditorManager<C, O> getEditorManager(EditorCtx editorContext) {
        EntityType entityType = ((OWLEntityDataContext) editorContext).getEntityData().getEntity().getEntityType();
        return (EditorManager<C, O>) map.get(entityType);
    }
}

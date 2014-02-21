package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class EditorContextMapper {

    private List<EditorManagerSelector> selectors = new ArrayList<EditorManagerSelector>();

    public <C extends EditorCtx, O extends Serializable> Optional<EditorManager<C, O>> getEditorManager(EditorCtx editorCtx) {
        for(EditorManagerSelector selector : selectors) {
            if(selector.canEditContext(editorCtx)) {
                EditorManager<C, O> man = selector.getEditorManager(editorCtx);
                return Optional.of(man);
            }
        }
        return Optional.absent();
    }

    public void registerSelector(EditorManagerSelector selector) {
        selectors.add(selector);
    }

}

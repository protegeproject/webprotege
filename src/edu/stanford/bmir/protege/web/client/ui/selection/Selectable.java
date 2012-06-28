package edu.stanford.bmir.protege.web.client.ui.selection;

import java.util.Collection;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

/**
 * A generic interface for things that are selectable. Often (always?) in this
 * application, listeners don't really care if they are listening to a List or a
 * Table or a Tree. They just want to find out when the selection changes and to
 * be able to find out the current selection list. This interface allows
 * listeners to do this. There are a variety of adapters available that turn
 * component specific selection events into Selectable events. There are also
 * Selectable versions of all of the standard components.
 */
public interface Selectable {

    void addSelectionListener(SelectionListener listener);

    void notifySelectionListeners(SelectionEvent selectionEvent);

    void removeSelectionListener(SelectionListener listener);
    
    Collection<EntityData> getSelection();
    
    void setSelection(Collection<EntityData> selection);
}

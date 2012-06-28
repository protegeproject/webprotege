package edu.stanford.bmir.protege.web.client.ui.selection;

public class SelectionEvent {
	public static final int SELECTION_CHANGED = 1;
	private Selectable selectable;
	
	public SelectionEvent(Selectable selectable) {
    	this.selectable = selectable;
    }

    public Selectable getSelectable() {
        return selectable;
    }
}

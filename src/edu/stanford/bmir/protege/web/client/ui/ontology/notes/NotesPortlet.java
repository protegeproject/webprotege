package edu.stanford.bmir.protege.web.client.ui.ontology.notes;

import java.util.ArrayList;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class NotesPortlet extends AbstractEntityPortlet {
	
	protected NotesGrid notesGrid;
	protected boolean topLevel;

	public NotesPortlet(Project project) {
		this(project, false);
	}
	
	public NotesPortlet(Project project, boolean topLevel) {
		super(project);
		this.topLevel = topLevel;
	}

	public void reload() {
		if (_currentEntity != null) {			
			setTitle(topLevel ? "Ontology notes" : "Notes for " + _currentEntity.getBrowserText());		
		}		
		notesGrid.setEntity(_currentEntity);
	}
	
	public void initialize() {
		setTitle("Notes");
		notesGrid = new NotesGrid(project, topLevel);
		add(notesGrid);
	}

	public ArrayList getSelection() {
		//TODO: Implement later
		return null;
	}
	
	public boolean isTopLevel() {
		return topLevel;
	}

	public void setTopLevel(boolean topLevel) {
		this.topLevel = topLevel;
		notesGrid.setTopLevel(topLevel);
	}
	
	protected void onRefresh() {
		notesGrid.refresh();	
	}
}

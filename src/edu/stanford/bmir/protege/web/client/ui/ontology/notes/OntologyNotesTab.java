package edu.stanford.bmir.protege.web.client.ui.ontology.notes;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;

public class OntologyNotesTab extends AbstractTab {

	public OntologyNotesTab(Project project) {
		super(project);
	}

	protected void buildUI() {
		NotesPortlet ontologyNotesPortlet = new NotesPortlet(project);
		ontologyNotesPortlet.setTopLevel(true);
		addPortlet(ontologyNotesPortlet, 0);
		ontologyNotesPortlet.setHeight(500);

		setControllingPortlet(ontologyNotesPortlet);
		ontologyNotesPortlet.reload();
	}

	@Override
	public void setup() {
	    setLabel("Ontology Notes");
	    super.setup();
	    buildUI();
	}

}

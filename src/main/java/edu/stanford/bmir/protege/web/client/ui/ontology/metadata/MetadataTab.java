package edu.stanford.bmir.protege.web.client.ui.ontology.metadata;

import com.gwtext.client.widgets.layout.ColumnLayoutData;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * A tab that shows metadata about an ontology such as the imported 
 * ontologies, comments, default language, version information, etc.
 * 
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class MetadataTab extends AbstractTab {

	public MetadataTab(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}
}

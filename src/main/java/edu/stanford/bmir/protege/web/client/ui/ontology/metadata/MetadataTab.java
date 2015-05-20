package edu.stanford.bmir.protege.web.client.ui.ontology.metadata;

import com.gwtext.client.widgets.layout.ColumnLayoutData;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;

/**
 * A tab that shows metadata about an ontology such as the imported 
 * ontologies, comments, default language, version information, etc.
 * 
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class MetadataTab extends AbstractTab {

	protected ImportsTreePortlet importsPortlet;

	public MetadataTab(Project project) {
		super(project);		
	}

	protected static ColumnLayoutData[] getDefaultColumnLayout() {
		return new ColumnLayoutData[] {
				new ColumnLayoutData(.5),
				new ColumnLayoutData(.5)
		};
	}
	
	protected void buildUI() {
//		importsPortlet = new ImportsTreePortlet(selectionModel, project);
//		addPortlet(importsPortlet, 0);
//
//
//		setControllingPortlet(importsPortlet);
	
	}
	
}

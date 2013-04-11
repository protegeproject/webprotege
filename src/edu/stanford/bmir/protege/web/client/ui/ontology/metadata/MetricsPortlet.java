package edu.stanford.bmir.protege.web.client.ui.ontology.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

/**
 * @author Jennifer Vendetti
 */
public class MetricsPortlet extends AbstractEntityPortlet {
	
	protected static boolean initialized = false;
	protected MetricsGrid metricsGrid;

	public MetricsPortlet(Project project) {
		super(project);
	}

	public void reload() {
		if (initialized) return;
		metricsGrid.reload();
	}

    @Override
    protected void onRefresh() {
        super.onRefresh();
        metricsGrid.reload();
    }

    public void initialize() {
		setTitle("Ontology Metrics for " + getProject().getDisplayName());
		
		metricsGrid = new MetricsGrid(getProjectId());
		add(metricsGrid);
		
		metricsGrid.reload();
		
		/*
		 * Don't want metrics to change based on what is selected in the 
		 * imported ontologies tree (unlike other portlets that do update).
		 * This portlet is part of the Metadata tab and the controlling 
		 * portlet for that tab is the ImportsTreePortlet.
		 */
		initialized = true;		
	}

    @Override
    public Collection<EntityData> getSelection() {
        return Collections.emptySet();
    }
}

package edu.stanford.bmir.protege.web.client.ui.hierarchy;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.library.syntaxhighlighting.SyntaxHighlighter;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/06/2012
 */
public class HierarchyPortlet extends AbstractEntityPortlet {

    public HierarchyPortlet(Project project) {
        super(project);
    }

    @Override
    public void reload() {
    }

    @Override
    public void initialize() {
//        add(new AbstractWebProtegeHierarchyView(null));
        add(new SyntaxHighlighter());
    }

    public Collection<EntityData> getSelection() {
        return Collections.emptySet();
    }
}

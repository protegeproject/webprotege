package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.client.ui.editor.EditorPortlet;
import edu.stanford.bmir.protege.web.client.ui.notes.DiscussionThreadPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.widgetmap.shared.node.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class PerspectiveLayoutManagerImpl implements PerspectiveLayoutManager {

    @Override
    public PerspectiveLayout getPerspectiveLayout(ProjectId projectId, UserId userId, PerspectiveId perspectiveId) {
//        if(perspectiveId.equals(new PerspectiveId("Classes"))) {
            ParentNode parentNode = new ParentNode(Direction.HORIZONTAL);
        NodeProperties props = NodeProperties.builder().setValue("portlet", ClassTreePortlet.class.getName()).build();
        parentNode.addChild(new TerminalNode(new TerminalNodeId()).toBuilder().withProperties(props).build(), 0.3);
        NodeProperties props2 = NodeProperties.builder().setValue("portlet", EditorPortlet.class.getName()).build();
        parentNode.addChild(new TerminalNode(new TerminalNodeId()).toBuilder().withProperties(props2).build(), 0.5);
        NodeProperties props3 = NodeProperties.builder().setValue("portlet", DiscussionThreadPortlet.class.getName()).build();
        parentNode.addChild(new TerminalNode(new TerminalNodeId()).toBuilder().withProperties(props3).build(), 0.2);
            return new PerspectiveLayout(perspectiveId, parentNode);
//        }
//        return new PerspectiveLayout(perspectiveId, new TerminalNode());
    }

    @Override
    public void setPerspectiveLayout(ProjectId projectId, UserId userId, PerspectiveLayout layout) {

    }

    @Override
    public void clearPerspectiveLayout(ProjectId projectId, UserId userId) {

    }
}

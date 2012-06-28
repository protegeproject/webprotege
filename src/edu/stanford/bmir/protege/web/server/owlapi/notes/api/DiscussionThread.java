package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2012
 */
public class DiscussionThread {

    private List<DiscussionThreadNode> nodes = new ArrayList<DiscussionThreadNode>();

    public DiscussionThread() {
    }

    public DiscussionThread(List<DiscussionThreadNode> nodes) {
        this.nodes.addAll(nodes);
    }

    public List<DiscussionThreadNode> getRootNodes() {
        return nodes;
    }

    public DiscussionThread addChild(DiscussionThreadNode parent, DiscussionThreadNode child) {
        return null;
    }

}

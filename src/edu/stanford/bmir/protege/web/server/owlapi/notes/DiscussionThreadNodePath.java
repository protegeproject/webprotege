package edu.stanford.bmir.protege.web.server.owlapi.notes;

import edu.stanford.bmir.protege.web.server.owlapi.notes.api.DiscussionThreadNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2012
 */
public class DiscussionThreadNodePath {

    private List<DiscussionThreadNode> path = new ArrayList<DiscussionThreadNode>();


    public DiscussionThreadNodePath(DiscussionThreadNode node) {
        path.add(node);
    }

    public DiscussionThreadNodePath(List<DiscussionThreadNode> path) {
        this.path.addAll(path);
    }
    
    public List<DiscussionThreadNode> getPath() {
        return new ArrayList<DiscussionThreadNode>(path);
    }
}

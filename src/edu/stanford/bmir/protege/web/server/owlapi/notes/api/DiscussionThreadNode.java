package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import edu.stanford.bmir.protege.web.server.owlapi.notes.DiscussionThreadNodePath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2012
 */
public class DiscussionThreadNode {

    private DiscussionThreadNode parent;

    private List<DiscussionThreadNode> children = new ArrayList<DiscussionThreadNode>();
    
    private Note note;

    private DiscussionThread discussionThread;
    
    public DiscussionThreadNode(Note note, List<DiscussionThreadNode> children) {
        this.note = note;
        for(DiscussionThreadNode childNode : children) {
            if(childNode.parent != null) {
                throw new RuntimeException(childNode + " is already a child of " + childNode.parent);
            }
            childNode.parent = this;
            children.add(childNode);
        }
        
    }
    
    public DiscussionThread getDiscussionThread() {
        return discussionThread;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public DiscussionThreadNode getParent() {
        return parent;
    }

    public DiscussionThreadNodePath getPathToRoot() {
        List<DiscussionThreadNode> result = new ArrayList<DiscussionThreadNode>();
        result.add(this);
        DiscussionThreadNode par = parent;
        while(par != null) {
            result.add(0, par);
            par = par.parent;
        }
        return new DiscussionThreadNodePath(result);
    }

    public int getDepth() {
        DiscussionThreadNode par = parent;
        int result = 0;
        while(par != null) {
            result++;
            par = par.parent;
        }
        return result;
    }

    public int getChildCount() {
        return children.size();
    }

    public List<DiscussionThreadNode> getChildren() {
        return Collections.emptyList();
    }

    public int getDescendantCount() {
        return 0;
    }

    public Note getNote() {
        return note;
    }

}

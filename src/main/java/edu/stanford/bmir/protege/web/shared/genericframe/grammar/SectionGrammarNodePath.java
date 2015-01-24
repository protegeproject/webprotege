package edu.stanford.bmir.protege.web.shared.genericframe.grammar;

import edu.stanford.bmir.protege.web.shared.genericframe.grammar.node.SectionGrammarNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2013
 */
public class SectionGrammarNodePath implements Serializable {

    private List<SectionGrammarNode> path = new ArrayList<SectionGrammarNode>();

    /**
     * For the purposes of serialization only
     */
    private SectionGrammarNodePath() {
    }

    public SectionGrammarNodePath(SectionGrammarNode... path) {
        this.path.addAll(Arrays.asList(path));
    }

    protected SectionGrammarNodePath(List<SectionGrammarNode> path) {
        this.path.addAll(path);
    }

    public int size() {
        return path.size();
    }

    public SectionGrammarNode get(int index) {
        if(index < 0 || index >= path.size()) {
            throw new IndexOutOfBoundsException();
        }
        return path.get(index);
    }

    public List<SectionGrammarNode> getPath() {
        return new ArrayList<SectionGrammarNode>(path);
    }

    public boolean startsWith(SectionGrammarNodePath prefix) {
        checkNotNull(prefix, "prefix must not be null");
        if(!(prefix.size() <= path.size())) {
            return false;
        }
        for(int i = 0; i < prefix.size(); i++) {
            SectionGrammarNode prefixNode = prefix.get(i);
            SectionGrammarNode thisNode = path.get(i);
            if(!prefixNode.equals(thisNode)) {
                return false;
            }
        }
        return true;
    }

    public SectionGrammarNode getNextNode(SectionGrammarNodePath prefix) {
        checkNotNull(prefix, "Prefix must not be null");
        if(!(prefix.size() < path.size())) {
            throw new IndexOutOfBoundsException("Specified prefix is too long");
        }
        if(!startsWith(prefix)) {
            throw new PathDoesNotStartWithPrefixException(this, prefix);
        }
        return path.get(prefix.size());
    }



}

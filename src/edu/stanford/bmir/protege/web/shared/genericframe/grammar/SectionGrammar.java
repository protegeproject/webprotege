package edu.stanford.bmir.protege.web.shared.genericframe.grammar;

import edu.stanford.bmir.protege.web.shared.genericframe.grammar.node.SectionGrammarNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2013
 */
public class SectionGrammar implements Serializable {

    private int depth;

    private List<SectionGrammarNodePath> paths = new ArrayList<SectionGrammarNodePath>();

    /**
     * For the purposes of serialization only
     */
    private SectionGrammar() {
    }

    public SectionGrammar(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public void addEntry(SectionGrammarNodePath path) {
        if(depth != path.size()) {
            throw new RuntimeException("Invalid depth: " + path.size() + " expected depth of " + depth);
        }
        paths.add(path);
    }


    public List<SectionGrammarNode> getNextNodesForPath(SectionGrammarNodePath prefix) {
        if(prefix.size() >= depth) {
            return Collections.emptyList();
        }
        List<SectionGrammarNode> result = new ArrayList<SectionGrammarNode>();
        for(SectionGrammarNodePath path : paths) {
            if(path.startsWith(prefix)) {
                result.add(path.getNextNode(prefix));
            }
        }
        return result;
    }

    public List<SectionGrammarNode> getStartNodes() {
        List<SectionGrammarNode> result = new ArrayList<SectionGrammarNode>();
        for(SectionGrammarNodePath path : paths) {
            result.add(path.get(0));
        }
        return result;
    }





}

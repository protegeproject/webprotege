package edu.stanford.bmir.protege.web.shared.genericframe.grammar.node;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2013
 */
public abstract class SectionGrammarNode implements Serializable {

    /**
     * For the purposes of serialization only
     */
    protected SectionGrammarNode() {

    }

    public abstract String getTypePlaceholderText();

    public abstract <R, E extends Exception> R accept(SectionGrammarNodeVisitor<R, E> visitor) throws E;
}

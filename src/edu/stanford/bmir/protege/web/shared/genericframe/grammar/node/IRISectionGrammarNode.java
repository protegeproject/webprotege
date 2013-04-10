package edu.stanford.bmir.protege.web.shared.genericframe.grammar.node;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2013
 */
public class IRISectionGrammarNode extends SectionGrammarNode {

    public static final String TYPE_NAME = "IRI";

    public IRISectionGrammarNode() {
    }

    @Override
    public String getTypePlaceholderText() {
        return TYPE_NAME;
    }

    @Override
    public <R, E extends Exception> R accept(SectionGrammarNodeVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "IRINode".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof IRISectionGrammarNode;
    }
}

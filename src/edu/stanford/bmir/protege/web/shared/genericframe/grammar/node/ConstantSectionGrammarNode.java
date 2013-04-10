package edu.stanford.bmir.protege.web.shared.genericframe.grammar.node;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2013
 */
public class ConstantSectionGrammarNode extends SectionGrammarNode {

    private String constantValue;

    private ConstantSectionGrammarNode() {
    }

    public ConstantSectionGrammarNode(String constantValue) {
        this.constantValue = constantValue;
    }

    @Override
    public String getTypePlaceholderText() {
        return constantValue;
    }

    @Override
    public <R, E extends Exception> R accept(SectionGrammarNodeVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "ConstantNode".hashCode() + constantValue.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ConstantSectionGrammarNode)) {
            return false;
        }
        ConstantSectionGrammarNode other = (ConstantSectionGrammarNode) obj;
        return this.constantValue.equals(other.constantValue);
    }

}

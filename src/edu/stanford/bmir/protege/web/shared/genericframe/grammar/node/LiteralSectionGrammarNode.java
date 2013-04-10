package edu.stanford.bmir.protege.web.shared.genericframe.grammar.node;


import org.semanticweb.owlapi.vocab.OWL2Datatype;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2013
 */
public class LiteralSectionGrammarNode extends SectionGrammarNode {

    private OWL2Datatype constrainingType;



    public LiteralSectionGrammarNode() {
        this(OWL2Datatype.RDFS_LITERAL);
    }

    public LiteralSectionGrammarNode(OWL2Datatype constrainingType) {
        this.constrainingType = constrainingType;
    }

    public OWL2Datatype getConstrainingType() {
        return constrainingType;
    }

    public boolean isAnyType() {
        return constrainingType == OWL2Datatype.RDFS_LITERAL;
    }

    @Override
    public String getTypePlaceholderText() {
        return isAnyType() ? "Literal e.g. string, integer, double etc." : constrainingType.getShortName();
    }

    @Override
    public <R, E extends Exception> R accept(SectionGrammarNodeVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "LiteralNode".hashCode() + constrainingType.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof LiteralSectionGrammarNode)) {
            return false;
        }
        LiteralSectionGrammarNode other = (LiteralSectionGrammarNode) obj;
        return this.constrainingType.equals(other.constrainingType);
    }
}

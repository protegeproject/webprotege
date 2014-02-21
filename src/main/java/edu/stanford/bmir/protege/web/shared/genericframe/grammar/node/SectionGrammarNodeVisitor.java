package edu.stanford.bmir.protege.web.shared.genericframe.grammar.node;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2013
 */
public interface SectionGrammarNodeVisitor<R, E extends Exception> {

    R visit(EntityTypeSectionGrammarNode node) throws E;

    R visit(IRISectionGrammarNode iriNode) throws E;

    R visit(LiteralSectionGrammarNode literalNode) throws E;

    R visit(ConstantSectionGrammarNode constantNode) throws E;

}

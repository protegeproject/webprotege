package edu.stanford.bmir.protege.web.shared.genericframe.grammar.node;

import com.google.common.base.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2013
 */
public class SectionGrammarNodeVisitorAdapter<R, E extends Exception> implements SectionGrammarNodeVisitor<Optional<R>, E> {

    public Optional<R> getDefaultReturnValue() {
        return Optional.absent();
    }

    @Override
    public Optional<R> visit(EntityTypeSectionGrammarNode node) throws E {
        return getDefaultReturnValue();
    }

    @Override
    public Optional<R> visit(IRISectionGrammarNode iriNode) throws E {
        return getDefaultReturnValue();
    }

    @Override
    public Optional<R> visit(LiteralSectionGrammarNode literalNode) throws E {
        return getDefaultReturnValue();
    }

    @Override
    public Optional<R> visit(ConstantSectionGrammarNode constantNode) throws E {
        return getDefaultReturnValue();
    }
}

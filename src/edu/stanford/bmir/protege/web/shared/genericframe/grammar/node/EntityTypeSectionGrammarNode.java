package edu.stanford.bmir.protege.web.shared.genericframe.grammar.node;

import org.semanticweb.owlapi.model.EntityType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2013
 */
public class EntityTypeSectionGrammarNode extends SectionGrammarNode  {

    private EntityType<?> entityType;

    /**
     * For the purposes of serialization only
     */
    protected EntityTypeSectionGrammarNode() {
    }

    public EntityTypeSectionGrammarNode(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    @Override
    public String getTypePlaceholderText() {
        return entityType.getName();
    }

    @Override
    public int hashCode() {
        return "EntityTypeNode".hashCode() + entityType.hashCode();
    }

    @Override
    public <R, E extends Exception> R accept(SectionGrammarNodeVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EntityTypeSectionGrammarNode)) {
            return false;
        }
        EntityTypeSectionGrammarNode other = (EntityTypeSectionGrammarNode) obj;
        return this.entityType.equals(other.entityType);
    }
}

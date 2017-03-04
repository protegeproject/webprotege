package edu.stanford.bmir.protege.web.client.place;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/05/2014
 */
public class ItemSelection implements Iterable<Item<?>> {

    private final ImmutableList<Item<?>> items;

    private ItemSelection(Iterable<? extends Item<?>> items) {
        this.items = ImmutableList.copyOf(items);
    }

    private ItemSelection() {
        this.items = ImmutableList.<Item<?>>builder().build();
    }

    public static <T> ItemSelection empty() {
        return new ItemSelection();
    }

    public List<Item<?>> getItems() {
        return items;
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean contains(Item<?> item) {
        return items.contains(item);
    }

    public java.util.Optional<Item<?>> getFirst() {
        if(items.isEmpty()) {
            return java.util.Optional.empty();
        }
        else {
            return java.util.Optional.<Item<?>>of(items.get(0));
        }
    }

    public Optional<Item<?>> getLast() {
        if(items.isEmpty()) {
            return Optional.absent();
        }
        else {
            return Optional.<Item<?>>of(items.get(items.size() - 1));
        }
    }

    public <I> void visitItems(Item.Type<I> type, Item.Handler<I> handler) {
        for(Item<?> item : items) {
            item.visit(type, handler);
        }
    }

    public Iterator<Item<?>> iterator() {
        return items.iterator();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("ItemSelection")
                .addValue(items).toString();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ItemSelection)) {
            return false;
        }
        ItemSelection other = (ItemSelection) o;
        return this.items.equals(other.items);
    }

    @Override
    public int hashCode() {
        return "ItemSelection".hashCode() + items.hashCode();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<Item<?>> items = Lists.newArrayList();

        public Builder addItem(Item<?> item) {
            items.add(item);
            return this;
        }

        public Builder addEntity(OWLEntity entity) {
            entity.accept(new OWLEntityVisitor() {
                @Override
                public void visit(OWLClass cls) {
                    items.add(new OWLClassItem(cls));
                }

                @Override
                public void visit(OWLObjectProperty property) {
                    items.add(new OWLObjectPropertyItem(property));
                }

                @Override
                public void visit(OWLDataProperty property) {
                    items.add(new OWLDataPropertyItem(property));
                }

                @Override
                public void visit(OWLNamedIndividual individual) {
                    items.add(new OWLNamedIndividualItem(individual));
                }

                @Override
                public void visit(OWLDatatype datatype) {

                }

                @Override
                public void visit(OWLAnnotationProperty property) {
                    items.add(new OWLAnnotationPropertyItem(property));
                }
            });
            return this;
        }

        public Builder addItems(ItemSelection itemSelection) {
            items.addAll(itemSelection.getItems());
            return this;
        }

        public Builder addItems(Collection<? extends Item<?>> items) {
            this.items.addAll(items);
            return this;
        }

        public Builder clear() {
            items.clear();
            return this;
        }

        public ItemSelection build() {
            return new ItemSelection(items);
        }
    }

}

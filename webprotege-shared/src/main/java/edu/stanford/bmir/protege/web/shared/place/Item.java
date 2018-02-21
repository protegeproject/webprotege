package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.base.MoreObjects;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/05/2014
 *
 * Represents an item which is an object that has some kind of type.
 */
public abstract class Item<T> {

    private T item;

    public abstract Type<T> getAssociatedType();

    public abstract String getItemRendering();

    public Item(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    /**
     * Visits the specified handler with this item if this item is of the specified type.  The handler will not be
     * visited if this item is not of the specified type.
     * @param type The type to be checked.
     * @param handler The handler to be visited.
     * @param <V> The type.
     */
    @SuppressWarnings("unchecked")
    public <V> void visit(Type<V> type, Handler<V> handler) {
        if(type.equals(getAssociatedType())) {
            handler.handleItemObject((Item<V>) this);
        }
    }

    @Override
    public int hashCode() {
        return "Item".hashCode() + getAssociatedType().hashCode() + item.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof Item)) {
            return false;
        }
        Item other = (Item) o;
        return this.item.equals(other.item);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Item")
                          .add("type", getAssociatedType().getName())
                          .addValue(item).toString();
    }


    public interface Handler<V> {
        void handleItemObject(Item<V> object);
    }


    public static class Type<T> {

        private static int counter = 0;

        private int hashCode = counter++;

        private String name;

        protected Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper("Item.Type")
                    .addValue(hashCode)
                    .add("name", name).toString();
        }

        @Override
        public int hashCode() {
            return counter;
        }

        @Override
        public boolean equals(Object o) {
            if(o == this) {
                return true;
            }
            if(!(o instanceof Type)) {
                return false;
            }
            Type other = (Type) o;
            return this.hashCode == other.hashCode;
        }
    }

}
